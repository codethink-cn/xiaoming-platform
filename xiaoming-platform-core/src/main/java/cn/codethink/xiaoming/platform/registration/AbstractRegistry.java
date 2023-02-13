package cn.codethink.xiaoming.platform.registration;

import cn.codethink.common.util.Arrays;
import cn.codethink.xiaoming.platform.event.Event;
import cn.codethink.xiaoming.platform.service.Service;

import java.util.*;
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
 * @param <V> 对象
 */
public abstract class AbstractRegistry<V extends Registration> {
    
    /**
     * 记录，用于快速获取值、类图和顺序图之间节点的对应关系
     *
     * @param <K> 类型
     * @param <V> 值
     */
    protected static class Record<K, V extends Registration> {
    
        private volatile V value;
        private final Class<K> registeredClass;
        private final ClassGraphNode<K, V> classGraphNode;
        private final SortGraphNode<K, V> sortGraphNode;
        
        public Record(Class<K> registeredClass, V value, ClassGraphNode<K, V> classGraphNode, SortGraphNode<K, V> sortGraphNode) {
            this.registeredClass = registeredClass;
            this.classGraphNode = classGraphNode;
            this.sortGraphNode = sortGraphNode;
            this.value = value;
        }
    
        public V getValue() {
            return value;
        }
    
        public void setValue(V value) {
            this.value = value;
        }
    
        public Class<K> getRegisteredClass() {
            return registeredClass;
        }
    
        public ClassGraphNode<K, V> getClassGraphNode() {
            return classGraphNode;
        }
    
        public SortGraphNode<K, V> getSortGraphNode() {
            return sortGraphNode;
        }
    }
    
    /**
     * 整张表的读写锁
     */
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    
    /**
     * 以注册名索引的记录
     */
    protected final Map<String, Record<?, V>> signatureIndexedRecords = new HashMap<>();
    
    /**
     * 以类型索引的记录
     */
    protected final Map<Class<?>, ClassGraphNode<?, V>> classIndexedNodes = new HashMap<>();
    
    protected static class ClassValuePair<K, V extends Registration> {
        private final Class<K> registeredClass;
        private final V value;
        
        public ClassValuePair(Class<K> registeredClass, V value) {
            this.registeredClass = registeredClass;
            this.value = value;
        }
    
        public Class<K> getRegisteredClass() {
            return registeredClass;
        }
    
        public V getValue() {
            return value;
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected <K> List<Record<K,V>> putAll(List<ClassValuePair<K, V>> pairs) {
        Record<K, V> record;
    
        final int size = pairs.size();
        final Record<K, V>[] results = new Record[size];
        
        // 先查找重名节点
        try {
            lock.readLock().lock();
    
            for (int i = 0; i < size; i++) {
                final ClassValuePair<K, V> pair = pairs.get(i);
                record = (Record<K, V>) signatureIndexedRecords.get(pair.value.getSignature());
                if (record != null) {
                    record.value = pair.value;
                    results[i] = record;
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        
        try {
            lock.writeLock().lock();
    
            for (int i = 0; i < size; i++) {
                if (results[i] != null) {
                    continue;
                }
    
                final ClassValuePair<K, V> pair = pairs.get(i);
                final Class<K> registeredClass = pair.registeredClass;
                final V value = pair.value;
                final String signature = pair.value.getSignature();
    
                // 获取同类型已有的监听器
                ClassGraphNode<K, V> classGraphNode = (ClassGraphNode<K, V>) classIndexedNodes.get(registeredClass);
                if (classGraphNode == null) {
                    classGraphNode = new ClassGraphNode<>(registeredClass);
                    classIndexedNodes.put(registeredClass, classGraphNode);
        
                    // 将记录添加进类表中
                    for (ClassGraphNode existingClassGraphNode : classIndexedNodes.values()) {
                        if (existingClassGraphNode.registeredClass.isAssignableFrom(registeredClass)) {
                            existingClassGraphNode.outgoingNodes.put(registeredClass, classGraphNode);
                            classGraphNode.incomingNodes.put(existingClassGraphNode.registeredClass, existingClassGraphNode);
                        } else if (registeredClass.isAssignableFrom(existingClassGraphNode.registeredClass)) {
                            existingClassGraphNode.incomingNodes.put(registeredClass, classGraphNode);
                            classGraphNode.outgoingNodes.put(existingClassGraphNode.registeredClass, existingClassGraphNode);
                        }
                    }
                }
    
                // 创建依赖关系节点
                final SortGraphNode<K, V> sortGraphNode = new SortGraphNode<>(registeredClass, value);
                record = new Record<>(registeredClass, value, classGraphNode, sortGraphNode);
                signatureIndexedRecords.put(signature, record);
    
                // 将记录添加进依赖关系表并调整依赖关系
                addAndAdjust(record);
            }
            
            return Arrays.unmodifiableList(results);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    protected abstract <K> void addAndAdjust(Record<K, V> record);
    
    protected static class ClassGraphNode<K, V extends Registration> {
        
        private final Map<Class<? super K>, ClassGraphNode<? super K, V>> incomingNodes = new HashMap<>();
        private final Map<Class<? extends K>, ClassGraphNode<? extends K, V>> outgoingNodes = new HashMap<>();
    
        private final Map<String, Record<K, V>> records = new HashMap<>();
        private final Class<K> registeredClass;
    
        private ClassGraphNode(Class<K> registeredClass) {
            this.registeredClass = registeredClass;
        }
    
        public Map<Class<? super K>, ClassGraphNode<? super K, V>> getIncomingNodes() {
            return incomingNodes;
        }
    
        public Map<Class<? extends K>, ClassGraphNode<? extends K, V>> getOutgoingNodes() {
            return outgoingNodes;
        }
    
        public Map<String, Record<K, V>> getRecords() {
            return records;
        }
    
        public Class<K> getRegisteredClass() {
            return registeredClass;
        }
    }
    
    protected static class SortGraphNode<K, V extends Registration> {
    
        private final Class<K> registeredClass;
        private final V value;
        
        private final Set<SortGraphNode<? super K, V>> incomingNodes = new HashSet<>();
        private final Set<SortGraphNode<? super K, V>> outgoingNodes = new HashSet<>();
        
        public SortGraphNode(Class<K> registeredClass, V value) {
            this.registeredClass = registeredClass;
            this.value = value;
        }
    
        public Class<K> getRegisteredClass() {
            return registeredClass;
        }
    
        public V getValue() {
            return value;
        }
    
        public Set<SortGraphNode<? super K, V>> getIncomingNodes() {
            return incomingNodes;
        }
    
        public Set<SortGraphNode<? super K, V>> getOutgoingNodes() {
            return outgoingNodes;
        }
    }
}