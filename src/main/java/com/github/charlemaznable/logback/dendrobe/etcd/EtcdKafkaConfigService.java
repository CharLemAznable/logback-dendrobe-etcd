package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.core.kafka.KafkaConfigElf;
import com.github.charlemaznable.logback.dendrobe.impl.DefaultKafkaConfigService;
import com.github.charlemaznable.logback.dendrobe.kafka.KafkaConfigService;
import com.google.auto.service.AutoService;

@AutoService(KafkaConfigService.class)
public final class EtcdKafkaConfigService extends DefaultKafkaConfigService {

    @Override
    public String getKafkaConfigValue(String configKey) {
        return KafkaConfigElf.getEtcdValue(configKey);
    }
}
