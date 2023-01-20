package com.gdut.boot.handler.reflect;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.constance.common.AllClass;
import com.gdut.boot.exception.BusinessException;
import com.gdut.boot.handler.reflect.invoker.MyGetFieldInvoker;
import com.gdut.boot.handler.reflect.invoker.MySetFieldInvoker;
import com.gdut.boot.handler.reflect.invoker.ServiceInvoker;
import com.gdut.boot.handler.reflect.property.MyPropertyNamer;
import com.gdut.boot.handler.reflect.wrapper.AnnotationWrapper;
import com.gdut.boot.util.MapUtil;
import lombok.Getter;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.reflection.TypeParameterResolver;
import org.apache.ibatis.reflection.invoker.AmbiguousMethodInvoker;
import org.apache.ibatis.reflection.invoker.Invoker;
import org.apache.ibatis.reflection.invoker.MethodInvoker;

import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;

import static com.gdut.boot.constance.cache.Cache.SERVICE_CLASS;
import static com.gdut.boot.util.CommonUtil.getGet;
import static com.gdut.boot.util.CommonUtil.getSet;

/**
 * @author JLHWASX
 * @Description 反射器
 * @verdion
 * @date 2022/3/27 14:43
 */

@Getter
public class MyReflector<T> {
    private final Class<T> type;    //类的类型
    private final T entity;    //实体类
    private final String[] readablePropertyNames;   //可读的属性名称 get
    private final String[] writablePropertyNames;   //可写的属性名称 set
    private final Map<String, Invoker> setMethods = new HashMap<>();    //set方法
    private final Map<String, Invoker> getMethods = new HashMap<>();    //get方法
    private ServiceInvoker serviceInvoker;                        //service执行器
    private final Map<String, Class<?>> setTypes = new HashMap<>();     //set属性的类型
    private final Map<String, Class<?>> getTypes = new HashMap<>();     //get属性的类型
    private final AnnotationWrapper annotationWrapper;     //属性类型和注解
    private Constructor<?> defaultConstructor;                          //默认的构造器
    //属性字母大写: NAME -> name, AGE -> age
    private Map<String, String> caseInsensitivePropertyMap = new HashMap<>();

    //构造器：传入一个class类型生成里面的这些属性
    public MyReflector(Class<T> clazz) throws Exception {
        type = clazz;
        entity = type.getDeclaredConstructor().newInstance();
        //设置默认的构造器, 参数必须无参构造器
        addDefaultConstructor(clazz);
        //处理 clazz 中的 get 方法，填充 getMethods 集合和 getTypes 集合
        addGetMethods(clazz);
        //处理 clazz 中的 set 方法，填充 setMethods 集合和 setTypes 集合
        addSetMethod(clazz);
        //添加参数集合
        addFields(clazz);
        //设置service
        addServiceInvoke(clazz);
        //设置可读的属性名称
        readablePropertyNames = getMethods.keySet().toArray(new String[0]);
        //设置可写的属性名称
        writablePropertyNames = setMethods.keySet().toArray(new String[0]);
        //设置注解存储
        annotationWrapper = new AnnotationWrapper(clazz);
        //设置大小写转换
        for (String name : readablePropertyNames) {
            caseInsensitivePropertyMap.put(name.toUpperCase(Locale.ENGLISH), name);
        }
        for (String name : writablePropertyNames) {
            caseInsensitivePropertyMap.put(name.toUpperCase(Locale.ENGLISH), name);
        }
    }

    //设置serviceInvoker
    private void addServiceInvoke(Class clazz) throws Exception {
        serviceInvoker = new ServiceInvoker(SERVICE_CLASS.get(AllClass.valueOf(clazz.getSimpleName()).getValue()));
    }


