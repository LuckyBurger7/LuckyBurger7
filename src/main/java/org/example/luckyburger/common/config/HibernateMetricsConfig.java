package org.example.luckyburger.common.config;

import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;
import io.micrometer.core.instrument.binder.jpa.HibernateQueryMetrics;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Prometheus Hibernate Metrics
 */
@Configuration
public class HibernateMetricsConfig {
    @Bean
    public MeterBinder hibernateMetricsBinder(EntityManagerFactory emf) {
        SessionFactoryImplementor sessionFactory = emf.unwrap(SessionFactoryImplementor.class);
        return new HibernateQueryMetrics(sessionFactory, "hibernateQuery", Tags.empty());
    }
}
