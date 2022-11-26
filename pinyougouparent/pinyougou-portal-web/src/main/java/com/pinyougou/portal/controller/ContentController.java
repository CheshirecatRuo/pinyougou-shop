package com.pinyougou.portal.controller;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.advertise.service.ContentService;
import com.pinyougou.http.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbContent;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/content")
public class ContentController {

	@Reference
	private ContentService contentService;

	/**
	 * 根据分类信息查询广告信息
	 * @param categoryId
	 * @return
	 */
	@RequestMapping("/findByCategoryId.do")
	public List<TbContent> findByCategoryId(Long categoryId)
	{
		return contentService.findByCategoryId(categoryId);
	}
}
