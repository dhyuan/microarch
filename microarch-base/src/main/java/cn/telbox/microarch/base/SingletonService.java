package cn.telbox.microarch.base;

/**
 * Created by dahui on 9/29/16.
 */
public interface SingletonService {

    /**
     * Only the service instance occupies this 'lock' has the right to run.
     * The underlying system (zookeeper, consul or others) can be different.
     *
     * @return
     */
    String getServiceLockName();

    /**
     *
     */
    void start();

    /**
     * The logic where the service to execute.
     */
    void doService();
}
