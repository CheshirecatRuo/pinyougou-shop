package com.pinyougou.advertise.service;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbContent;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ContentService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbContent> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbContent> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbContent content);
	
	
	/**
	 * 修改
	 */
	public void update(TbContent content);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbContent findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageInfo<TbContent> findPage(TbContent content, int pageNum,int pageSize);

	/**
	 * 根据分类信息查询广告信息
	 * @param categoryId
	 * @return
	 */
	public List<TbContent> findByCategoryId(Long categoryId);
}
