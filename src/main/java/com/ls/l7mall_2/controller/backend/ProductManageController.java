package com.ls.l7mall_2.controller.backend;

import com.google.common.collect.Maps;
import com.ls.l7mall_2.entity.Product;
import com.ls.l7mall_2.entity.User;
import com.ls.l7mall_2.global.Const;
import com.ls.l7mall_2.global.ResponseEntity;
import com.ls.l7mall_2.service.CategoryService;
import com.ls.l7mall_2.service.FileService;
import com.ls.l7mall_2.service.ProductService;
import com.ls.l7mall_2.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author laijs
 * @date 2020-3-29-14:19
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private FileService fileService;

    private Logger logger = LoggerFactory.getLogger(ProductManageController.class);

    // 添加OR更新产品
    @RequestMapping("save.do")
    @ResponseBody
    public ResponseEntity saveOrUpdateProduct(HttpSession session, Product product){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 保存产品信息
            return productService.saveOrUpdateProduct(product);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 修改产品状态
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ResponseEntity<String> setSaleStatus(HttpSession session, Integer productId,Integer status){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 保存产品信息
            return productService.setSaleStatus(productId,status);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");

    }

    // 获取产品详细信息
    @RequestMapping("detail.do")
    @ResponseBody
    public ResponseEntity<ProductDetailVo> getDetail(HttpSession session, Integer productId){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取产品详细信息
            return productService.manageProductDetail(productId);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }


    // 分页获取产品列表
    @RequestMapping("list.do")
    @ResponseBody
    public ResponseEntity getList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取产品列表
            return productService.getProductList(pageNum,pageSize);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 产品搜索功能
    @RequestMapping("search.do")
    @ResponseBody
    public ResponseEntity searchProduct(HttpSession session,String productName,Integer productId, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 搜索产品信息
            return productService.searchProductList(productName,productId,pageNum,pageSize);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    @Value("${ftp.server.http.prefix}")
    private String urlprefix;
    
    //  图片上传
    @RequestMapping("upload.do")
    @ResponseBody
    public ResponseEntity upload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile multipartFile, HttpServletRequest request){
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ResponseEntity.responesWhenError("尚未登录，请登录");
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取路径
            String path = request.getServletContext().getRealPath("upload");
            // 上传图片
            String targetFileName = fileService.upload(multipartFile, path);
            if(StringUtils.isBlank(targetFileName)){
                return ResponseEntity.responesWhenError("上传文件失败");
            }
            String url = urlprefix + targetFileName;
            HashMap<String, String> map = Maps.newHashMap();
            map.put("uri",targetFileName);
            map.put("url",url);

            return ResponseEntity.responesWhenSuccess(map);
        }
        return ResponseEntity.responesWhenError("需要管理员权限");
    }

    // 富文本上传
    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile multipartFile, HttpServletRequest request, HttpServletResponse response){
        HashMap resultMap = Maps.newHashMap();
        // 判断是否已经登录
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","尚未登录，请登录");
            return resultMap;
        }
        // 判断是否为管理员
        if(user.getRole() == Const.Role.ROLE_ADMIN){
            // 获取路径
            String path = request.getServletContext().getRealPath("upload");
            // 上传图片
            String targetFileName = fileService.upload(multipartFile, path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传文件失败");
                return resultMap;
            }
            String url = urlprefix + targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;

        }
        resultMap.put("success",false);
        resultMap.put("msg","需要管理员权限");
        return resultMap;
    }
}
