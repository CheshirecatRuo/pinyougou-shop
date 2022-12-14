package com.pinyougou.sellersgoods.service.impl;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojo.TbOrderItemExample;
import com.pinyougou.pojo.TbOrderItemExample.Criteria;
import com.pinyougou.sellersgoods.service.OrderItemService;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

	@Autowired
	private TbOrderItemMapper orderItemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrderItem> findAll() {
		return orderItemMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbOrderItem> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrderItem> page=   (Page<TbOrderItem>) orderItemMapper.selectByExample(null);
		return new PageInfo<TbOrderItem>(page);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrderItem orderItem) {
		orderItemMapper.insert(orderItem);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrderItem orderItem){
		orderItemMapper.updateByPrimaryKey(orderItem);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrderItem findOne(Long id){
		return orderItemMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderItemMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageInfo<TbOrderItem> findPage(TbOrderItem orderItem, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderItemExample example=new TbOrderItemExample();
		Criteria criteria = example.createCriteria();
		
		if(orderItem!=null){			
						if(orderItem.getTitle()!=null && orderItem.getTitle().length()>0){
				criteria.andTitleLike("%"+orderItem.getTitle()+"%");
			}
			if(orderItem.getPicPath()!=null && orderItem.getPicPath().length()>0){
				criteria.andPicPathLike("%"+orderItem.getPicPath()+"%");
			}
			if(orderItem.getSellerId()!=null && orderItem.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+orderItem.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrderItem> page= (Page<TbOrderItem>)orderItemMapper.selectByExample(example);		
		return new PageInfo<TbOrderItem>(page);
	}
	
}
