package com.fintech.cleanarchitecture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.Executor;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableAsync
public class CleanArchitectureApplication {

    private final ApplicationProperties applicationProperties;

    public CleanArchitectureApplication(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
        validateProperties();
    }

    public static void main(String[] args) {
        SpringApplication.run(CleanArchitectureApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Primary
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(applicationProperties.getAsyncCorePoolSize());
        executor.setMaxPoolSize(applicationProperties.getAsyncMaxPoolSize());
        executor.setQueueCapacity(applicationProperties.getAsyncQueueCapacity());
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

    private void validateProperties() {
        if (applicationProperties.getAsyncCorePoolSize() <= 0) {
            throw new IllegalStateException("async.core-pool-size must be greater than 0");
        }
        if (applicationProperties.getAsyncMaxPoolSize() < applicationProperties.getAsyncCorePoolSize()) {
            throw new IllegalStateException("async.max-pool-size must be greater than or equal to async.core-pool-size");
        }
        if (applicationProperties.getAsyncQueueCapacity() <= 0) {
            throw new IllegalStateException("async.queue-capacity must be greater than 0");
        }
    }
}

record ApplicationProperties(int asyncCorePoolSize, int asyncMaxPoolSize, int asyncQueueCapacity) {
}