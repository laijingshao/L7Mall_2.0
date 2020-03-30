package com.ls.l7mall_2.controller.backend;

import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @author laijs
 * @date 2020-3-29-11:33
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    private CategoryService categoryService;

    // 添加分类功能
    @RequestMapping(value = "add_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity addCategory(HttpSession session,String categoryName,@RequestParam(value = "parentId",defaultValue = "0") int parentId){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 添加分类
            return categoryService.addCategory(categoryName,parentId);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 更新分类的名字
    @RequestMapping(value = "set_category_name.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setCategoryName(HttpSession session,int categoryId,String categoryName){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 更新分类的名字
            return categoryService.setCategoryName(categoryId,categoryName);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 获取当前分类的子节点（平级）
    @RequestMapping(value = "get_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getCategory(HttpSession session,@RequestParam(value = "categoryId",defaultValue = "0") int categoryId){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取节点
            return categoryService.getChildrenCategoryById(categoryId);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 获取当前分类的子节点（递归）
    @RequestMapping(value = "get_deep_category.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity getDeepCategory(HttpSession session, @RequestParam(value = "categoryId",defaultValue = "0") int categoryId){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取节点并递归
            return categoryService.getDeepChildrenCategoryById(categoryId);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }
}
