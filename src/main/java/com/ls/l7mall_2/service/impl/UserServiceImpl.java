package com.ls.l7mall_2.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.global.TokenCache;
import com.ls.l7mall_2.mapper.UserMapper;
import com.ls.l7mall_2.service.UserService;
import com.ls.l7mall_2.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author laijs
 * @date 2020-3-28-11:50
 */
@Service("userService")
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    // 登录功能
    public ResponseEntity<User> login(String username, String password) {
        // 校验用户名是否存在
        int resultCount = userMapper.checkUsername(username);
        if (resultCount == 0) {
            return ResponseEntity.responesWhenError("用户名不存在");
        }
        // 根据用户名和用户密码(MD5加密)找到用户
        String md5password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectToLogin(username, md5password);
        if (user == null) {
            return ResponseEntity.responesWhenError("密码错误");
        }
        // 将置空密码后的用户信息作为响应信息
        user.setPassword(StringUtils.EMPTY);
        return ResponseEntity.responesWhenSuccess("登录成功", user);
    }

    // 校验用户名和email是否已被使用
    public ResponseEntity<String> checkValid(String str, String type) {
        if (type.isBlank()) {
            return ResponseEntity.responesWhenError("参数错误");
        } else {
            // 检查用户名是否已被使用
            if (Const.USERNAME.equals(type)) {
                int resultCount = userMapper.checkUsername(str);
                if (resultCount > 0) {
                    return ResponseEntity.responesWhenError("用户名已被使用");
                }
            }
            // 检查email使用已被使用
            if (Const.EMAIL.equals(type)) {
                int resultCount = userMapper.checkEmail(str);
                if (resultCount > 0) {
                    return ResponseEntity.responesWhenError("email已被使用");
                }
            }
        }
        return ResponseEntity.responesWhenSuccess("校验成功");
    }

    // 注册功能
    public ResponseEntity<String> register(User user) {
        // 检查用户名是否已被使用
        ResponseEntity<String> response = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!response.isSuccess()) {
            return response;
        }
        // 检查email使用已被使用
        response = this.checkValid(user.getemail(), Const.EMAIL);
        if (!response.isSuccess()) {
            return response;
        }
        // 设置用户等级
        user.setRole(Const.Role.ROLE_CUSTOMER);
        // 对密码进行MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        // 持久层操作
        int resultCount = userMapper.insert(user);
        if (resultCount == 0) {
            return ResponseEntity.responesWhenError("注册失败");
        }
        return ResponseEntity.responesWhenSuccess();
    }

    // 获取安全问题
    public ResponseEntity<String> getQuestion(String username) {
        ResponseEntity<String> valid = this.checkValid(username, Const.USERNAME);
        if (valid.isSuccess()) {
            return ResponseEntity.responesWhenError("用户不存在");
        } else {
            String question = userMapper.selectQuestion(username);
            if (question.isBlank()) {
                return ResponseEntity.responesWhenError("安全问题为空");
            } else {
                return ResponseEntity.responesWhenSuccess(question);
            }
        }
    }

    // 校验答案
    public ResponseEntity<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.selectAnswer(username, question, answer);
        if (resultCount > 0) {
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ResponseEntity.responesWhenSuccess(forgetToken);
        }
        return ResponseEntity.responesWhenError("安全问题答案错误");
    }

    // 重置密码
    public ResponseEntity<String> forgetResetPassword(String username, String newPassword,String forgetToken) {
        // 判断forgetToken是否为空
        if(forgetToken.isBlank()){
            return ResponseEntity.responesWhenError("重置密码需要token参数");
        }
        // 判断username是否存在
        ResponseEntity<String> response = this.checkValid(username, Const.USERNAME);
        if(response.isSuccess()){
            return ResponseEntity.responesWhenError("用户不存在");
        }
        // 判断username对应的token是否失效
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ResponseEntity.responesWhenError("token已失效");
        }
        // 判断token和forgetToken是否一致
        if(StringUtils.equals(token,forgetToken)){
            // 调用持久层进行重置密码
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int resultCount = userMapper.updatePassword(username, md5Password);
            if(resultCount>0){
                return ResponseEntity.responesWhenSuccess("重置密码成功");
            } else {
                return ResponseEntity.responesWhenError("重置密码失败");
            }
        } else {
            return ResponseEntity.responesWhenError("token错误，请重新获取");
        }
    }

    // 登录状态下重置密码
    public ResponseEntity<String> resetPassword(String oldPassword, String newPassword, User user) {
        // 判断原密码是否正确
        int resultCount = userMapper.checkPasword(user.getId(), MD5Util.MD5EncodeUtf8(oldPassword));
        if(resultCount == 0){
            return ResponseEntity.responesWhenError("原密码错误");
        }
        // 重置密码
        resultCount = userMapper.updatePassword(user.getUsername(), MD5Util.MD5EncodeUtf8(newPassword));
        if(resultCount > 0 ){
            return ResponseEntity.responesWhenSuccess("重置密码成功");
        }
        return ResponseEntity.responesWhenError("重置密码失败");
    }

    // 更新用户信息
    public ResponseEntity<User> updateInformation(User user) {
        // 校验新的email是否以被使用（校验时跳过本账号）
        int resultCount = userMapper.checkEmailById(user.getId(),user.getemail());
        if(resultCount > 0){
            return ResponseEntity.responesWhenError("email已被使用");
        }
        // 更新信息
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setemail(user.getemail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount > 0){
            return ResponseEntity.responesWhenSuccess("更新个人信息成功",updateUser);
        }
        return ResponseEntity.responesWhenError("更新个人信息失败");
    }

    public ResponseEntity<User> getInformation(Integer userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ResponseEntity.responesWhenError("获取信息失败");
        }
        user.setPassword(StringUtils.EMPTY);
        return ResponseEntity.responesWhenSuccess(user);
    }

    // 获取用户列表
    public ResponseEntity getUserList(Integer pageSize,Integer pageNum){
        Page<Object> page = PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.selectAll();
        PageInfo<User> userPageInfo = new PageInfo<User>(users);
        return ResponseEntity.responesWhenSuccess(userPageInfo);
    }
    
    
    
}
