package com.gdut.boot.handler.generalHandler;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.bean.RequestMessage;

public abstract class Handler<T>{

    protected Handler handler;
    public Handler next;


    public void next(Handler handler) {
        this.handler = handler;
    }

    //处理请求接口, 同一返回Msg
    public abstract Msg deal(RequestMessage requestMessage);

    //创建者模式
    public static class Builder<T>{
        private Handler<T> head;
        private Handler<T> tail;

        public Builder<T> addHandler(Handler<T> handler) {
            if (this.head == null) {
                this.head = this.tail = handler;
                this.tail.next = null;
                return this;
            }
            //设置handler
            this.tail.next(handler);
            //设置next对象
            this.tail.next = handler;
            //尾巴
            this.tail = handler;
            return this;
        }

        //头结点
        public Handler<T> build() {
            return this.head;
        }
    }
}
