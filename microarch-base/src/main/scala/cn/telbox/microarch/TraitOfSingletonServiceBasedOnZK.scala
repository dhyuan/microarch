package cn.telbox.microarch

import java.util.concurrent.ConcurrentHashMap

import org.apache.curator.framework.recipes.leader.LeaderLatch
import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.ExponentialBackoffRetry
import org.slf4j.{Logger, LoggerFactory}


/**
  *
  *
  * Created by dahui on 08/11/2016.
  */
trait TraitOfSingletonServiceBasedOnZK[T, R] {
  private val logger: Logger = LoggerFactory.getLogger(TraitOfSingletonServiceBasedOnZK.getClass)

  private val ZOOKEEPER_CONN_TIMEOUT: Integer = 60



  def doSingletonJob(zkConnStr: String, pathOfLeader: String, work: T => R)(param: T): Option[R] =
    if (isLeader(zkConnStr, pathOfLeader)) {
      logger.info("I'm the leader. I do this work. %s", this.toString)
      Option.apply(work(param))
    }else {
      logger.info("I'm NOT leader. I do nothing... %s", this.toString)
      Option.empty
    }


  def isLeader(zkConnStr: String, pathOfLeader: String): Boolean = getLeaderLatch(zkConnStr, pathOfLeader).hasLeadership

  private def getLeaderLatch(zkConnStr: String, pathOfLeader: String): LeaderLatch = {
    val latchKey = zkConnStr + "_" + pathOfLeader
    val latch = TraitOfSingletonServiceBasedOnZK.leaderLatchs.get(latchKey)
    if (latch == null) {
      val newLatch = createLeaderLatch(zkConnStr, pathOfLeader)
      TraitOfSingletonServiceBasedOnZK.leaderLatchs.putIfAbsent(latchKey, newLatch)
    }
    TraitOfSingletonServiceBasedOnZK.leaderLatchs.get(latchKey)
  }

  private def createLeaderLatch(zkConnStr: String, pathOfLeader: String): LeaderLatch = {
    val zkClient = getZkClient(zkConnStr)
    val leaderLatch = new LeaderLatch(zkClient, pathOfLeader)
    leaderLatch.start()
    leaderLatch
  }

  private def getZkClient(zkConnStr: String): CuratorFramework = {
    val client = TraitOfSingletonServiceBasedOnZK.zkClients.get(zkConnStr)
    if (client == null) {
      val newClient = createZkClient(zkConnStr)
      TraitOfSingletonServiceBasedOnZK.zkClients.putIfAbsent(zkConnStr, newClient)
    }
    TraitOfSingletonServiceBasedOnZK.zkClients.get(zkConnStr)
  }

  private def createZkClient(zkConnStr: String): CuratorFramework = {
    val zookeeperClient = CuratorFrameworkFactory.newClient(zkConnStr, new ExponentialBackoffRetry(1000, 5))
    zookeeperClient.start()

    logger.info("===== try to connect to the zookeeper.  %s", zkConnStr)
    zookeeperClient
  }

}

object TraitOfSingletonServiceBasedOnZK {
  val zkClients = new ConcurrentHashMap[String, CuratorFramework]()
  val leaderLatchs = new ConcurrentHashMap[String, LeaderLatch]()
}
