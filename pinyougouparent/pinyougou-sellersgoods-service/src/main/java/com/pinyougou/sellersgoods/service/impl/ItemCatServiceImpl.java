package com.pinyougou.sellersgoods.service.impl;
import java.util.List;
import java.util.Set;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.pojo.TbItemCatExample;
import com.pinyougou.pojo.TbItemCatExample.Criteria;
import com.pinyougou.sellersgoods.service.ItemCatService;
import org.springframework.data.redis.core.RedisTemplate;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 查询全部
	 */
	@Override
	public List<TbItemCat> findAll() {
		return itemCatMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbItemCat> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbItemCat> page=   (Page<TbItemCat>) itemCatMapper.selectByExample(null);
		return new PageInfo<TbItemCat>(page);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbItemCat itemCat) {
		itemCatMapper.insert(itemCat);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbItemCat itemCat){
		itemCatMapper.updateByPrimaryKey(itemCat);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbItemCat findOne(Long id){
		return itemCatMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids)
	{
		for(Long id:ids) {
			//含有子分类递归调用
			Long[] sonIds = itemCatMapper.findIdsByParentId(id);
			if (sonIds.length > 0) {
				delete(sonIds);
			}
			itemCatMapper.deleteByPrimaryKey(id);
		}
	}
	
	@Override
	public PageInfo<TbItemCat> findPage(TbItemCat itemCat, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbItemCatExample example=new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		
		if(itemCat!=null){			
						if(itemCat.getName()!=null && itemCat.getName().length()>0){
				criteria.andNameLike("%"+itemCat.getName()+"%");
			}
	
		}
		
		Page<TbItemCat> page= (Page<TbItemCat>)itemCatMapper.selectByExample(example);		
		return new PageInfo<TbItemCat>(page);
	}

	/**
	 * select * from tb_item_cat where parent_id = ?
	 * @param id
	 * @return
	 */
	@Override
	public List<TbItemCat> findByParentId(Long id) {
		TbItemCat itemCat = new TbItemCat();
		itemCat.setParentId(id);

		//查询所有分类
		List<TbItemCat> tbItemCats = itemCatMapper.selectAll();

		//加入redis
		for (TbItemCat cat : tbItemCats)
		{
			String key = cat.getName();
			Long value = cat.getTypeId();

			redisTemplate.boundHashOps("itemCat").put(key, value);
		}

		return itemCatMapper.select(itemCat);
	}

}
