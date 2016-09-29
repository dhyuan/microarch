package cn.telbox.microarch.base;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.io.IOException;

/**
 * This service depends on the zookeeper environment.
 *
 * Reference: http://curator.apache.org/curator-recipes/leader-latch.html
 *
 * Created by dahui on 9/29/16.
 */
public abstract class AbstractSingletonServiceOnZooKeeper implements SingletonService {

    @Override
    public void start() {
        CuratorFramework curatorClient = CuratorFrameworkFactory.newClient("connStr", new ExponentialBackoffRetry(1000, Integer.MAX_VALUE));
        LeaderLatch leaderLatch = new LeaderLatch(curatorClient, getServiceLockName());
        try {
            leaderLatch.start();
            leaderLatch.await();
            if (leaderLatch.hasLeadership()) {
                doService();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                leaderLatch.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
