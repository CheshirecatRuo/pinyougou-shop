package com.pinyougou.sellersgoods.service.impl;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbFreightTemplateMapper;
import com.pinyougou.pojo.TbFreightTemplate;
import com.pinyougou.pojo.TbFreightTemplateExample;
import com.pinyougou.pojo.TbFreightTemplateExample.Criteria;
import com.pinyougou.sellersgoods.service.FreightTemplateService;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class FreightTemplateServiceImpl implements FreightTemplateService {

	@Autowired
	private TbFreightTemplateMapper freightTemplateMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbFreightTemplate> findAll() {
		return freightTemplateMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbFreightTemplate> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbFreightTemplate> page=   (Page<TbFreightTemplate>) freightTemplateMapper.selectByExample(null);
		return new PageInfo<TbFreightTemplate>(page);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbFreightTemplate freightTemplate) {
		freightTemplateMapper.insert(freightTemplate);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbFreightTemplate freightTemplate){
		freightTemplateMapper.updateByPrimaryKey(freightTemplate);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbFreightTemplate findOne(Long id){
		return freightTemplateMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			freightTemplateMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
	@Override
	public PageInfo<TbFreightTemplate> findPage(TbFreightTemplate freightTemplate, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbFreightTemplateExample example=new TbFreightTemplateExample();
		Criteria criteria = example.createCriteria();
		
		if(freightTemplate!=null){			
						if(freightTemplate.getSellerId()!=null && freightTemplate.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+freightTemplate.getSellerId()+"%");
			}
			if(freightTemplate.getIsDefault()!=null && freightTemplate.getIsDefault().length()>0){
				criteria.andIsDefaultLike("%"+freightTemplate.getIsDefault()+"%");
			}
			if(freightTemplate.getName()!=null && freightTemplate.getName().length()>0){
				criteria.andNameLike("%"+freightTemplate.getName()+"%");
			}
			if(freightTemplate.getSendTimeType()!=null && freightTemplate.getSendTimeType().length()>0){
				criteria.andSendTimeTypeLike("%"+freightTemplate.getSendTimeType()+"%");
			}
	
		}
		
		Page<TbFreightTemplate> page= (Page<TbFreightTemplate>)freightTemplateMapper.selectByExample(example);		
		return new PageInfo<TbFreightTemplate>(page);
	}
	
}
