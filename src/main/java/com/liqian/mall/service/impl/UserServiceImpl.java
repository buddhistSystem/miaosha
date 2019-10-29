package com.liqian.mall.service.impl;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.common.TokenCache;
import com.liqian.mall.dao.UserMapper;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IUserService;
import com.liqian.mall.util.MD5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

/**
 * 用户模块
 *
 * @author liqian
 */
@Service("iUserService")
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Result login(String username, String password) {
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            throw new BusinessException(EmError.USER_NOT_EXIST);
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            throw new BusinessException(EmError.LOGIN_PASSWORD_ERROR);
        }
        user.setPassword("");
        return Result.createBySuccess(user);
    }

    @Override
    public Result register(User user) {
        if ("".equalsIgnoreCase(user.getPassword())) {
            throw new BusinessException(EmError.PASSWORD_NOT_NULL);
        }
        this.checkValid(user.getUsername(), Const.USERNAME);
        this.checkValid(user.getEmail(), Const.EMAIL);

        user.setRole(Const.Role.ROLE_CUSTOMER);
        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        Date now = new Date();
        user.setCreateTime(now);
        userMapper.insertSelective(user);
        return Result.createBySuccess();
    }

    @Override
    public Result checkValid(String str, String type) {
        if (StringUtils.isEmpty(type)) {
            throw new BusinessException(EmError.PARAMETER_VALIDATION_ERROR);
        } else {
            if (Const.USERNAME.equalsIgnoreCase(type)) {
                if ("".equalsIgnoreCase(str)) {
                    throw new BusinessException(EmError.USERNAME_NOT_NULL);
                }
                int usernameCount = userMapper.checkUsername(str);
                if (usernameCount > 0) {
                    throw new BusinessException(EmError.USERNAME_EXIST);
                }
            }
            if (Const.EMAIL.equalsIgnoreCase(type)) {
                int emailCount = userMapper.checkEmail(str);
                if (emailCount > 0) {
                    throw new BusinessException(EmError.EMAIL_EXIST);
                }
            }
        }
        return Result.createBySuccess();
    }

    @Override
    public Result getQuestionByUsername(String username) {
        if ("".equalsIgnoreCase(username)) {
            throw new BusinessException(EmError.USERNAME_NOT_NULL);
        }
        int usernameCount = userMapper.checkUsername(username);
        if (usernameCount == 0) {
            throw new BusinessException(EmError.USER_NOT_EXIST);
        }
        String question = userMapper.getQuestionByUsername(username);
        return Result.createBySuccess(question);
    }

    @Override
    public Result checkAnswer(String username, String question, String answer) {
        int count = userMapper.checkAnswer(username, question, answer);
        if (count == 0) {
            throw new BusinessException(EmError.QUESTION_ANSWER_ERROR);
        }
        String token = UUID.randomUUID().toString();
        TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
        return Result.createBySuccess(token);
    }

    @Override
    public Result forgetRestPassword(String username, String passwordNew, String token) {
        if ("".equalsIgnoreCase(username)) {
            throw new BusinessException(EmError.USERNAME_NOT_NULL);
        }
        String cacheToken = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if ("".equalsIgnoreCase(cacheToken) || null == cacheToken) {
            throw new BusinessException(EmError.TOKEN_INVALID_OR_EXPIRED);
        }
        if (cacheToken.equals(token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            userMapper.updatePasswordByUsername(username, md5Password);
        } else {
            throw new BusinessException(EmError.TOKEN_ERROR);
        }
        return Result.createBySuccess();
    }

    @Override
    public Result restPassword(String passwordOld, String passwordNew, User user) {
        int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (count == 0) {
            throw new BusinessException(EmError.OLD_PASSWORD_ERROR);
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        userMapper.updateByPrimaryKeySelective(user);
        return Result.createBySuccess();
    }

    @Override
    public Result updateUser(User user) {
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setUsername(user.getUsername());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setPhone(user.getPhone());
        userMapper.updateByPrimaryKeySelective(updateUser);
        return Result.createBySuccess(updateUser);
    }


}
