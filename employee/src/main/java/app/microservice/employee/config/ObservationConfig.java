package app.microservice.employee.config;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.observability.ContextProviderFactory;
import org.springframework.data.mongodb.observability.MongoObservationCommandListener;

/**
 * Configuration class for setting up observation/metrics support.
 * ObservedAspect is used to enable method-level observation and metrics tracking.
 */
@Configuration
public class ObservationConfig {

    /**
     * Bean to register ObservedAspect, enabling method observation and metrics collection.
     *
     * @param registry The observation registry for tracking and exporting metrics
     * @return ObservedAspect object that applies metrics to observed methods
     */
    @Bean
    public ObservedAspect observedAspect(ObservationRegistry registry) {
        return new ObservedAspect(registry);
    }

    @Bean
    public MongoClientSettingsBuilderCustomizer mongoClientSettingsBuilderCustomizer(
            ObservationRegistry observationRegistry) {
        return builder -> builder.contextProvider(ContextProviderFactory.create(observationRegistry))
                .addCommandListener(new MongoObservationCommandListener(observationRegistry));
    }
}
