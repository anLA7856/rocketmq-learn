package com.anla.rocketmqlearn.sample.filter;

import org.apache.rocketmq.common.filter.FilterContext;
import org.apache.rocketmq.common.filter.MessageFilter;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author luoan
 * @version 1.0
 * @date 2020/6/20 13:27
 **/
public class MessageFilterDemoImpl implements MessageFilter {

    @Override
    public boolean match(MessageExt messageExt, FilterContext filterContext) {
        String property = messageExt.getProperty("SequenceId");
        if (property != null){
            int id = Integer.parseInt(property);
            if (((id%10)==0) && (id >100)){
                return true;
            }
        }
        return false;
    }
}
