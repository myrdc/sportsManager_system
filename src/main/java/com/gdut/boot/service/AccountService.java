package com.gdut.boot.service;

import com.gdut.boot.bean.Msg;
import com.gdut.boot.entity.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public interface AccountService extends IService<Account> {

    Msg loginOut(String number);
}
