package com.pinyougou.user.controller;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.http.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageInfo<TbUser> findPage(int page, int rows){
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user, String code){
		try {
			Boolean codeRelativity = userService.checkIdentifyingCode(user.getPhone(), code);
			if (!codeRelativity)
			{
				return new Result(false, "验证码不正确!");
			}

			int count = userService.findCountByUserName(user.getUsername());
			if (count > 0)
			{
				return new Result(false, "用户名被占用!");
			}
			count = userService.findCountByPhone(user.getPhone());
			if (count > 0)
			{
				return new Result(false, "手机号被占用!");
			}

			Date now = new Date();
			user.setUpdated(now);
			user.setCreated(now);
			String password = user.getPassword();
			user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)));
			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			userService.update(user);
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
	public TbUser findOne(Long id){
		return userService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param user
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageInfo<TbUser> search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

	@RequestMapping("/createIdentifyingCode")
	public Result createIdentifyingCode(String phone)
	{
		try {
			userService.createIdentifyingCode(phone);
			return new Result(true, "发送验证码成功!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result(false, "发送验证码失败!");
	}

	@RequestMapping("/name")
	public String getUsername()
	{
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
