package cn.codethink.xiaoming.platform.common;

import cn.codethink.common.util.Maps;
import cn.codethink.common.util.Preconditions;
import cn.codethink.xiaoming.platform.event.ClassRegistration;
import cn.codethink.xiaoming.platform.event.Event;
import cn.codethink.xiaoming.platform.service.Service;
import cn.codethink.xiaoming.platform.util.Locks;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <h1>注册图</h1>
 *
 * <p>注册图是一张有向无环图，用以维护基于 Java 类型的注册关系，如监听器 {@link Event}
 * 和服务 {@link Service}。</p>
 *
 * <p>图中每个节点代表一个类。若存在边 A -> B，说明 B 是 A 的子类。每一个节点还有数据域</p>
 *
 * @param <V> 注册的对象
 */
public class Graph<K, V extends ClassRegistration<K>> {
    
    /**
     * 在读注册图时可以获取该锁。在修改注册图时应该优先获取被修改的节点的读锁，
     */
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Map<Class<? extends K>, Node<? extends K, V>> nodes = new HashMap<>();
    
    /**
     * 获取或添加新的节点
     *
     * @param value 值
     * @return 值所在的节点
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Node<K, V> put(V value) {
        Preconditions.objectNonNull(value, "Value");
        final Class<K> registeredClass = value.getRegisteredClass();
    
        // 两次校验获取已有的节点，若存在则直接操作，否则创建。
        Node<K, V> node = (Node<K, V>) nodes.get(registeredClass);
        if (node == null) {
            synchronized (nodes) {
                node = (Node<K, V>) nodes.get(registeredClass);
                
                if (node == null) {
                    node = new Node<>(this, registeredClass);
    
                    // 获取因此节点创建受到影响的所有节点
                    final List<Node> incomingNodes = new ArrayList<>();
                    final List<Node> outgoingNodes = new ArrayList<>();
                    for (Node existingNode : nodes.values()) {
                        if (existingNode.registeredClass.isAssignableFrom(registeredClass)) {
                            incomingNodes.add(existingNode);
                        } else if (registeredClass.isAssignableFrom(existingNode.registeredClass)) {
                            outgoingNodes.add(existingNode);
                        }
                    }
    
                    // 获取这些节点的写锁，准备加锁
                    final List<Lock> locks = new ArrayList<>(incomingNodes.size() + outgoingNodes.size());
                    for (Node incomingNode : incomingNodes) {
                        locks.add(incomingNode.lock.writeLock());
                    }
                    for (Node outgoingNode : outgoingNodes) {
                        locks.add(outgoingNode.lock.writeLock());
                    }
                    
                    boolean locked = false;
                    
                    // 若获取失败则自旋，自旋最大时长为 TODO: read config
                    long bound = 0;
                    while (System.currentTimeMillis() < bound) {
                        locked = Locks.tryLockAll(locks);
                        if (locked) {
                            break;
                        }
                    }
                    
                    // 若仍未获取锁，则获取整张表的重型锁
                    locked = lock.writeLock().tryLock();
    
                    // 修改节点
                    for (Node incomingNode : incomingNodes) {
                        incomingNode.outgoingNodes.put(registeredClass, node);
                        node.incomingNodes.put(incomingNode.registeredClass, incomingNode);
                    }
                    for (Node outgoingNode : outgoingNodes) {
                        outgoingNode.incomingNodes.put(registeredClass, node);
                        node.outgoingNodes.put(outgoingNode.registeredClass, outgoingNode);
                    }
    
                    nodes.put(registeredClass, node);
                }
            }
        }
        
        return node;
    }
    
    /**
     * 获取一个类的所有父类
     *
     * @param currentClass 当前类
     * @return 该类所有父类
     */
    private Set<Class<?>> getSuperclasses(Class<?> currentClass) {
        if (currentClass == Object.class) {
            return Collections.emptySet();
        }
        
        final Set<Class<?>> superclasses = new HashSet<>();
        addSuperclasses(superclasses, currentClass.getSuperclass());
    
        final Class<?>[] interfaces = currentClass.getInterfaces();
        for (Class<?> implementedInterface : interfaces) {
            addSuperclasses(superclasses, implementedInterface);
        }
        
        return superclasses;
    }
    
    private void addSuperclasses(Set<Class<?>> superclasses, Class<?> currentClass) {
        if (!superclasses.add(currentClass) || currentClass == Object.class) {
            return;
        }
        
        addSuperclasses(superclasses, currentClass.getSuperclass());
    
        final Class<?>[] interfaces = currentClass.getInterfaces();
        for (Class<?> implementedInterface : interfaces) {
            addSuperclasses(superclasses, implementedInterface);
        }
    }
    
    public static class Node<K, V extends ClassRegistration<K>> {
        
        private final ReadWriteLock lock = new ReentrantReadWriteLock();
        
        private final Class<K> registeredClass;
        private final Map<Class<? super K>, Node<? super K, V>> incomingNodes = new HashMap<>();
        private final Map<Class<? extends K>, Node<? extends K, V>> outgoingNodes = new HashMap<>();
        private final Graph<K, V> graph;
        
        private final Map<String, V> registrations = new HashMap<>();
    
        private Node(Graph<K, V> graph, Class<K> registeredClass) {
            Preconditions.objectNonNull(graph, "Graph");
            Preconditions.objectNonNull(registeredClass, "Registered class");
            
            this.registeredClass = registeredClass;
            this.graph = graph;
        }
        
        public Class<K> getRegisteredClass() {
            return registeredClass;
        }
        
        public Graph<K, V> getGraph() {
            return graph;
        }
        
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Set<Node<? super K, V>> getIncomingNodes() {
            return Collections.unmodifiableSet((Set) incomingNodes.values());
        }
        
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Set<Node<? extends K, V>> getOutgoingNodes() {
            return Collections.unmodifiableSet((Set) outgoingNodes.values());
        }
    }
}