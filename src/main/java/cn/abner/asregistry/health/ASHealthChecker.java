package cn.abner.asregistry.health;

import cn.abner.asregistry.model.InstanceMeta;
import cn.abner.asregistry.service.ASRegistryService;
import cn.abner.asregistry.service.RegistryService;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Default implement of HealthChecker
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/13 20:42
 */
@Slf4j
public class ASHealthChecker implements HealthChecker{
    final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    long timeout = 20 * 1000;
    RegistryService registryService;

    public ASHealthChecker(RegistryService registryService) {
        this.registryService = registryService;
    }

    @Override
    public void start() {
        executor.scheduleWithFixedDelay(() -> {
            log.info(" ====> Health Checker is starting...");
            long now = System.currentTimeMillis();
            ASRegistryService.TIMESTAMPS.keySet().forEach(serviceAndInstance -> {
                long timestamp = ASRegistryService.TIMESTAMPS.get(serviceAndInstance);
                if (now - timestamp > timeout) {
                    log.info(" ====> Health checker: {} is down", serviceAndInstance);
                    int index = serviceAndInstance.indexOf("@");
                    String service = serviceAndInstance.substring(0, index);
                    String url = serviceAndInstance.substring(index + 1);
                    InstanceMeta instance = InstanceMeta.from(url);
                    registryService.register(service, instance);
                    ASRegistryService.TIMESTAMPS.remove(serviceAndInstance);
                }
            });
        }, 10, 10, TimeUnit.SECONDS);
    }

    @Override
    public void stop() {
        executor.shutdown();
    }
}
