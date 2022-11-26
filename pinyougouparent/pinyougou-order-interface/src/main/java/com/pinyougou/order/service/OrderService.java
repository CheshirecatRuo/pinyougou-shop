package com.pinyougou.order.service;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;

public interface OrderService{

    public int add(TbOrder order);

    public TbPayLog getPayLogByUserId(String username);

    public void updatePayStatus(String username, String transaction_id);
}
