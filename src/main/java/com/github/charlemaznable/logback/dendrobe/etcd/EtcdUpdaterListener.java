package com.github.charlemaznable.logback.dendrobe.etcd;

import java.util.Properties;

public interface EtcdUpdaterListener {

    void acceptEtcdValueProperties(Properties properties);
}
