package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.etcdconf.MockEtcdServer;

public class EtcdTestEnv {

    static {
        MockEtcdServer.setUpMockServer();
    }
}
