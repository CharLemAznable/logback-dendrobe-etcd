package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.etcdconf.EtcdConfigService;

public class EtcdTestEnv {

    static {
        EtcdConfigService.setUpTestMode();
    }
}
