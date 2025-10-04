package com.redbus.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.redbus.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.redbus.repository.elasticsearch")
public class RepositoryConfig {
}
