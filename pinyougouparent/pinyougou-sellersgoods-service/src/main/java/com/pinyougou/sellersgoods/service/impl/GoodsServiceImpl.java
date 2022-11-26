package com.pinyougou.sellersgoods.service.impl;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbGoodsExample.Criteria;
import com.pinyougou.sellersgoods.service.GoodsService;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	@Autowired
	private TbItemMapper itemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbGoods> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageInfo<TbGoods>(page);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbGoods goods) {
		goods.setIsDelete("0");
		//增加tb_goods
		goodsMapper.insert(goods);
		//增加tb_goods_desc
		// @GeneratedValue(strategy = GenerationType.IDENTITY) 用于获取主键自增值
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		goodsDesc.setGoodsId(goods.getId());
		goodsDescMapper.insert(goodsDesc);

		List<TbItem> items = buildItems(goods);
		itemMapper.batchInsert(items);


	}

	/**
	 * 增加商品Item
	 * @param goods
	 */
	private void addItems(TbGoods goods) {
		Date now = new Date();

		//如果启用了规格，则批量增加SKU item
		if ("1".equals(goods.getIsEnableSpec())) {

			//增加SKU
			for (TbItem item : goods.getItems()) {
				String title = "";

				//获取goods的名称
				String goodsName = goods.getGoodsName();

				//规格属性 {“机身内存” : "16G", ”网络"： "联通3G"}
				Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
				for (Map.Entry<String, String> entry : specMap.entrySet()) {
					title += " " + entry.getValue().toString();
				}
				item.setTitle(title);

				goodsParameterInit(goods, item, now);

				//增加到数据库
				itemMapper.insertSelective(item);
			}
		}
		else
		{
			TbItem item = new TbItem();

			//获取goods的名称
			String goodsName = goods.getGoodsName();
			item.setTitle(goodsName);

			goodsParameterInit(goods, item, now);

			//价格
			item.setPrice(goods.getPrice());

			//是否启用
			item.setStatus("1");

			//数量
			item.setNum(1);

			//是否是默认商品
			item.setIsDefault("1");
			//增加到数据库
			itemMapper.insertSelective(item);
		}
	}

	/**
	 * 增构造商品Item
	 * @param goods
	 */
	private List<TbItem> buildItems(TbGoods goods) {
		List<TbItem> items = new ArrayList<>();
		Date now = new Date();

		//如果启用了规格，则批量增加SKU item
		if ("1".equals(goods.getIsEnableSpec())) {

			//增加SKU
			for (TbItem item : goods.getItems()) {
				String title = "";

				//获取goods的名称
				String goodsName = goods.getGoodsName();

				//规格属性 {“机身内存” : "16G", ”网络"： "联通3G"}
				Map<String, String> specMap = JSON.parseObject(item.getSpec(), Map.class);
				for (Map.Entry<String, String> entry : specMap.entrySet()) {
					title += " " + entry.getValue().toString();
				}
				item.setTitle(title);

				goodsParameterInit(goods, item, now);
				items.add(item);
			}
		}
		else
		{
			TbItem item = new TbItem();

			//获取goods的名称
			String goodsName = goods.getGoodsName();
			item.setTitle(goodsName);

			goodsParameterInit(goods, item, now);

			//价格
			item.setPrice(goods.getPrice());

			//是否启用
			item.setStatus("1");

			//数量
			item.setNum(1);

			//是否是默认商品
			item.setIsDefault("1");
			items.add(item);
		}

		return items;
	}

	/**
	 * 商品信息初始化
	 * @param goods
	 * @param item
	 * @param now
	 */
	private void goodsParameterInit(TbGoods goods, TbItem item, Date now) {
		//图片
		String itemImages = goods.getGoodsDesc().getItemImages();
		List<Map> imageMap = JSON.parseArray(itemImages, Map.class);
		String imageUrl = imageMap.get(0).get("url").toString();
		item.setImage(imageUrl);

		//分类ID
		item.setCategoryid(goods.getCategory3Id());

		//创建时间、修改时间
		item.setUpdateTime(now);
		item.setCreateTime(now);

		//设置SKU也就是goods的id
		item.setGoodsId(goods.getId());

		//设置sellerid
		item.setSellerId(goods.getSellerId());

		//category
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id());
		item.setCategory(itemCat.getName());

		//brand
		TbBrand brand = brandMapper.selectByPrimaryKey(goods.getBrandId());
		item.setBrand(brand.getName());

		//seller
		TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getSellerId());
		item.setSeller(seller.getName());
	}


	/**
	 * 修改
	 */
	@Override
	public void update(TbGoods goods){
		//修改Goods
		goodsMapper.updateByPrimaryKey(goods);
		//待审核
		goods.setAuditStatus("0");

		//修改GoodsDesc
		goodsDescMapper.updateByPrimaryKeySelective(goods.getGoodsDesc());

		//删除item
		TbItem item = new TbItem();
		item.setGoodsId(goods.getId());
		itemMapper.delete(item);

		//新增item 批量增加
		addItems(goods);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbGoods findOne(Long id){
		TbGoods goods = goodsMapper.selectByPrimaryKey(id);

		//查询goodsDesc
		TbGoodsDescExample example = new TbGoodsDescExample();
		TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(id);
		goods.setGoodsDesc(goodsDesc);

		//List<Item>
		TbItem item = new TbItem();
		item.setGoodsId(id);
		List<TbItem> items = itemMapper.select(item);

		goods.setItems(items);
		return goods;
	}

	/**
	 * 批量删除
	 * @return
	 */
	@Override
	public int delete(Long[] ids) {
//		for(Long id:ids){
//			goodsMapper.deleteByPrimaryKey(id);
//		}

		TbGoodsExample example =new TbGoodsExample();
		Criteria criteria = example.createCriteria();

		criteria.andIdIn(Arrays.asList(ids));

		TbGoods goods = new TbGoods();
		goods.setIsDelete("1");
		goodsMapper.updateByExampleSelective(goods, example);
		return 0;
	}
	
	
		@Override
	public PageInfo<TbGoods> findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		
		if(goods!=null){
			if(StringUtils.isNoneBlank(goods.getSellerId())){
				criteria.andSellerIdEqualTo(goods.getSellerId());
			}
			if(StringUtils.isNoneBlank(goods.getGoodsName())){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(StringUtils.isNoneBlank(goods.getAuditStatus())){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(StringUtils.isNoneBlank(goods.getIsMarketable())){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(StringUtils.isNoneBlank(goods.getCaption())){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(StringUtils.isNoneBlank(goods.getSmallPic())){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(StringUtils.isNoneBlank(goods.getIsEnableSpec())){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(StringUtils.isNoneBlank(goods.getIsDelete())){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}

		criteria.andIsDeleteEqualTo("0");
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageInfo<TbGoods>(page);
	}

	@Override
	public int updateStatus(List<Long> ids, String status) {
		Example example = new Example(TbGoods.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andIn("id", ids);

		TbGoods tbGoods = new TbGoods();
		tbGoods.setAuditStatus(status);
		return goodsMapper.updateByExampleSelective(tbGoods, example);
	}

	@Override
	public List<TbItem> getByGoodIds(List<Long> ids, String status) {
		Example example = new Example(TbItem.class);
		Example.Criteria criteria = example.createCriteria();

		criteria.andIn("goodsId", ids);
		criteria.andEqualTo("status", status);

		return itemMapper.selectByExample(example);
	}

}
