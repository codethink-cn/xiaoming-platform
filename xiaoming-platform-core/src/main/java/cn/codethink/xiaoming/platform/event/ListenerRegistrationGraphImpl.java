package cn.codethink.xiaoming.platform.event;

import cn.codethink.common.util.Maps;
import cn.codethink.common.util.Preconditions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class ListenerRegistrationGraphImpl
    implements ListenerRegistrationGraph {
    
    private final Map<Class<?>, NodeImpl> nodes = new ConcurrentHashMap<>();
    
    @Override
    public Node get(Class<?> eventClass) {
        Preconditions.objectNonNull(eventClass, "Event class");
        
        return nodes.get(eventClass);
    }
    
    @Override
    public Node getOrDefault(Class<?> eventClass, Node defaultNode) {
        Preconditions.objectNonNull(eventClass, "Event class");
    
        final Node node = nodes.get(eventClass);
        return node == null ? defaultNode : node;
    }
    
    @Override
    public Node remove(Class<?> eventClass) {
        Preconditions.objectNonNull(eventClass, "Event class");
    
        final NodeImpl node = nodes.remove(eventClass);
        if (node == null) {
            return null;
        }
    
        for (NodeImpl incomingNode : node.incomingNodes.values()) {
            // TODO
        }
    
        return node;
    }
    
    @Override
    public Node getOrFail(Class<?> eventClass) {
        return Maps.getOrFail(nodes, eventClass);
    }
    
    /**
     * 获取或添加新的节点
     *
     * @param eventClass 事件类型
     * @return 代表该事件类型的节点
     */
    private Node getOrPut(Class<?> eventClass) {
        NodeImpl node = nodes.get(eventClass);
    
        // TODO
        if (node == null) {
            synchronized (nodes) {
                node = nodes.get(eventClass);
                
                if (node == null) {
                    node = new NodeImpl(this, eventClass);
    
                    for (NodeImpl existingNode : nodes.values()) {
                        if (existingNode.eventClass.isAssignableFrom(eventClass)) {
                            existingNode.outgoingNodes.put(eventClass, node);
                            node.incomingNodes.put(existingNode.eventClass, existingNode);
                        } else if (eventClass.isAssignableFrom(existingNode.eventClass)) {
                            existingNode.incomingNodes.put(eventClass, node);
                            node.outgoingNodes.put(existingNode.eventClass, existingNode);
                        }
                    }
    
                    nodes.put(eventClass, node);
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
    
    public static class NodeImpl
        implements Node {
        
        private final Class<?> eventClass;
        private final Map<Class<?>, NodeImpl> incomingNodes = new ConcurrentHashMap<>();
        private final Map<Class<?>, NodeImpl> outgoingNodes = new ConcurrentHashMap<>();
        private final ListenerRegistrationGraph graph;
    
        private NodeImpl(ListenerRegistrationGraph graph, Class<?> eventClass) {
            Preconditions.objectNonNull(graph, "Graph");
            Preconditions.objectNonNull(eventClass, "Event class");
            
            this.eventClass = eventClass;
            this.graph = graph;
        }
    
        @Override
        public Class<?> getEventClass() {
            return eventClass;
        }
    
        @Override
        public ListenerRegistrationGraph getGraph() {
            return graph;
        }
    
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Set<Node> getIncomingNodes() {
            return Collections.unmodifiableSet((Set) incomingNodes.values());
        }
    
        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        public Set<Node> getOutgoingNodes() {
            return Collections.unmodifiableSet((Set) outgoingNodes.values());
        }
    }
}
