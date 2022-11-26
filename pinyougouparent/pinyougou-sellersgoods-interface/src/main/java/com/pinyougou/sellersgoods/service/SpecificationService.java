package com.pinyougou.sellersgoods.service;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbSpecification;


/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface SpecificationService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbSpecification> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbSpecification> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public int add(TbSpecification specification);
	
	
	/**
	 * 修改
	 */
	public void update(TbSpecification specification);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbSpecification findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageInfo<TbSpecification> findPage(TbSpecification specification, int pageNum, int pageSize);

	/**
	 * 查询所有规格，并封装数据for select2:{"id":1,"text":"xxx"}
	 * @return
	 */
	public List<Map<String, Object>> findAllForList();
}
