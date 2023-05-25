package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.core.vertx.VertxOptionsConfigElf;
import com.github.charlemaznable.logback.dendrobe.impl.DefaultVertxOptionsService;
import com.github.charlemaznable.logback.dendrobe.vertx.VertxOptionsService;
import com.google.auto.service.AutoService;

@AutoService(VertxOptionsService.class)
public final class EtcdVertxOptionsService extends DefaultVertxOptionsService {

    @Override
    public String getVertxOptionsValue(String configKey) {
        return VertxOptionsConfigElf.getEtcdValue(configKey);
    }
}
