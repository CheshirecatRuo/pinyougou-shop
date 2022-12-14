package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.pinyougou.http.Result;
import com.pinyougou.pojo.TbTypeTemplate;
import com.pinyougou.sellersgoods.service.TypeTemplateService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;

	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbTypeTemplate> findAll() {
		return typeTemplateService.findAll();
	}


	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageInfo<TbTypeTemplate> findPage(int page, int rows) {
		return typeTemplateService.findPage(page, rows);
	}

	/**
	 * 增加
	 *
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbTypeTemplate typeTemplate) {
		try {
			typeTemplateService.add(typeTemplate);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 *
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbTypeTemplate typeTemplate) {
		try {
			typeTemplateService.update(typeTemplate);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}

	/**
	 * 获取实体
	 *
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbTypeTemplate findOne(Long id) {
		return typeTemplateService.findOne(id);
	}

	/**
	 * 批量删除
	 *
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids) {
		try {
			typeTemplateService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 *
	 * @param typeTemplate
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageInfo<TbTypeTemplate> search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows) {
		return typeTemplateService.findPage(typeTemplate, page, rows);
	}

	/**
	 * 返回所有类型，封装for select2,格式：{"id":1,"text":"xxx"}
	 *
	 * @return
	 */
	@RequestMapping("/findTypeTemplateList")
	public List<Map<String, Object>> findTypeTemplateList() {
		return typeTemplateService.findAllForList();
	}


	//根据模板ID查询规格选项信息
	@RequestMapping("/options/{id}")
	public List<Map> findOptionsByTypeId(@PathVariable(value = "id")Long id)
	{
		return typeTemplateService.findOptionsByTypeId(id);
	}
}
