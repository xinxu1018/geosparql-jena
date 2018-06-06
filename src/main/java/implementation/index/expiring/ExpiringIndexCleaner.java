/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package implementation.index.expiring;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 */
public class ExpiringIndexCleaner extends TimerTask {

    //private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final SortedSet<KeyTimestampPair> tracking = Collections.synchronizedSortedSet(new TreeSet<>());
    private final ConcurrentHashMap<Object, Long> refresh = new ConcurrentHashMap<>();
    private final ExpiringIndex index;
    private long expiryInterval;

    public ExpiringIndexCleaner(ExpiringIndex index, long expiryInterval) {
        this.index = index;
        this.expiryInterval = expiryInterval;
    }

    @Override
    public void run() {

        //LOGGER.info("Run Start - Tracker: {}, Refresh: {}, Index: {}", tracking.size(), refresh.size(), index.size());
        long thresholdTimestamp = System.currentTimeMillis() - expiryInterval;
        boolean isEarlier = true;
        while (isEarlier) {
            if (tracking.isEmpty()) {
                return;
            }

            KeyTimestampPair current = tracking.first();
            isEarlier = current.isEarlier(thresholdTimestamp);
            if (isEarlier) {
                Object key = current.getKey();
                tracking.remove(current);
                if (refresh.containsKey(key)) {
                    //Check whether the refresh is still valid.
                    Long timestamp = refresh.get(key);
                    if (thresholdTimestamp < timestamp) {
                        tracking.add(new KeyTimestampPair(key, timestamp));
                    }
                    refresh.remove(key);
                } else {
                    index.remove(key);
                }

            }
        }
        //LOGGER.info("Run End - Tracker: {}, Refresh: {}, Index: {}", tracking.size(), refresh.size(), index.size());
    }

    public synchronized void refresh(Object key) {
        refresh.put(key, System.currentTimeMillis());
    }

    public synchronized void put(Object key) {
        tracking.add(new KeyTimestampPair(key, System.currentTimeMillis()));
    }

    public synchronized void setExpiryInterval(long expiryInterval) {
        this.expiryInterval = expiryInterval;
    }

    public synchronized void clear() {
        tracking.clear();
    }

}
