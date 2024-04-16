package cn.abner.asregistry;

import cn.abner.asregistry.cluster.Cluster;
import cn.abner.asregistry.health.ASHealthChecker;
import cn.abner.asregistry.health.HealthChecker;
import cn.abner.asregistry.service.ASRegistryService;
import cn.abner.asregistry.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description for this class
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/13 19:53
 */
@Configuration
public class RegistryConfig {

    @Bean
    public RegistryService registryService() {
        return new ASRegistryService();
    }

//    @Bean(initMethod = "start", destroyMethod = "stop")
//    public HealthChecker healthChecker(@Autowired RegistryService registryService) {
//        return new ASHealthChecker(registryService);
//    }

    @Bean(initMethod = "init")
    public Cluster cluster(@Autowired ASRegistryConfigProperties asRegistryConfigProperties) {
        return new Cluster(asRegistryConfigProperties);
    }
}