    /**
     * 会把所有的无 getter/setter 的字段添加到 setMethods、setTypes、getMethods、getTypes中
     * 添加的类型是其他的一些特殊的执行器
     * @param clazz 类
     */
    private void addFields(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if(!setMethods.containsKey(field.getName())){
                int modifiers = field.getModifiers();
                //如果不是final的变量和static的变量
                if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers))) {
                    //加入set里面
                    addSetField(field);
                }
            }
            if (!getMethods.containsKey(field.getName())) {
                addGetField(field);
            }
        }
    }

    /**
     * 添加get方法
     * @param field
     */
    private void addGetField(Field field) {
        if(isValidPropertyName(field.getName())){
            try {
                getMethods.put(field.getName(), new MyGetFieldInvoker(field, this.getType().getMethod(getGet(field.getName()))));
                Type fieldType = TypeParameterResolver.resolveFieldType(field, type);
                getTypes.put(field.getName(), typeToClass(fieldType));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 添加set方法
     * @param field
     */
    private void addSetField(Field field) {
        if(isValidPropertyName(field.getName())){
            //属性满足条件, 就设置set专属的执行器
            try {
                setMethods.put(field.getName(), new MySetFieldInvoker(field, this.getType().getMethod(getSet(field.getName()), field.getType())));
                Type fieldType = TypeParameterResolver.resolveFieldType(field, type);
                setTypes.put(field.getName(), typeToClass(fieldType));
            } catch (NoSuchMethodException e) {
                throw new BusinessException(Msg.fail("MyReflector"), "addSetField", e,
                        Arrays.asList(field),
                        MyReflector.class);
            }

        }
    }


    /**
     * 添加set方法
     * @param clazz 类
     */
    private void addSetMethod(Class<?> clazz) {
        //一样的Map集合
        Map<String, List<Method>> conflictingSetters = new HashMap<>();
        //获取所有的set方法
        Method[] methods = getClassMethods(clazz);
        //筛查出只有一个参数的set集合
        Arrays.stream(methods).filter(method -> method.getParameters().length == 1 && MyPropertyNamer.isSet(method.getName()))
                .forEach(m -> addMethodConflict(conflictingSetters, MyPropertyNamer.methodToProperty(m.getName()), m));
        resolveSetterConflicts(conflictingSetters);
    }

    /**
     * 筛选出set方法
     * @param conflictingSetters map集合
     */
    private void resolveSetterConflicts(Map<String, List<Method>> conflictingSetters) {
        Set<Map.Entry<String, List<Method>>> entries = conflictingSetters.entrySet();
        for (Map.Entry<String, List<Method>> entry : entries) {
            //获取属性名字
            String propName = entry.getKey();
            //所有的set方法
            List<Method> value = entry.getValue();
            //get方法对应的类型
            Class<?> getterType = getTypes.get(propName);
            //判断这个get类型的方法是不是存在
            boolean isGetterAmbiguous = getMethods.get(propName) instanceof AmbiguousMethodInvoker;
            //set方法存不存在
            boolean isSetterAmbiguous = false;
            Method best = null;
            for (Method setMethod : value) {
                if(!isGetterAmbiguous && setMethod.getParameters()[0].equals(getterType)){
                    //get方法有效并且此时set方法的参数和get方法返回值类型一样
                    best = setMethod;
                    break;
                }
                //get方法的返回值和set方法的参数不一样
                if(!isGetterAmbiguous){
                    best = pickBestSetMethod(best, setMethod, propName);
                    //如果s方法返回null, 那么set方法就是有问题的, 不符合我们需要的逻辑
                    isSetterAmbiguous = best == null;
                }
            }
            if (best != null) {
                addSetMethod(propName, best);
            }
        }
    }

    /**
     * 添加进set的缓存中去
     * @param propName 方法属性名称
     * @param method 方法
     */
    private void addSetMethod(String propName, Method method) {
        //封装方法
        MethodInvoker invoker = new MethodInvoker(method);
        //加入set集合
        setMethods.put(propName, invoker);
        //获取set的参数
        Type[] paramTypes = TypeParameterResolver.resolveParamTypes(method, type);
        //加入到参数类型集合中
        setTypes.put(propName, typeToClass(paramTypes[0]));
    }

    /**
     * 存在两个或以上的set方法, 比如子类和父类都有，这时候要挑一个最合适的
     * @param setter1 set方法1
     * @param setter2 set方法2
     * @param propName 方法对应的参数名
     * @return best
     */
    private Method pickBestSetMethod(Method setter1, Method setter2, String propName) {
        if(setter1 == null){
            return setter2;
        }
        Class<?> paramType1 = setter1.getParameterTypes()[0];
        Class<?> paramType2 = setter2.getParameterTypes()[0];
        //看哪一个是父类, 就返回子类
        if (paramType1.isAssignableFrom(paramType2)) {
            return setter2;
        } else if (paramType2.isAssignableFrom(paramType1)) {
            return setter1;
        }
        //如果都不是, 那么就是有异常了
        MethodInvoker invoker = new AmbiguousMethodInvoker(setter1,
                MessageFormat.format(
                        "Ambiguous setters defined for property ''{0}'' in class ''{1}'' with types ''{2}'' and ''{3}''.",
                        propName, setter2.getDeclaringClass().getName(), paramType1.getName(), paramType2.getName()));
        setMethods.put(propName, invoker);
        Type[] paramTypes = TypeParameterResolver.resolveParamTypes(setter1, type);
        setTypes.put(propName, typeToClass(paramTypes[0]));
        return null;
    }

    /**
     * 设置默认的构造器
     * @param clazz 类
     */
    private void addDefaultConstructor(Class<?> clazz) {
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        if(constructors.length == 0){
            throw new ReflectionException("构造器不能为空, 必须提供一个无参构造器");
        }
        Arrays.stream(constructors).filter(constructor -> constructor.getParameterTypes().length == 0)
                //把无参构造器赋值给defaultConstructor属性
                .findAny().ifPresent(constructor -> this.defaultConstructor = constructor);
        //最后判断
        if(this.defaultConstructor == null){
            throw new ReflectionException("构造器不能为空, 必须提供一个无参构造器");
        }
    }

    /**
     * 设置get方法
     * 1. 加入所有get方法
     * 2. 筛选出那些和父类重复的方法
     * 3. 对一些特殊类的get方法进行筛选
     * @param clazz
     */
    private void addGetMethods(Class<?> clazz){
        //里面存放了子类和父类共同的get和set方法
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        //获取所有的方法
        Method[] methods = getClassMethods(clazz);
        //做一个过滤，把所有传入参数=0的过滤出来, 参数为0的才有可能是get方法
        Arrays.stream(methods).filter(method -> method.getParameters().length == 0 && MyPropertyNamer.isGet(method.getName()))
                .forEach(m -> addMethodConflict(conflictingGetters, MyPropertyNamer.methodToProperty(m.getName()), m));
        //解决子类和父类之间get方法的冲突,比如父类有一个getName方法，子类也有，但是父类的方法返回值和子类的不一样, 那么我们就需要从map中去除掉这个方法
        resolveGetterConflicts(conflictingGetters);
    }

    /**
     * 解决子类和父类方法之间的冲突, 比如一个类中可能有很多个方法
     * @param conflictingGetters
     */
    private void resolveGetterConflicts(Map<String, List<Method>> conflictingGetters) {
        Set<Map.Entry<String, List<Method>>> entries = conflictingGetters.entrySet();
        for (Map.Entry<String, List<Method>> entry : entries) {
            //最终选择的方法
            Method finalMethod = null;
            boolean isAmbiguous = false;
            //遍历value中的所有的方法
            for (Method method : entry.getValue()) {
                //正常来说一个变量只有一个get方法的
                if(finalMethod == null){
                    finalMethod = method;
                    continue;
                }
                //如果到这里了证明这个变量最少设置了两个get方法
                Class<?> returnType = finalMethod.getReturnType();
                Class<?> otherType = method.getReturnType();    //另一个重复的
                if(returnType == otherType){
                    //返回值都一样, 证明没有使用多态，直接子类重写父类方法
                    //到这里, 如果一个变量是boolean类型的, 那么可能设置了get和is方法, 这时候要以is方法为准
                    if(!boolean.class.equals(otherType)){
                        isAmbiguous = true;
                        break;
                    }else if(otherType.getName().startsWith("is")){
                        //如果多余的类型是is开头的get方法, 那么就以这个为标准
                        finalMethod = method;
                    }
                }else if(otherType.isAssignableFrom(returnType)){
                    //第二个找到的是第一个的父类, 那么这时候用第一个就好了
                }else if(returnType.isAssignableFrom(otherType)){
                    //第二个是第一个的子类, 那么要以子类为标准
                    finalMethod = method;
                }else{
                    //不知道什么东西
                    isAmbiguous = true;
                    break;
                }
            }
            //把这个get方法最终存入缓存中
            addGetMethods(entry.getKey(), finalMethod, isAmbiguous);
        }
    }

    private void addGetMethods(String name, Method method, boolean isAmbiguous) {
        MethodInvoker invoker = null;
        if(isAmbiguous == true){
            invoker = new AmbiguousMethodInvoker(method, MessageFormat.format(
                    "Illegal overloaded getter method with ambiguous type for property ''{0}'' in class ''{1}''. This breaks the JavaBeans specification and can cause unpredictable results.",
                    name, method.getDeclaringClass().getName()));
        }
        getMethods.put(name, invoker);
        //获取参数方法的类型
        Type returnType = TypeParameterResolver.resolveReturnType(method, type);
        //属性对应的变量
        getTypes.put(name, typeToClass(returnType));
    }

    /**
     * 类类型变量的解析
     * @param src 类类型
     * @return
     */
    private Class<?> typeToClass(Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            //普通类类型
            result = (Class<?>) src;
        } else if (src instanceof ParameterizedType) {
            //泛型参数类型
            result = (Class<?>) ((ParameterizedType) src).getRawType();
        }else if (src instanceof GenericArrayType) {
            //数组类型，且元素类型为 ParameterizedType 和 TypeVariable 。如 List<String>[] 或 T[]
            //获取原始变量的类型
            Type componentType = ((GenericArrayType) src).getGenericComponentType();
            //如果是Class
            if (componentType instanceof Class) {
                //就实际new一个出来获取具体类型
                result = Array.newInstance((Class<?>) componentType, 0).getClass();
            } else {
                //否则就证明里面可能还有list或者数组, 继续递归
                Class<?> componentClass = typeToClass(componentType);
                result = Array.newInstance(componentClass, 0).getClass();
            }
        }
        if(result == null){
            result = Object.class;
        }
        return result;
    }

    /**
     * 把方法加入到对应的map中
     * @param conflictingMethods map
     * @param name 参数
     * @param method get方法
     */
    private void addMethodConflict(Map<String, List<Method>> conflictingMethods, String name, Method method) {
        if (isValidPropertyName(name)) {
            //获取list
            List<Method> list = MapUtil.computeIfAbsent(conflictingMethods, name, k -> new ArrayList<>());
            //往list中加入method
            list.add(method);
        }
    }

    /**
     * 判断是不是一个合法的参数名字
     * @param name 参数名
     * @return true or false
     */
    private boolean isValidPropertyName(String name) {
        return !(name.startsWith("$") || "serialVersionUID".equals(name) || "class".equals(name));
    }

    // 解析 Method 方法
    private Method[] getClassMethods(Class<?> clazz) {
        //用于存储类中唯一的签名对应的method对象
        Map<String, Method> uniqueMethods = new HashMap<>();
        Class<?> currentClass = clazz;
        while(currentClass != null && currentClass != Object.class){
            //当前类不为null并且当前类不等于顶级父类, 添加类里面的方法
            addUniqueMethods(uniqueMethods, currentClass.getDeclaredMethods());
            //接口类型的, 抽象方法也要加入里面的方法
            Class<?>[] interfaces = currentClass.getInterfaces();
            for (Class<?> anInterface : interfaces) {
                addUniqueMethods(uniqueMethods, anInterface.getDeclaredMethods());
            }
            //往父类上遍历
            currentClass = currentClass.getSuperclass();
        }
        return uniqueMethods.values().toArray(new Method[0]);
    }

    /**
     * 把所有方法添加到uniqueMethods里面去
     * @param uniqueMethods map集合
     * @param methods 所有的方法
     */
    private void addUniqueMethods(Map<String, Method> uniqueMethods, Method[] methods) {
        for (Method method : methods) {
            //不是桥接方法, 桥接方法就是说这个方法是泛型，但是由于要适配虚拟机，参数就自动换成Object，实际上是没有这个Object参数的
            if(!method.isBridge()){
                // 通过 Reflector.getSignature () 方法得到的方法签名是:
                // 返回值类型#方法名称:参数类型列表。
                // 可以作为该方法的唯一标识
                String signature = getSignature(method);
                if(!uniqueMethods.containsKey(signature)){
                    uniqueMethods.put(signature, method);
                }
            }
        }
    }

    /**
     * 获取方法的签名
     * @param method 方法
     * @return 返回值类型#方法名称:参数类型列表。
     */
    private String getSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        //返回值全名
        String name = method.getReturnType().getName();
        sb.append(name + "#" + method.getName());
        Class<?>[] types = method.getParameterTypes();
        for (int i = 0; i < types.length; i++) {
            if(i == 0){
                //第一个
                sb.append(":" + types[i].getName());
            }else{
                //不是第一个
                sb.append("?" + types[i].getName());
            }
        }
        return sb.toString();
    }

    /**
     * 有无权限控制
     * @return
     */
    public static boolean canControlMemberAccessible() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (null != securityManager) {
                securityManager.checkPermission(new ReflectPermission("suppressAccessChecks"));
            }
        } catch (SecurityException e) {
            return false;
        }
        return true;
    }

    /**
     * 获取类的类型
     * @return
     */
    public Class<?> getType() {
        return type;
    }

    /**
     * 获取默认的选择器
     * @return
     */
    public Constructor<?> getDefaultConstructor() {
        if (defaultConstructor != null) {
            return defaultConstructor;
        } else {
            throw new ReflectionException("There is no default constructor for " + type);
        }
    }

    /**
     * 查看是否具有默认的选择器
     * @return
     */
    public boolean hasDefaultConstructor() {
        return defaultConstructor != null;
    }

    /**
     * 获取set执行器
     * @param propertyName
     * @return
     */
    public Invoker getSetInvoker(String propertyName) {
        Invoker method = setMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
        }
        return method;
    }

    /**
     * 获取get方法的执行器
     * @param propertyName
     * @return
     */
    public Invoker getGetInvoker(String propertyName) {
        Invoker method = getMethods.get(propertyName);
        if (method == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
        }
        return method;
    }

    /**
     * 获取set属性的类型
     * @param propertyName
     * @return
     */
    public Class<?> getSetterType(String propertyName) {
        Class<?> clazz = setTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no setter for property named '" + propertyName + "' in '" + type + "'");
        }
        return clazz;
    }


    /**
     * 获取get属性的类型
     * @param propertyName
     * @return
     */
    public Class<?> getGetterType(String propertyName) {
        Class<?> clazz = getTypes.get(propertyName);
        if (clazz == null) {
            throw new ReflectionException("There is no getter for property named '" + propertyName + "' in '" + type + "'");
        }
        return clazz;
    }

    /**
     * 获取所有get方法的属性名字
     * @return
     */
    public String[] getGetablePropertyNames() {
        return readablePropertyNames;
    }

    /**
     * 获取所有的set方法的属性的名字
     * @return
     */
    public String[] getSetablePropertyNames() {
        return writablePropertyNames;
    }

    /**
     * 参数有没有set方法
     * @param propertyName
     * @return
     */
    public boolean hasSetter(String propertyName) {
        return setMethods.containsKey(propertyName);
    }

    /**
     * 参数有没有get方法
     * @param propertyName
     * @return
     */
    public boolean hasGetter(String propertyName) {
        return getMethods.containsKey(propertyName);
    }

    /**
     * 找参数
     * @param name
     * @return
     */
    public String findPropertyName(String name) {
        return caseInsensitivePropertyMap.get(name.toUpperCase(Locale.ENGLISH));
    }

    public AnnotationWrapper getAnnotationWrapper() {
        return annotationWrapper;
    }
}
