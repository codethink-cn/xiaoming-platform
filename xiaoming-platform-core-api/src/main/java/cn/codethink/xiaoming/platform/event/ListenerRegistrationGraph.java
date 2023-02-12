package cn.codethink.xiaoming.platform.event;

import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * <h1>监听器注册图</h1>
 *
 * @author Chuanwise
 */
public interface ListenerRegistrationGraph {
    
    interface Node {
    
        /**
         * 获取图
         *
         * @return 图
         */
        ListenerRegistrationGraph getGraph();
    
        /**
         * 获取事件类型
         *
         * @return 事件类型
         */
        Class<?> getEventClass();
    
        /**
         * 获取所有父节点
         *
         * @return 所有父节点
         */
        Set<Node> getIncomingNodes();
    
        /**
         * 获取所有子节点
         *
         * @return 所有子节点
         */
        Set<Node> getOutgoingNodes();
    }
    
    Node get(Class<?> eventClass);
    
    Node getOrFail(Class<?> eventClass);
    
    Node getOrDefault(Class<?> eventClass, Node defaultNode);
    
    Node remove(Class<?> eventClass);
}
