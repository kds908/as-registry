package cn.abner.asregistry.cluster;

import lombok.*;

/**
 * Registry server
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/16 20:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"url"})
public class Server {
    private String url;
    // 状态
    private boolean status;
    // 主否为主节点
    private boolean leader;
    // 版本号
    private long version;
}
