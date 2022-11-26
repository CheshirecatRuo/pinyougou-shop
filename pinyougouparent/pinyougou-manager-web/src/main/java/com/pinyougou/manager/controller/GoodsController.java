package com.pinyougou.manager.controller;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.http.Result;
import com.pinyougou.manager.service.MessageSender;
import com.pinyougou.mq.MessageInfo;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.sellersgoods.service.GoodsService;

import javax.jms.*;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;

	@Autowired
	private MessageSender messageSender;

	@RequestMapping("/update/status")
	public Result updateStatus(@RequestBody List<Long> ids, String status) {
		int count = 0;
		try {
			count = goodsService.updateStatus(ids, status);
			if (count > 0)
			{
				//商品审核通过，将商品Item增加到索引库
				if(status.equals("1"))
				{
					//根据GoodsIds查询items
					List<TbItem> items = goodsService.getByGoodIds(ids, status);

					//批量导入索引库
//					itemSearchService.importList(items);

					//审核通过，生成静态页
//					for (Long id : ids)
//					{
//						itemPageService.buildHtml(id);
//					}

					//发送消息
					MessageInfo messageInfo = new MessageInfo(MessageInfo.METHOD_UPDATE, items);
					messageSender.sendObjectMessage(messageInfo);

				}
				return new Result(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "审核失败");

	}
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageInfo<TbGoods> findPage(int page, int rows){
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param goods
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbGoods goods){
		try {
			goodsService.add(goods);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbGoods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			int dcount = goodsService.delete(ids);
			if (dcount > 0)
			{
//				itemSearchService.deleteByGoodsIds(ids);
				MessageInfo messageInfo = new MessageInfo(MessageInfo.METHOD_DELETE, ids);

				messageSender.sendObjectMessage(messageInfo);

			}
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param goods
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageInfo<TbGoods> search(@RequestBody TbGoods goods, int page, int rows  ){
		return goodsService.findPage(goods, page, rows);		
	}
	
}
