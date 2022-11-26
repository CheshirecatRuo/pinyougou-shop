package com.pinyougou.advertise.service.impl;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbContentMapper;
import com.pinyougou.pojo.TbContent;
import com.pinyougou.pojo.TbContentExample;
import com.pinyougou.pojo.TbContentExample.Criteria;
import com.pinyougou.advertise.service.ContentService;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JedisConnectionFactory jedisConnectionFactory;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbContent> findAll() {
		return contentMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbContent> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbContent> page=   (Page<TbContent>) contentMapper.selectByExample(null);
		return new PageInfo<TbContent>(page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbContent content) {
		contentMapper.insert(content);
		//清空redis缓存
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbContent content){
		contentMapper.updateByPrimaryKey(content);
		//清空redis缓存
		redisTemplate.boundHashOps("content").delete(content.getCategoryId());
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbContent findOne(Long id){
		return contentMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {

		TbContentExample example2 = new TbContentExample();
		Criteria criteria2 = example2.createCriteria();
		criteria2.andIdIn(Arrays.asList(ids));
		List<TbContent> contents = contentMapper.selectByExample(example2);

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();

		criteria.andIdIn(Arrays.asList(ids));

		int count = contentMapper.deleteByExample(example);

		//清空缓存
		if (count > 0)
		{
			for (TbContent content : contents)
			{
				redisTemplate.boundHashOps("content").delete(content.getCategoryId());
			}
		}

	}
	
	
		@Override
	public PageInfo<TbContent> findPage(TbContent content, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbContentExample example=new TbContentExample();
		Criteria criteria = example.createCriteria();
		
		if(content!=null){			
						if(content.getTitle()!=null && content.getTitle().length()>0){
				criteria.andTitleLike("%"+content.getTitle()+"%");
			}
			if(content.getUrl()!=null && content.getUrl().length()>0){
				criteria.andUrlLike("%"+content.getUrl()+"%");
			}
			if(content.getPic()!=null && content.getPic().length()>0){
				criteria.andPicLike("%"+content.getPic()+"%");
			}
			if(content.getStatus()!=null && content.getStatus().length()>0){
				criteria.andStatusLike("%"+content.getStatus()+"%");
			}
	
		}
		
		Page<TbContent> page= (Page<TbContent>)contentMapper.selectByExample(example);		
		return new PageInfo<TbContent>(page.getResult());
	}

	@Override
	public List<TbContent> findByCategoryId(Long categoryId) {
		//第二次查询或者N>=2查询，去redis中查询是否有数据，查询不到再去数据库查询,并存入redis
		Object result = redisTemplate.boundHashOps("content").get(categoryId);
		if (result != null)
		{
			return (List<TbContent>) result;
		}


		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		criteria.andStatusEqualTo("1");

		//排序
		example.setOrderByClause("sort_order asc");

		//第一次查询
		List<TbContent> contents = contentMapper.selectByExample(example);
		if (contents != null && contents.size() > 0)
		{
			redisTemplate.boundHashOps("content").put(categoryId, contents);
		}
		return contents;
	}

}
