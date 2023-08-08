package com.example.core.config;

import com.example.core.category.QueryCategoryRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class TestQueryDslConfig {
    @PersistenceContext
    EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public QueryCategoryRepositoryImpl queryCategoryRepository() {
        return new QueryCategoryRepositoryImpl(jpaQueryFactory());
    }
}
