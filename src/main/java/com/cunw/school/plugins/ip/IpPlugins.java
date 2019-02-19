package com.cunw.school.plugins.ip;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;

import java.util.List;

public class IpPlugins implements BrokerPlugin{

    List<String> allowedIPAddresses;

    public Broker installPlugin(Broker broker) throws Exception {
            return new IpFilter(broker,allowedIPAddresses);
    }

    public List<String> getAllowedIPAddresses() {
        return allowedIPAddresses;
    }

    public void setAllowedIPAddresses(List<String> allowedIPAddresses) {
        this.allowedIPAddresses = allowedIPAddresses;
    }
}
