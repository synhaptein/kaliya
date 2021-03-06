package com.synhaptein.kaliya.core.mapreduce;

import com.synhaptein.kaliya.core.worker.Worker;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Abstract task that is sent to workers.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public abstract class Task<T> {
    public static ObjectMapper mapper = new ObjectMapper();
    protected String m_job;
    protected String m_taskId;
    protected String m_key;
    protected T m_value;

    public Task(String p_job, String p_taskId, String p_key, T p_value) {
        m_job = p_job;
        m_taskId = p_taskId;
        m_key = p_key;
        m_value = p_value;
    }

    protected abstract void setType(MapReduceRequest<T> p_mapReduceRequest);

    public void writeTask(Worker p_worker) {
        MapReduceRequest<T> request = new MapReduceRequest<T>();
        setType(request);
        request.job = m_job;
        request.id = m_taskId;
        request.key = m_key;
        request.value = m_value;

        try {
            p_worker.sendMsg(mapper.writeValueAsString(request));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
