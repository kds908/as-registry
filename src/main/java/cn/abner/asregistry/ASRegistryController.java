package cn.abner.asregistry;

import cn.abner.asregistry.cluster.Cluster;
import cn.abner.asregistry.cluster.Server;
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
 * Rest controller for this class
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
    @Autowired
    private Cluster cluster;

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
    public long renew(@RequestParam String service, @RequestBody InstanceMeta instance) {
        log.info("=======> renew {} @ {}", service, instance);
        return registryService.renew(instance, service);
    }

    @RequestMapping("/renews")
    public long renews(@RequestParam String services, @RequestBody InstanceMeta instance) {
        log.info("=======> renews {} @ {}", services, instance);
        return registryService.renew(instance, services.split(","));
    }

    @RequestMapping("/version")
    public Long version(@RequestParam String service, InstanceMeta instance) {
        log.info("=======> version {} @ {}", service, instance);
        return registryService.version(service);
    }

    @RequestMapping("/versions")
    public Map<String, Long> versions(@RequestParam String service, InstanceMeta instance) {
        log.info("=======> versions {} @ {}", service, instance);
        return registryService.getVersions(service);
    }

    @RequestMapping("/info")
    public Server info() {
        log.info("=======> info: {}", cluster.self());
        return cluster.self();
    }

    @RequestMapping("/cluster")
    public List<Server> cluster() {
        log.info("=======> cluster: {}", cluster.getServers());
        return cluster.getServers();
    }

    @RequestMapping("/leader")
    public List<Server> leader() {
        log.info("=======> leader: {}", cluster.leader());
        return cluster.getServers();
    }


    @RequestMapping("/setLeader")
    public List<Server> setLeader() {
        cluster.self().setLeader(true);
        log.info("=======> set leader: {}", cluster.self());
        return cluster.getServers();
    }
}
