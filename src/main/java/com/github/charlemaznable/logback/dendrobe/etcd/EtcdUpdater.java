package com.github.charlemaznable.logback.dendrobe.etcd;

import com.github.charlemaznable.etcdconf.EtcdConfigService;
import com.github.charlemaznable.logback.dendrobe.HotUpdater;
import com.github.charlemaznable.logback.dendrobe.LogbackDendrobeListener;
import com.google.auto.service.AutoService;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;
import lombok.val;
import org.slf4j.helpers.Util;

import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.charlemaznable.core.lang.Propertiess.parseStringToProperties;
import static java.util.concurrent.Executors.newFixedThreadPool;

@AutoService(HotUpdater.class)
public final class EtcdUpdater implements HotUpdater {

    private static final CopyOnWriteArrayList<EtcdUpdaterListener> listeners = new CopyOnWriteArrayList<>();
    private static final AsyncEventBus notifyBus;

    private static final String ETCD_NAMESPACE_KEY = "logback.etcd.namespace";
    private static final String ETCD_KEY_KEY = "logback.etcd.key";

    private static final String DEFAULT_ETCD_NAMESPACE = "Logback";
    private static final String DEFAULT_ETCD_KEY = "default";

    private LogbackDendrobeListener dendrobeListener;

    static {
        notifyBus = new AsyncEventBus(EtcdUpdaterListener.class.getName(), newFixedThreadPool(1));
        notifyBus.register(new Object() {
            @Subscribe
            public void notifyListeners(Properties properties) {
                for (val listener : listeners) {
                    try {
                        listener.acceptEtcdValueProperties(properties);
                    } catch (Exception t) {
                        Util.report("listener error:", t);
                    }
                }
            }
        });
    }

    public static void addListener(EtcdUpdaterListener listener) {
        listeners.add(listener);
    }

    public static void removeListener(EtcdUpdaterListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void initialize(LogbackDendrobeListener listener, Properties config) {
        this.dendrobeListener = listener;

        // 本地配置etcd配置坐标
        val namespace = config.getProperty(ETCD_NAMESPACE_KEY, DEFAULT_ETCD_NAMESPACE);
        val key = config.getProperty(ETCD_KEY_KEY, DEFAULT_ETCD_KEY);

        new Thread(() -> {
            // etcd配置覆盖默认配置
            val etcdConfig = EtcdConfigService.getConfig(namespace);
            accept(etcdConfig.getString(key, ""));

            etcdConfig.addChangeListener(key, e -> accept(e.getValue()));
        }).start();
    }

    public void accept(String propertyValue) {
        val properties = parseStringToProperties(propertyValue);
        this.dendrobeListener.reset(properties);
        notifyBus.post(properties);
    }
}
