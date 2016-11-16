package cn.telbox.microarch.base.tools;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


/**
 *
 * @param <T>
 * @param <R>
 */
public abstract class AbstractSingletonServiceBasedOnZK<T, R> implements ConnectionStateListener {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSingletonServiceBasedOnZK.class);

    // The timeout value for zookeeper connection and leaderLatch acquire.
    // The unit of time is SECOND.
    private static final Integer ZOOKEEPER_CONN_TIMEOUT = 60;

    abstract String zookeeperConnectionString();
    abstract String singletonServicePath();

    private CuratorFramework zookeeperClient;
    private LeaderLatch leaderLatch;

    @PostConstruct
    private void postConstruct() {
        logger.info("post constructor on AbstractSingletonServiceBasedOnZK ....");
        initZookeeperClient();
        initLeaderLatch();
    }

    protected Optional<R> doSingletonWork(Function<T, Optional<R>> work, T value) {
        if (isLeaderService()) {
            System.out.println("leader");
            logger.info("I'm the leader. I do this work. {}", this.toString());
            return work.apply(value);
        }

        logger.info("I'm NOT leader. I do nothing.  {}", this.toString());
        return Optional.empty();
    }

    protected void initZookeeperClient() {
        if (zookeeperClient != null) return;

        try {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);
            zookeeperClient = CuratorFrameworkFactory.newClient(zookeeperConnectionString(), retryPolicy);
            zookeeperClient.start();

            logger.info("===== try to connect to the zookeeper.  {}", zookeeperConnectionString());
            zookeeperClient.blockUntilConnected(ZOOKEEPER_CONN_TIMEOUT, TimeUnit.SECONDS);
            logger.info("===== connected to the zookeeper.  {}", zookeeperConnectionString());

        }catch (Exception e) {
            logger.error("Can NOT communication with the zookeeper well. {}", zookeeperConnectionString());
            e.printStackTrace();
            throw new RuntimeException("Can NOT communication with the zookeeper well. " + zookeeperConnectionString());
        }
    }


    protected CuratorFramework getZookeeperClient() {
        if (zookeeperClient != null) {
            return  zookeeperClient;
        }else {
            logger.error("!!! !!! The zookeeper client has not been initialized.  Try to reconnect ... !!!");

            initZookeeperClient();
            return  zookeeperClient;
        }
    }


    protected void initLeaderLatch() {
        if (leaderLatch != null) return;

        leaderLatch = new LeaderLatch(getZookeeperClient(), singletonServicePath());
        try {
            leaderLatch.start();
            leaderLatch.await(ZOOKEEPER_CONN_TIMEOUT, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Can not connect to the zookeeper.");
        }
        try {
            leaderLatch.getParticipants().forEach(p -> logger.info("scheduler try to acquire the leadership.  zkClientId={}", p.getId()));
        } catch (Exception e) {
            logger.info("Can not lest the participants of the scheduler.");
        }
    }

    protected Boolean isLeaderService() {
        if (leaderLatch == null) initLeaderLatch();
        return leaderLatch.hasLeadership();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        logger.warn("Zookeeper Client ConnectionStateListener:  clientState=%s   connectionState={}", client.getState().name(), newState.name());
    }


}
