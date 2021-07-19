package com.dz.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.dz.o2o.entity.ProductCategory;

public interface ProductCategoryDao {
	/**
	 * 通过shopid查询店铺商铺类别
	 * @param shopId
	 * @return
	 */
	List<ProductCategory> queryProductCategoryList(long shopId);
	
	/**
	 * 批量新增商品类别
	 * @param productCategoryList
	 * @return
	 */
	int batchInsertProductCategory(List<ProductCategory> productCategoryList);
	
	/**
	 * 删除指定商品类别
	 * 
	 * @param productCategoryId
	 * @param shopId
	 * @return effectedNum
	 */
	int deleteProductCategory(@Param("productCategoryId") long productCategoryId, @Param("shopId") long shopId);
}
