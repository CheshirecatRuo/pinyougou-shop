package com.pinyougou.sellersgoods.service.impl;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellersgoods.service.SpecificationService;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbSpecification> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageInfo<TbSpecification>(page);
	}

	/**
	 * 增加
	 */
	@Override
	public int add(TbSpecification specification) {
		//将规格信息增加到规格表
		int account = specificationMapper.insert(specification);

		//获取规格信息的主键，并将主键赋值给specificationOptionList的spec_id
		//@GeneratedValue(strategy = GenerationType.IDENTITY)

		//将规格选项增加到规格选项表
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();

		//循环增加
		for (TbSpecificationOption specificationOption: specificationOptionList)
		{
			//将主键赋值给specificationOptionList的spec_id
			specificationOption.setSpecId(specification.getId());

			//增加入库
			specificationOptionMapper.insertSelective(specificationOption);
		}
		return account;
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbSpecification specification){
	    //规格信息修改
		int account = specificationMapper.updateByPrimaryKey(specification);
		//删除之前的规格选项 delete from tb_specification_option where spec_id = ?
        Example example = new Example(TbSpecificationOption.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("specId", specification.getId());
        specificationOptionMapper.deleteByExample(example);
        //添加新的规格选项
        List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
        for (TbSpecificationOption specificationOption : specificationOptionList)
        {
            //设置外键关联ID
            specificationOption.setSpecId(specification.getId());
            //增加
            specificationOptionMapper.insert(specificationOption);
        }
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbSpecification findOne(Long id){
	    //规格信息
		TbSpecification specification = specificationMapper.selectByPrimaryKey(id);
		//规格选项
        TbSpecificationOption specificationOption = new TbSpecificationOption();
        specificationOption.setSpecId(specification.getId());
		List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.select(specificationOption);
	    specification.setSpecificationOptionList(specificationOptionList);
	    return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
	    //删除规格信息
		for(Long id:ids){
			specificationMapper.deleteByPrimaryKey(id);
		}
		//批量删除规格选项 delete from tb_specification_option where spec_id in ()
        Example example = new Example(TbSpecificationOption.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("specId", Arrays.asList(ids));
		specificationOptionMapper.deleteByExample(example);
	}
	
	
		@Override
	public PageInfo<TbSpecification> findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageInfo<TbSpecification>(page);
	}

	@Override
	public List<Map<String, Object>> findAllForList() {
		return specificationMapper.findAllForList();
	}

}
