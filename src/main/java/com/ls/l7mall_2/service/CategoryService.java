package com.ls.l7mall_2.service;

import com.ls.l7mall_2.entity.Category;
import com.ls.l7mall_2.global.ResponseEntity;

import java.util.List;

/**
 * @author laijs
 * @date 2020-3-29-11:34
 */
public interface CategoryService {
    // 添加分类
    public ResponseEntity addCategory(String categoryName, Integer parentId);

    // 更新分类名称
    public ResponseEntity setCategoryName(Integer categoryId,String categoryName);

    // 获取分类的子节点
    public ResponseEntity<List<Category>> getChildrenCategoryById(Integer categoryId);

    // 递归获取分类的子节点
    public ResponseEntity<List<Integer>> getDeepChildrenCategoryById(Integer categoryId);
}
