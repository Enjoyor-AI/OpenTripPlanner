package org.opentripplanner.util.monitoring;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This supports the monitoring of various system properties, such as free memory.
 * 
 * Think of it like a logger, except that it can be read from inside the system and it supports
 * tracking max values as well as a list of notes.  The use pattern, when monitoring is expensive,
 * is to check isMonitoring before computing anything.
 * 
 * TODO: allow registering special case monitoring for complex cases like long queries.
 * 
 * @author novalis
 * 
 */
public class MonitoringStore {

    private HashSet<String> monitoring = new HashSet<String>();

    private HashMap<String, Long> longs = new HashMap<String, Long>();

    private ListMultimap<String, String> notes = LinkedListMultimap.create();

    public void addNote(String k, String v) {
        if (!monitoring.contains(k))
            return;
        notes.put(k, v);
    }

    public void clearNotes(String k) {
        notes.removeAll(k);
    }

    public Long getLong(String k) {
        return longs.get(k);
    }

    public List<String> getNotes(String k) {
        return notes.get(k);
    }

    public boolean isMonitoring(String k) {
        return monitoring.contains(k);
    }

    public void monitor(String k) {
        monitoring.add(k);
    }

    public void setLong(String k, long v) {
        if (!monitoring.contains(k))
            return;
        longs.put(k, v);
    }

    public synchronized void setLongMax(String k, long v) {
        if (!monitoring.contains(k))
            return;
        Long old = longs.get(k);
        if (old == null || old < v) {
            longs.put(k, v);
        }
    }

    public void stopMonitoring(String k) {
        monitoring.remove(k);
    }

    public void setMonitoring(String key, boolean on) {
        if (on) {
            monitoring.add(key);
        } else {
            monitoring.remove(key);
        }
    }
}
