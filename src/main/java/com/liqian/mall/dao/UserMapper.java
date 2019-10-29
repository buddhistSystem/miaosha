package com.liqian.mall.dao;

import com.liqian.mall.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int checkUsername(String username);

    int checkEmail(String email);

    /**
     * 功能描述: 通过用户名密码查找用户
     *
     * @param: username 用户名
     * @Param: password 密码
     * @return: User    用户对象
     * @author: liqian
     * @date: 2019/8/20 0020 15:18
     */
    User selectLogin(@Param("username") String username,
                     @Param("password") String password);


    String getQuestionByUsername(String username);


    int checkAnswer(@Param("username") String username,
                    @Param("question") String question,
                    @Param("answer") String answer);

    int updatePasswordByUsername(@Param("username") String username,
                                 @Param("passwordNew") String passwordNew);


    int checkPassword(@Param("password") String password,@Param("userId")Integer userId);


}