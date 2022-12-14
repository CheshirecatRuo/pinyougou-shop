package com.pinyougou.sellersgoods.service;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbPayLog;


/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface PayLogService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbPayLog> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbPayLog> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbPayLog payLog);
	
	
	/**
	 * 修改
	 */
	public void update(TbPayLog payLog);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbPayLog findOne(Long id);
	
	
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
	public PageInfo<TbPayLog> findPage(TbPayLog payLog, int pageNum, int pageSize);
	
}
