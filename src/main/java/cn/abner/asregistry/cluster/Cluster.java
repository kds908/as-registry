package cn.abner.asregistry.cluster;

import cn.abner.asregistry.ASRegistryConfigProperties;
import cn.abner.asregistry.http.HttpInvoker;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Registry cluster
 *
 * <p>
 * {@code @author:} Abner Song
 * <p>
 * {@code @date:} 2024/4/16 20:20
 */
@Slf4j
public class Cluster {
    @Value("${server.port}")
    String port;

    String host;

    Server MYSERVER;

    ASRegistryConfigProperties registryConfigProperties;

    final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Getter
    private List<Server> servers;

    public Cluster(ASRegistryConfigProperties registryConfigProperties) {
        this.registryConfigProperties = registryConfigProperties;
    }

    long timeout = 5000;

    public void init() {
        try {
            host = new InetUtils(new InetUtilsProperties())
                    .findFirstNonLoopbackHostInfo()
                    .getIpAddress();
            System.out.println(" ====> find firstNonLoopbackHostInfo: " + host);
        } catch (Exception e) {
            host = "127.0.0.1";
        }

        MYSERVER = new Server("http://" + host + ":" + port, true, false, -1);
        System.out.println(" ====> myserver: " + MYSERVER);

        servers = new ArrayList<>();
        for (String url : registryConfigProperties.getServerList()) {
            if (url.contains("localhost")) {
                url = url.replace("localhost", host);
            }
            if (url.contains("127.0.0.1")) {
                url = url.replace("127.0.0.1", host);
            }
            if (url.equals(MYSERVER.getUrl())) {
                servers.add(MYSERVER);
            } else {
                Server server = Server.builder()
                        .url(url)
                        .status(false)
                        .leader(false)
                        .version(-1L)
                        .build();
                servers.add(server);
            }
        } // TODO ...

        executor.scheduleAtFixedRate(() -> {
            try {
                updateServers();
                electLeader();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, timeout, TimeUnit.MICROSECONDS);
    }

    private void electLeader() {
        List<Server> masters = this.servers.stream()
                .filter(Server::isStatus)
                .filter(Server::isLeader)
                .toList();
        if (masters.isEmpty()) {
            System.out.println(" ====> no leader found");
            elect();
        } else if (masters.size() > 1) {
            System.out.println(" ====> elect for more than one leader: " + servers);
            elect();
        } else {
            System.out.println(" ====> no need to elect leader: " + masters.get(0));
        }
    }

    private void elect() {
        // 1. 各种节点自己选，算法保证大家选的是同一个
        // 2. 外部有一个分布式锁，谁拿到锁，谁是主
        // 3. 分布式一致性算法 ， 如 paxos, raft, 复杂
        Server candidate = null;
        for (Server server : servers) {
            server.setLeader(false);
            if (server.isStatus()) {
                if (candidate == null) {
                    candidate = server;
                } else {
                    if (candidate.hashCode() > server.hashCode()) {
                        candidate = server;
                    }
                }
            }
        }
        if (candidate != null) {
            candidate.setLeader(true);
            System.out.println(" ====> elect for leader: " + candidate);
        } else {
            System.out.println(" ====> elect failed");
        }
    }

    private void updateServers() {
        servers.forEach(server -> {
            try {
                Server serverInfo = HttpInvoker.httpGet(server.getUrl() + "/info", Server.class);
                System.out.println(" ====> health check success: " + serverInfo);
                if (serverInfo != null) {
                    server.setStatus(true);
                    server.setLeader(server.isLeader());
                    server.setVersion(serverInfo.getVersion());
                }
            } catch (Exception e) {
                System.out.println(" ====> updateServers: " + e.getMessage());
                server.setStatus(false);
                server.setLeader(false);
            }
        });

    }

    public Server self() {
        return MYSERVER;
    }

    public Server leader() {
        return this.servers.stream()
                .filter(Server::isStatus)
                .filter(Server::isLeader)
                .findFirst()
                .orElse(null);
    }

}
