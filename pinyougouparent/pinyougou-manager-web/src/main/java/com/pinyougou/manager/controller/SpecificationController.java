package com.pinyougou.manager.controller;
import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pinyougou.sellersgoods.service.SpecificationOptionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.sellersgoods.service.SpecificationService;


import com.pinyougou.http.Result;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/specification")
public class SpecificationController {

	@Reference
	private SpecificationService specificationService;
	@Reference
	private SpecificationOptionService specificationOptionService;

	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbSpecification> findAll() {
		return specificationService.findAll();
	}


	/**
	 * 返回全部列表
	 *
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageInfo<TbSpecification> findPage(int page, int rows) {
		return specificationService.findPage(page, rows);
	}

	/**
	 * 增加
	 *
	 * @param specification
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbSpecification specification) {
		try {
			specificationService.add(specification);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}

	/**
	 * 修改
	 *
	 * @param specification
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbSpecification specification) {
		try {
			specificationService.update(specification);
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
	public TbSpecification findOne(Long id) {
		return specificationService.findOne(id);
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
			specificationService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}

	/**
	 * 查询+分页
	 *
	 * @param specification
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageInfo<TbSpecification> search(@RequestBody TbSpecification specification, int page, int rows) {
		return specificationService.findPage(specification, page, rows);
	}

	/**
	 * 查询所有规格信息，封装为select2格式数据:{"id":1,"text":"xxx"}
	 * @return
	 */
	@RequestMapping("/findSpecificationList")
	public List<Map<String, Object>> findSpecificationList()
	{
		return specificationService.findAllForList();
	}
	
}
