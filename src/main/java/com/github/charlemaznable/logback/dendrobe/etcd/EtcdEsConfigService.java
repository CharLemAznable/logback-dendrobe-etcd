package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.core.es.EsConfigElf;
import com.github.charlemaznable.logback.dendrobe.es.EsConfigService;
import com.github.charlemaznable.logback.dendrobe.impl.DefaultEsConfigService;
import com.google.auto.service.AutoService;

@AutoService(EsConfigService.class)
public final class EtcdEsConfigService extends DefaultEsConfigService {

    @Override
    public String getEsConfigValue(String configKey) {
        return EsConfigElf.getEtcdValue(configKey);
    }
}
