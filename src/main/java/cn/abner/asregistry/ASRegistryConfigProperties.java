package cn.abner.asregistry;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * registry config properties
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/16 20:25
 */
@Data
@ConfigurationProperties(prefix = "as-registry")
public class ASRegistryConfigProperties {
    private List<String> serverList;
}
