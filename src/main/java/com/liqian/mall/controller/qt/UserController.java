package com.liqian.mall.controller.qt;

import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 用户接口（已测试）
 *
 * @author liqian
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private IUserService iUserService;

    @GetMapping("login")
    public Result login(String username, String password, HttpSession httpSession) {
        Result result = iUserService.login(username, password);
        httpSession.setAttribute(Const.CURRENT_USER, result.getData());
        return result;
    }

    @GetMapping("logout")
    public Result logout(HttpSession httpSession) {
        httpSession.removeAttribute(Const.CURRENT_USER);
        return Result.createBySuccess();
    }

    @PostMapping("register")
    public Result register(User user) {
        return iUserService.register(user);
    }

    @PostMapping("checkValid")
    public Result checkValid(String str, String type) {
        return iUserService.checkValid(str, type);
    }


    @GetMapping("getUserInfo")
    public Result getUserInfo(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return Result.createBySuccess(user);
    }

    @GetMapping("getQuestion")
    public Result forgetGetQuestion(String username) {
        return iUserService.getQuestionByUsername(username);
    }

    @GetMapping("checkAnswer")
    public Result forgetCheckAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    @GetMapping("forgetRestPassword")
    public Result forgetRestPassword(String username, String passwordNew, String token) {
        return iUserService.forgetRestPassword(username, passwordNew, token);
    }

    @GetMapping("restPassword")
    public Result restPassword(HttpSession session, String passwordOld, String passwordNew) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        return iUserService.restPassword(passwordOld, passwordNew, user);
    }

    @GetMapping("updateUser")
    public Result updateUser(HttpSession session, User user) {
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        Result result = iUserService.updateUser(user);
        session.setAttribute(Const.CURRENT_USER, result.getData());
        return result;
    }


}
