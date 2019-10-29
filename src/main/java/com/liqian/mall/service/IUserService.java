package com.liqian.mall.service;

import com.liqian.mall.common.Result;
import com.liqian.mall.entity.User;

/**
 * @author liqian
 */
public interface IUserService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return Result
     */
    Result login(String username, String password);

    /**
     * 注册
     *
     * @param user 用户表单对象
     * @return Result
     */
    Result register(User user);

    /**
     * 检查用户名或者邮箱是否已经被使用
     *
     * @param str  用户名字符串或者邮箱字符串
     * @param type str字符串类型
     * @return Result
     */
    Result checkValid(String str, String type);

    /**
     * 通过用户名获取修改密码的问题
     *
     * @param username 用户名
     * @return Result
     */
    Result getQuestionByUsername(String username);

    /**
     * 经检查用户的问题和问题答案是否匹配
     * 如果匹配返回一个token
     *
     * @param username 用户名
     * @param question 修改密码问题
     * @param answer   修改密码问题答案
     * @return Result
     */
    Result checkAnswer(String username, String question, String answer);

    /**
     * 未登录状态，通过token修改密码
     *
     * @param username    用户名
     * @param passwordNew 新密码
     * @param token       token checkAnswer通过后获得的token
     * @return Result
     */
    Result forgetRestPassword(String username, String passwordNew, String token);

    /**
     * 登录状态下通过旧密码重置新密码
     *
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @param user        session中获取的当前登录的用户
     * @return Result
     */
    Result restPassword(String passwordOld, String passwordNew, User user);

    /**
     * 修改用户
     *
     * @param user 修改用户的Email,question,answer,phone
     * @return Result
     */
    Result updateUser(User user);


}
