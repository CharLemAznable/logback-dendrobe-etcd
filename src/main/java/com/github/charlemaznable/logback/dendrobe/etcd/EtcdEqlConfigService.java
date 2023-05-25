package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.eql.etcd.EqlEtcdConfig;
import com.github.charlemaznable.etcdconf.EtcdConfigService;
import com.github.charlemaznable.logback.dendrobe.eql.EqlConfigService;
import com.google.auto.service.AutoService;
import lombok.val;
import org.n3r.eql.config.EqlConfig;

import static com.github.charlemaznable.core.lang.Propertiess.parseStringToProperties;
import static com.github.charlemaznable.core.lang.Propertiess.tryDecrypt;
import static com.github.charlemaznable.eql.etcd.EqlEtcdConfig.EQL_CONFIG_NAMESPACE;

@AutoService(EqlConfigService.class)
public final class EtcdEqlConfigService implements EqlConfigService {

    @Override
    public String getEqlConfigValue(String configKey) {
        return EtcdConfigService.getConfig(EQL_CONFIG_NAMESPACE).getString(configKey, "");
    }

    @Override
    public EqlConfig parseEqlConfig(String configKey, String configValue) {
        val properties = tryDecrypt(parseStringToProperties(configValue), configKey);
        if (properties.isEmpty()) return null;
        return new EqlEtcdConfig(properties, configKey);
    }
}
