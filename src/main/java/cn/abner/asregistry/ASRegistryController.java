package cn.abner.asregistry;

import cn.abner.asregistry.model.InstanceMeta;
import cn.abner.asregistry.service.RegistryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Description for this class
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/13 19:49
 */
@Slf4j
@RestController
public class ASRegistryController {
    @Autowired
    private RegistryService registryService;

    @RequestMapping("/register")
    public InstanceMeta register(@RequestParam String service, @RequestBody InstanceMeta instance) {
        log.info("=======> register {} @ {}", service, instance);
        return registryService.register(service, instance);
    }

    @RequestMapping("/unregister")
    public InstanceMeta unregister(@RequestParam String service, @RequestBody InstanceMeta instance) {
        log.info("=======> unregister {} @ {}", service, instance);
        return registryService.unregister(service, instance);
    }

    @RequestMapping("findAll")
    public List<InstanceMeta> findAllInstance(@RequestParam String service) {
        log.info("=====> find all instance, param = {}", service);
        return registryService.getAllInstances(service);
    }

    @RequestMapping("/renew")
    public long renew(String service, InstanceMeta instance) {
        log.info("=======> renew {} @ {}", service, instance);
        return registryService.renew(instance, service);
    }

    @RequestMapping("/version")
    public Long version(@RequestParam String service, InstanceMeta instance) {
        log.info("=======> version {} @ {}", service, instance);
        return registryService.version(service);
    }

    @RequestMapping("/versions")
    public Map<String, Long> versions(@RequestParam String service, InstanceMeta instance) {
        log.info("=======> versions {} @ {}", service, instance);
        return registryService.getVersions(service)
    }
}
