package com.pinyougou.user.service;
import java.util.List;

import com.github.pagehelper.PageInfo;
import com.pinyougou.pojo.TbUser;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageInfo<TbUser> findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageInfo<TbUser> findPage(TbUser user, int pageNum,int pageSize);

	/**
	 * 发送验证码
	 * @param phone
	 */
	public void createIdentifyingCode(String phone);

	/**
	 * 验证码校验
	 * @param phone
	 * @param identifyingCode
	 * @return
	 */
	public Boolean checkIdentifyingCode(String phone, String identifyingCode);

	/**
	 * 通过用户名查查找用户数量
	 * @param username
	 * @return
	 */
	public int findCountByUserName(String username);

	/**
	 * 通过手机号查找用户数量
	 * @param phone
	 * @return
	 */
	public int findCountByPhone(String phone);

	/**
	 * 通过用户名查查找用户
	 * @param username
	 * @return
	 */
	public TbUser findUserByUsername(String username);
}
