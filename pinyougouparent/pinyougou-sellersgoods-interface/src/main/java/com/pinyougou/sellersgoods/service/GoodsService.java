package com.pinyougou.sellersgoods.service;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbItem;


/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbGoods> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbGoods> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbGoods goods);
	
	
	/**
	 * 修改
	 */
	public void update(TbGoods goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbGoods findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public int delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageInfo<TbGoods> findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 修改Goods状态
	 * @param ids
	 * @param status
	 * @return
	 */
	public int updateStatus(List<Long> ids, String status);

	/**
	 * 根据GoodsIds查询Items
	 * @param ids
	 * @param status
	 * @return
	 */
	public List<TbItem> getByGoodIds(List<Long> ids, String status);
}
