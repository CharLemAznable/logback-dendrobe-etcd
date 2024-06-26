package com.github.charlemaznable.logback.dendrobe.etcd;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.OpenRequest;
import com.github.charlemaznable.core.es.EsConfig;
import com.github.charlemaznable.etcdconf.MockEtcdServer;
import com.github.charlemaznable.logback.dendrobe.es.EsClientManager;
import com.github.charlemaznable.logback.dendrobe.es.EsClientManagerListener;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.Map;
import java.util.Properties;

import static com.github.charlemaznable.core.es.EsClientElf.buildElasticsearchClient;
import static com.github.charlemaznable.core.es.EsClientElf.closeElasticsearchApiClient;
import static com.github.charlemaznable.core.es.EsConfigElf.ES_CONFIG_ETCD_NAMESPACE;
import static com.google.common.collect.Lists.newArrayList;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EsAppenderTest implements EtcdUpdaterListener, EsClientManagerListener {

    private static final String CLASS_NAME = EsAppenderTest.class.getName();

    private static final String ELASTICSEARCH_VERSION = "7.17.6";
    private static final DockerImageName ELASTICSEARCH_IMAGE = DockerImageName
            .parse("docker.elastic.co/elasticsearch/elasticsearch")
            .withTag(ELASTICSEARCH_VERSION);

    private static final String ELASTICSEARCH_USERNAME = "elastic";
    private static final String ELASTICSEARCH_PASSWORD = "changeme";

    private static final ElasticsearchContainer elasticsearch
            = new ElasticsearchContainer(ELASTICSEARCH_IMAGE)
            .withPassword(ELASTICSEARCH_PASSWORD);

    private static ElasticsearchClient esClient;

    private static Logger root;
    private static Logger self;

    private boolean updated;
    private boolean configured;

    @SneakyThrows
    @BeforeAll
    public static void beforeAll() {
        elasticsearch.start();

        MockEtcdServer.setUpMockServer();

        val esConfig = new EsConfig();
        esConfig.setUris(newArrayList(elasticsearch.getHttpHostAddress()));
        esConfig.setUsername(ELASTICSEARCH_USERNAME);
        esConfig.setPassword(ELASTICSEARCH_PASSWORD);
        esClient = buildElasticsearchClient(esConfig);

        esClient.indices().create(CreateIndexRequest.of(builder -> builder.index("logback.etcd")));
        esClient.indices().open(OpenRequest.of(builder -> builder.index("logback.etcd")));

        root = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        self = LoggerFactory.getLogger(EsAppenderTest.class);
    }

    @AfterAll
    public static void afterAll() {
        closeElasticsearchApiClient(esClient);
        MockEtcdServer.tearDownMockServer();
        elasticsearch.stop();
    }

    @Test
    public void testEsAppender() {
        EtcdUpdater.addListener(this);
        EsClientManager.addListener(this);

        updated = false;
        configured = false;
        MockEtcdServer.addOrModifyProperty(ES_CONFIG_ETCD_NAMESPACE, "DEFAULT", "" +
                "uris=" + elasticsearch.getHttpHostAddress() + "\n" +
                "username=" + ELASTICSEARCH_USERNAME + "\n" +
                "password=" + ELASTICSEARCH_PASSWORD + "\n");
        MockEtcdServer.addOrModifyProperty("Logback", "test", "" +
                "root[console.level]=info\n" +
                CLASS_NAME + "[appenders]=[es]\n" +
                CLASS_NAME + "[es.level]=info\n" +
                CLASS_NAME + "[es.name]=DEFAULT\n" +
                CLASS_NAME + "[es.index]=logback.etcd\n");
        await().forever().until(() -> updated);
        await().forever().until(() -> configured);

        root.info("root es log {}", "1");
        self.info("self es log {}", "1");
        await().timeout(Duration.ofSeconds(20)).untilAsserted(() ->
                assertSearchContent("self es log 1"));

        EsClientManager.removeListener(this);
        EtcdUpdater.removeListener(this);
    }

    @SuppressWarnings({"unchecked", "SameParameterValue"})
    @SneakyThrows
    private void assertSearchContent(String content) {
        val searchRequest = SearchRequest.of(builder ->
                builder.index("logback.etcd").query(queryBuilder ->
                        queryBuilder.matchPhrase(phrase ->
                                phrase.field("event.message").query(content)
                        )
                )
        );
        val searchResponse = esClient.search(searchRequest, Map.class);
        val searchResponseHits = searchResponse.hits().hits();
        assertTrue(searchResponseHits.size() > 0);
        val responseMap = searchResponseHits.get(0).source();
        assertNotNull(responseMap);
        assertEquals(content, ((Map<String, String>) responseMap.get("event")).get("message"));
    }

    @Override
    public void acceptEtcdValueProperties(Properties properties) {
        updated = true;
    }

    @Override
    public void configuredEsClient(String esName) {
        configured = true;
    }
}
