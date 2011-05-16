package com.synhaptein.kaliya.core.mapreduce;

import com.synhaptein.kaliya.core.worker.Worker;
import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.*;

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

    public MapReducer(String p_jobName, WorkerServer p_server, Iterator<Map.Entry<String, Vin>> p_problemIterator) {
        setName("MapReducer");
        m_jobName = p_jobName;
        m_server = p_server;
        m_problemIterator = p_problemIterator;
        m_groupBy = Collections.synchronizedMap(new HashMap<String, List<Vint>>());
        m_results = Collections.synchronizedList(new ArrayList<Pair<String, Vout>>());
        m_currentTasks = Collections.synchronizedMap(new HashMap<Worker, Task>());
    }

    public void run() {
        try {
            while(m_problemIterator.hasNext()) {
                Map.Entry<String, Vin> pair = m_problemIterator.next();
                Worker worker = m_server.getIdleWorker();
                Task<Vin> task = new MapTask<Vin>(m_jobName, pair.getKey(), pair.getValue());
                m_currentTasks.put(worker, task);
                worker.sendTask(task);
            }

            m_allMapSent = true;
            synchronized (LOCKMAP) {
                LOCKMAP.wait();
            }

            for(Map.Entry<String, List<Vint>> pair : m_groupBy.entrySet()) {
                Worker worker = m_server.getIdleWorker();
                Task<List<Vint>> task = new ReduceTask<List<Vint>>(m_jobName, pair.getKey(), pair.getValue());
                m_currentTasks.put(worker, task);
                worker.sendTask(task);
            }

            m_allReduceSent = true;
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
        addValues(m_groupBy, p_map);
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
        synchronized (LOCKMAP) {
            LOCKMAP.notify();
        }
    }
}
