package com.ls.l7mall_2.controller.backend;

import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author laijs
 * @date 2020-3-29-11:28
 */
@Controller
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private UserService userService;

    // 登录功能
    @RequestMapping(value = "login.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> login(String username, String password, HttpSession session){
        ResponseEntity<User> loginResponse = userService.login(username, password);
        if(loginResponse.isSuccess()){
            // 判断是否为管理员
            User user = loginResponse.getData();
            if(user.getRole() == Const.Role.ROLE_ADMIN){
                session.setAttribute(Const.CURRENT_USER,user);
                return loginResponse;
            }
            return ResponseEntity.responesWhenError("管理员账号才能登录");
        }
        return loginResponse;
    }

    // 获取用户列表功能
    @RequestMapping(value = "list.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity list(HttpSession session, @RequestParam(value = "pageSize",defaultValue = "10") int pageSize, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取用户列表
            return userService.getUserList(pageSize,pageNum);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }
}
