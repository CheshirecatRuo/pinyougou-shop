package com.pinyougou.sellersgoods.service;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbCities;


/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface CitiesService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbCities> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbCities> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbCities cities);
	
	
	/**
	 * 修改
	 */
	public void update(TbCities cities);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbCities findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageInfo<TbCities> findPage(TbCities cities, int pageNum, int pageSize);
	
}
