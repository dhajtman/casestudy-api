package com.casestudy.api.service;

import com.casestudy.api.CommonBaseTest;
import com.casestudy.api.OrderApplication;
import com.casestudy.api.model.OrderedElastic;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.Assert;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(
        classes = { OrderApplication.class, ElasticServiceTest.TestConfiguration.class })
@Testcontainers
@DirtiesContext
public class ElasticServiceTest extends CommonBaseTest {

    @Container
    private static final ElasticsearchContainer container = new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.17.0")) //
            .withPassword("foobar") //
            .withReuse(true);

    @Configuration
    static class TestConfiguration extends ReactiveElasticsearchConfiguration {
        @Override
        public ClientConfiguration clientConfiguration() {

            Assert.notNull(container, "TestContainer is not initialized!");

            return ClientConfiguration.builder() //
                    .connectedTo(container.getHttpHostAddress()) //
                    .usingSsl(container.createSslContextFromCa()) //
                    .withBasicAuth("elastic", "foobar") //
                    .build();
        }
    }

    @Autowired
    private ElasticService elasticService;

    @Test
    void testSaveAndGetAndDelete() {
        OrderedElastic orderedElastic = OrderedElastic.builder().id("1").product("Product 1").quantity(1).build();

        elasticService.save(orderedElastic);

        OrderedElastic savedOrderedElastic = elasticService.get("1");
        Assert.notNull(savedOrderedElastic, "Saved OrderedElastic is null");
        Assert.isTrue(savedOrderedElastic.getId().equals("1"), "Saved OrderedElastic id is not equal to 1");
        Assert.isTrue(savedOrderedElastic.getProduct().equals("Product 1"), "Saved OrderedElastic productName is not equal to Product 1");
        Assert.isTrue(savedOrderedElastic.getQuantity() == 1, "Saved OrderedElastic quantity is not equal to 1");

        elasticService.delete("1");
        OrderedElastic deletedOrderedElastic = elasticService.get("1");
        Assert.isNull(deletedOrderedElastic, "Deleted OrderedElastic is not null");
    }

    @Test
    void testDeleteIndex() {
        OrderedElastic orderedElastic = OrderedElastic.builder().id("1").product("Product 1").quantity(1).build();

        elasticService.save(orderedElastic);

        elasticService.deleteIndex();

        boolean indexExists = elasticService.existsIndex();
        Assert.isTrue(!indexExists, "Deleted OrderedElastic index exists");
    }
}
