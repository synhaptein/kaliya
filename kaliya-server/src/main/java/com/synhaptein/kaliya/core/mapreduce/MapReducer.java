package com.synhaptein.kaliya.core.mapreduce;

import com.synhaptein.kaliya.core.worker.Worker;
import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * The problem is split across all workers by using a mapreduce pattern.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class MapReducer<Vin, Vint, Vout> extends Thread {
    private WorkerServer m_server;
    private String m_jobName;
    private Iterator<Map.Entry<String, Vin>> m_problemIterator;
    private Map<Worker, Task> m_currentTasks;
    private List<Task> m_idleTasks;
    private Map<String, List<Vint>> m_groupBy;
    private List<Pair<String, Vout>> m_results;
    private boolean m_allMapSent = false;
    private boolean m_allReduceSent = false;
    private Object LOCKMAP = new Object() {};
    private boolean m_mapOnly = false;

    public MapReducer(String p_jobName, WorkerServer p_server, Iterator<Map.Entry<String, Vin>> p_problemIterator) {
        setName("MapReducer");
        m_jobName = p_jobName;
        m_server = p_server;
        m_problemIterator = p_problemIterator;
        m_groupBy = new ConcurrentHashMap<String, List<Vint>>();
        m_results = new CopyOnWriteArrayList<Pair<String, Vout>>();
        m_currentTasks = new ConcurrentHashMap<Worker, Task>();
    }

    public void run() {
        try {
            while(m_problemIterator.hasNext()) {
                Map.Entry<String, Vin> pair = m_problemIterator.next();
                Worker worker = m_server.getIdleWorker();
                Task<Vin> task = new MapTask<Vin>(m_jobName, pair.getKey(), pair.getValue());
                m_currentTasks.put(worker, task);
                synchronized (this) {
                    worker.sendTask(task);
                    if(!m_problemIterator.hasNext()) {
                        m_allMapSent = true;
                        m_allReduceSent = m_mapOnly;
                    }
                }
            }

            if(!m_mapOnly) {
                synchronized (LOCKMAP) {
                    LOCKMAP.wait();
                }

                for(Iterator<Map.Entry<String, List<Vint>>> it = m_groupBy.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, List<Vint>> pair = it.next();
                    Worker worker = m_server.getIdleWorker();
                    Task<List<Vint>> task = new ReduceTask<List<Vint>>(m_jobName, pair.getKey(), pair.getValue());
                    m_currentTasks.put(worker, task);
                    synchronized (this) {
                        worker.sendTask(task);
                        if(!it.hasNext()) {
                            m_allReduceSent = true;
                        }
                    }
                }
            }
        }
        catch (InterruptedException e) {}
        System.out.println("MapReducer is finished.");
    }

    private void addValues(Map<String, List<Vint>> p_groupBy, List<Pair<String, Vint>> p_map) {
        for(Pair<String, Vint> pair : p_map) {
            List<Vint> values = p_groupBy.get(pair.key);
            if(values == null) {
                values = new ArrayList<Vint>();
                p_groupBy.put(pair.key, values);
            }
            values.add(pair.value);
        }
    }

    public void addFinishedMapper(Worker p_worker, List<Pair<String, Vint>> p_map) {
        if(m_mapOnly) {
            for(Pair<String, Vint> pair : p_map) {
                Pair<String, Vout> newPair = new Pair<String, Vout>();
                newPair.key = pair.key;
                newPair.value = (Vout)pair.value;
                m_results.add(newPair);
            }
        }
        else {
            addValues(m_groupBy, p_map);
        }
        m_currentTasks.remove(p_worker);
        m_server.setIdleWorker(p_worker);
    }

    public void addFinishedReducer(Worker p_worker, Pair<String, Vout> p_reduce) {
        m_results.add(p_reduce);
        m_currentTasks.remove(p_worker);
        m_server.setIdleWorker(p_worker);
    }

    public boolean isCurrentTaskRemaining() {
        return m_currentTasks.size() > 0;
    }

    public boolean allMapSent() {
        return m_allMapSent;
    }

    public boolean allReduceSent() {
        return m_allReduceSent;
    }

    public void startReduce() {
        if(!m_mapOnly) {
            synchronized (LOCKMAP) {
                LOCKMAP.notify();
            }
        }
    }

    public void setMapOnly() {
        m_mapOnly = true;
    }

    public List<Pair<String, Vout>> getResults() {
        return m_results;
    }
}
