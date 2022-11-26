package com.pinyougou.page.service.mq;

import com.pinyougou.mq.MessageInfo;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbItem;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopicMessageListener implements MessageListener {

    @Autowired
    private ItemPageService itemPageService;

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
                    Set<Long> ids = getGoodsIds(items);
                    for (Long id : ids)
                    {
                        itemPageService.buildHtml(id);
                    }

                }
                else if (messageInfo.getMethod() == MessageInfo.METHOD_DELETE)
                {
                    List<Long> ids = (List<Long>) messageInfo.getContext();
                    for (Long id : ids)
                    {
                        itemPageService.deleteHtml(id);
                    }
                }
            } catch (JMSException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Set<Long> getGoodsIds(List<TbItem> items)
    {
        Set<Long> ids = new HashSet<>();

        for (TbItem item : items)
        {
            ids.add(item.getGoodsId());
        }
        return ids;
    }
}
