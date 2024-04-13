package cn.abner.asregistry.service;

import cn.abner.asregistry.model.InstanceMeta;

import java.util.List;
import java.util.Map;

/**
 * Interface for registry service
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/13 19:26
 */
public interface RegistryService {
    // 最基础的3个方法
    InstanceMeta register(String service, InstanceMeta instance);

    InstanceMeta unregister(String service, InstanceMeta instance);

    List<InstanceMeta> getAllInstances(String service);

    // TODO 添加一些高级功能

    /**
     * 保活
     * @param service
     * @param instance
     * @return
     */
    long renew(InstanceMeta instance, String... service);

    Long version(String service);

    Map<String, Long> getVersions(String... services);

}
