package com.ls.l7mall_2.controller.portal;

import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.ResponseCode;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.UserService;
import com.ls.l7mall_2.global.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author laijs
 * @date 2020-3-28-11:27
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;
    
    /*
   登录功能：返回响应对象类
    */
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> login(String username, String password, HttpSession session){
        ResponseEntity<User> response = userService.login(username, password);
        if(response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    // 登出功能
    @RequestMapping(value = "logout.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> logout(HttpSession session){
        //删除session中的用户信息
        session.removeAttribute(Const.CURRENT_USER);
        return ResponseEntity.responesWhenSuccess();
    }

    // 校验用户名和邮箱是否存在
    @RequestMapping(value = "check_valid.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> checkValid(String str,String type){
        return userService.checkValid(str,type);
    }

    // 注册功能
    @RequestMapping(value = "register.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> register(User user){
        return userService.register(user);
    }

    // 获取用户信息
    @RequestMapping(value = "get_user_info.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，无法获取用户信息");
        }
        return ResponseEntity.responesWhenSuccess(user);
    }

    // 忘记密码——获取安全问题
    @RequestMapping(value = "forget_get_question.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> forgetGetQuestion(String username){
        return userService.getQuestion(username);
    }
    // 忘记密码——校验答案
    @RequestMapping(value = "forget_check_answer.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> forgetCheckAnswer(String username,String question,String answer){
        return userService.checkAnswer(username,question,answer);
    }
    // 忘记密码——重置密码
    @RequestMapping(value = "forget_reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> forgetResetPassword(String username,String newPassword,String forgetToken){
        return userService.forgetResetPassword(username, newPassword, forgetToken);
    }

    // 登录状态——重置密码
    @RequestMapping(value = "reset_password.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> resetPassword(HttpSession session,String oldPassword,String newPassword){
        // 判断用户是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("用户尚未登录");
        }
        // 重置密码
        return userService.resetPassword(oldPassword,newPassword,user);
    }

    // 更新用户个人信息
    @RequestMapping(value = "update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> updateInformation(HttpSession session,User user){
        // 判断用户是否已经登录
        User loginUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(loginUser == null){
            return ResponseEntity.responesWhenError("用户尚未登录");
        }
        // 更新信息
        user.setUsername(loginUser.getUsername());
        user.setId(loginUser.getId());
        ResponseEntity<User> response = userService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(loginUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }

    // 获取用户详细信息
    @RequestMapping(value = "get_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> getInformation(HttpSession session){
        // 判断用户是否已经登录
        User loginUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(loginUser == null){
            // 强制登录
            return ResponseEntity.responesWhenError(ResponseCode.NEED_LOGIN.getCode(),"用户尚未登录");
        }
        // 获取信息
        return userService.getInformation(loginUser.getId());
    }

}
