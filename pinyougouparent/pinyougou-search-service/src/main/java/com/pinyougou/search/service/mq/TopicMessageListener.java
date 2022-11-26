package com.pinyougou.search.service.mq;

import com.pinyougou.mq.MessageInfo;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.List;

public class TopicMessageListener implements MessageListener {
    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        if (message instanceof ObjectMessage)
        {
            ObjectMessage objectMessage = (ObjectMessage) message;
            try {
                MessageInfo messageInfo = (MessageInfo) objectMessage.getObject();
                if (messageInfo.getMethod() == MessageInfo.METHOD_UPDATE)
                {
                    List<TbItem> items = (List<TbItem>) messageInfo.getContext();
                    itemSearchService.importList(items);
                }
                else if (messageInfo.getMethod() == MessageInfo.METHOD_DELETE)
                {
                    List<Long> ids = (List<Long>) messageInfo.getContext();
                    itemSearchService.deleteByGoodsIds((Long[]) ids.toArray());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
