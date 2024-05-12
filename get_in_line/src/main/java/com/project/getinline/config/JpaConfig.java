package com.project.getinline.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@EnableJpaAuditing
@Configuration
// JpaAuditing 하고 싶어서 만든 클래스
public class JpaConfig {

    @Bean
    public DataSource dataSource() {
 /*
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.HSQL).build();

        // 1. 직접 해주고 싶을 경우
         return DataSourceBuilder builder = DataSourceBuilder.create()
                .driverClassName()
                .type()
                .url()
                .username()
                .password()
                .build();*/

        // 2. 직접 해줄때 더 간단하게
        // 메소드명 위에 @ConfigurationProperties("spring.dataSource") 추가
        // application.protperties 안에 spring.datasource.url=jdbc:h2:mem:testdb 처럼 설정해둔거 읽음
        return DataSourceBuilder.create().build();
        // 이후 밑에 entityManagerFactory() 안에 DataSource dataSource 넣어서 사용
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.project.getinline");
        factory.setDataSource(dataSource());
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory);
        return txManager;
    }
}
