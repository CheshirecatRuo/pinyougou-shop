package com.pinyougou.sellersgoods.service;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbTypeTemplate;


/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface TypeTemplateService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbTypeTemplate> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbTypeTemplate> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbTypeTemplate typeTemplate);
	
	
	/**
	 * 修改
	 */
	public void update(TbTypeTemplate typeTemplate);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbTypeTemplate findOne(Long id);
	
	
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
	public PageInfo<TbTypeTemplate> findPage(TbTypeTemplate typeTemplate, int pageNum, int pageSize);

	/**
	 * 返回所有类型，封装for select2,格式：{"id":1,"text":"xxx"}
	 * @return
	 */
	public List<Map<String, Object>> findAllForList();

	/**
	 * 根据模板ID查询规格选项信息
	 * @param id
	 * @return
	 */
	public List<Map> findOptionsByTypeId(Long id);
}
