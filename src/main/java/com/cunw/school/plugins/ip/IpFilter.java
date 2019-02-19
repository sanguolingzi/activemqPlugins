package com.cunw.school.plugins.ip;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerFilter;
import org.apache.activemq.broker.ConnectionContext;
import org.apache.activemq.broker.ProducerBrokerExchange;
import org.apache.activemq.broker.region.MessageReference;
import org.apache.activemq.command.ConnectionInfo;
import org.apache.activemq.command.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IpFilter extends BrokerFilter {

    List<String> allowedIPAddresses;
    private Log log;
    private String pattern = "(\\d)*\\.(\\d)*\\.(\\d)*\\.(\\d)*";

    public IpFilter(Broker next,List<String> allowedIPAddresses){
        super(next);
        log = LogFactory.getLog(IpFilter.class);
        this.allowedIPAddresses = allowedIPAddresses;
    }

    @Override
    public void send(ProducerBrokerExchange producerExchange, Message messageSend) throws Exception {
        log.info("message:"+messageSend);
        super.send(producerExchange, messageSend);
    }


    @Override
    public void messageConsumed(ConnectionContext context, MessageReference messageReference) {
        String remoteAddress = context.getConnection().getRemoteAddress();
        log.info("messageConsumed:"+messageReference  + "  remoteAddress:"+remoteAddress);
        super.messageConsumed(context, messageReference);
    }

    @Override
    public void addConnection(ConnectionContext context, ConnectionInfo info) throws Exception {
        //remoteAddress 格式如 tcp://127.0.0.1:端口
        String remoteAddress = context.getConnection().getRemoteAddress();
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(remoteAddress);
        boolean find = matcher.find();
        if(find) {
            String ip = matcher.group();
            if (allowedIPAddresses.contains(ip)) {
                log.info(" remoteAddress access :"+remoteAddress);
                super.addConnection(context, info);
            } else {
                log.error(" ip:" + ip + " is valid allowedIPAddresses:"+allowedIPAddresses);
                throw new IllegalAccessException(ip + " is valid");
            }
        }else{
            log.error(" remoteAddress:" + remoteAddress + " is valid allowedIPAddresses:"+allowedIPAddresses);
            throw new IllegalAccessException(remoteAddress + " is valid");
        }
    }
}
