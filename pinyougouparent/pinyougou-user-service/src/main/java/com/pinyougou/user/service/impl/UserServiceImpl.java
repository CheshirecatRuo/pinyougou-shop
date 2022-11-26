package com.pinyougou.user.service.impl;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.pojo.TbUserExample;
import com.pinyougou.pojo.TbUserExample.Criteria;
import com.pinyougou.user.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;


/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private TbUserMapper userMapper;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination queueDestination;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageInfo<TbUser> findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbUser> page=   (Page<TbUser>) userMapper.selectByExample(null);
		return new PageInfo(page);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbUser user) {
		int count = userMapper.insert(user);
		if (count > 0)
		{
			redisTemplate.boundHashOps("phoneNumber").delete(user.getPhone());
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbUser user){
		userMapper.updateByPrimaryKey(user);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbUser findOne(Long id){
		return userMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			userMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageInfo<TbUser> findPage(TbUser user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if(user!=null){			
						if(user.getUsername()!=null && user.getUsername().length()>0){
				criteria.andUsernameLike("%"+user.getUsername()+"%");
			}
			if(user.getPassword()!=null && user.getPassword().length()>0){
				criteria.andPasswordLike("%"+user.getPassword()+"%");
			}
			if(user.getPhone()!=null && user.getPhone().length()>0){
				criteria.andPhoneLike("%"+user.getPhone()+"%");
			}
			if(user.getEmail()!=null && user.getEmail().length()>0){
				criteria.andEmailLike("%"+user.getEmail()+"%");
			}
			if(user.getSourceType()!=null && user.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+user.getSourceType()+"%");
			}
			if(user.getNickName()!=null && user.getNickName().length()>0){
				criteria.andNickNameLike("%"+user.getNickName()+"%");
			}
			if(user.getName()!=null && user.getName().length()>0){
				criteria.andNameLike("%"+user.getName()+"%");
			}
			if(user.getStatus()!=null && user.getStatus().length()>0){
				criteria.andStatusLike("%"+user.getStatus()+"%");
			}
			if(user.getHeadPic()!=null && user.getHeadPic().length()>0){
				criteria.andHeadPicLike("%"+user.getHeadPic()+"%");
			}
			if(user.getQq()!=null && user.getQq().length()>0){
				criteria.andQqLike("%"+user.getQq()+"%");
			}
			if(user.getIsMobileCheck()!=null && user.getIsMobileCheck().length()>0){
				criteria.andIsMobileCheckLike("%"+user.getIsMobileCheck()+"%");
			}
			if(user.getIsEmailCheck()!=null && user.getIsEmailCheck().length()>0){
				criteria.andIsEmailCheckLike("%"+user.getIsEmailCheck()+"%");
			}
			if(user.getSex()!=null && user.getSex().length()>0){
				criteria.andSexLike("%"+user.getSex()+"%");
			}
	
		}
		
		Page<TbUser> page= (Page<TbUser>)userMapper.selectByExample(example);		
		return new PageInfo(page);
	}

	@Override
	public void createIdentifyingCode(String phone) {
		//生成验证码
		String code = String.valueOf((int)(Math.random() * 999999) + 1);

		//将验证码存入redis
		redisTemplate.boundHashOps("phoneNumber").put(phone, code);

		sendMessage(phone, code);
	}

	@Override
	public Boolean checkIdentifyingCode(String phone, String code) {
		String redisCode = (String) redisTemplate.boundHashOps("phoneNumber").get(phone);
		if (!StringUtils.equals(redisCode, code))
		{
			return false;
		}
		return true;
	}

	@Override
	public int findCountByUserName(String username) {
		Example example = new Example(TbUser.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("username", username);
		return userMapper.selectCountByExample(example);
	}

	@Override
	public int findCountByPhone(String phone) {
		Example example = new Example(TbUser.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("phone", phone);
		return userMapper.selectCountByExample(example);
	}

	@Override
	public TbUser findUserByUsername(String username) {
		Example example = new Example(TbUser.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("username", username);
		return userMapper.selectOneByExample(example);
	}

	private void sendMessage(String phone, String code) {
		//消息发送给ActiveMQ
		jmsTemplate.send(queueDestination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage mapMessage = session.createMapMessage();
				mapMessage.setString("signName", "阿里云短信测试");
				mapMessage.setString("templateCode", "SMS_154950909");
				mapMessage.setString("phoneNumber", phone);
				mapMessage.setString("param", "{'code': '"+ code +"'}");

				return mapMessage;
			}
		});
	}

}
