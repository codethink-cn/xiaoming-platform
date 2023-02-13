package cn.codethink.xiaoming.platform.util;

import cn.codethink.common.util.Exceptions;
import cn.codethink.common.util.Preconditions;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <h1>锁工具</h1>
 *
 * @author Chuanwise
 */
public class Locks {
    private Locks() {
        Exceptions.throwUtilClassInitializeException(Locks.class);
    }
    
    private static final int MAX_SPINNING_COUNT = 50;
    
    public static boolean tryLockAllOrSpinning(List<Lock> locks) {
        return tryLockAllOrSpinning(locks, MAX_SPINNING_COUNT);
    }
    
    /**
     * 尝试获取一个锁的集合
     *
     * @param locks            锁
     * @param maxSpinningCount 最大自旋次数
     * @return 是否成功获取锁
     */
    public static boolean tryLockAllOrSpinning(List<Lock> locks, int maxSpinningCount) {
        Preconditions.objectNonNull(locks, "Locks");
        Preconditions.argument(maxSpinningCount >= 0, "Max spinning count should be greater or equals than 0!");
    
        if (tryLockAll(locks)) {
            return true;
        }
        
        int spinningCount = 0;
        while (spinningCount <= maxSpinningCount) {
            if (tryLockAll(locks)) {
                break;
            } else {
                spinningCount++;
            }
        }
        return spinningCount != maxSpinningCount;
        
    }
    
    /**
     * 尝试获取一个锁的集合
     *
     * @param locks 锁
     * @return 如果获取成功，返回 true，否则返回 false 并释放所有已获取的锁
     */
    public static boolean tryLockAll(List<Lock> locks) {
        Preconditions.objectNonNull(locks, "Locks");
    
        final int size = locks.size();
    
        int index = 0;
        try {
            while (index < size) {
                if (locks.get(index).tryLock()) {
                    index++;
                } else {
                    break;
                }
            }
    
            return index == size;
        } finally {
            if (index != size) {
                for (int i = index; i >= 0; i--) {
                    locks.get(i).unlock();
                }
            }
        }
    }
    
    /**
     * 尝试在给定时间内获取一个锁的集合
     *
     * @param locks 锁
     * @param time  时长
     * @param unit  时间单位
     * @return 如果获取成功，返回 true，否则返回 false 并释放所有已获取的锁
     */
    public static boolean tryLockAll(List<Lock> locks, long time, TimeUnit unit) throws InterruptedException {
        Preconditions.objectNonNull(locks, "Locks");
        Preconditions.objectNonNull(unit, "Unit");
        Preconditions.argument(time > 0, "Time should be greater than 0!");
    
        final int size = locks.size();
        final long bound = System.currentTimeMillis() + unit.toMillis(time);
    
        int index = 0;
        try {
            while (index < size) {
                final long remainMilliseconds = bound - System.currentTimeMillis();
                if (remainMilliseconds > 0 && locks.get(index).tryLock(remainMilliseconds, TimeUnit.MILLISECONDS)) {
                    index++;
                } else {
                    break;
                }
            }
    
            return index == size;
        } finally {
            if (index != size) {
                for (int i = index; i >= 0; i--) {
                    locks.get(i).unlock();
                }
            }
        }
    }
    
    /**
     * 释放锁的集合
     *
     * @param locks 锁
     */
    public static void unlockAll(List<Lock> locks) {
        Preconditions.objectNonNull(locks, "Locks");
    
        for (Lock lock : locks) {
            lock.unlock();
        }
    }
    
    /**
     * 尝试在给定时间内获取一个锁的集合
     *
     * @param locks        锁
     * @param milliseconds 毫秒数
     * @return 如果获取成功，返回 true，否则返回 false 并释放所有已获取的锁
     */
    public static boolean tryLockAll(List<Lock> locks, long milliseconds) throws InterruptedException {
        return tryLockAll(locks, milliseconds, TimeUnit.MILLISECONDS);
    }
}