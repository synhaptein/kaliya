package com.synhaptein.kaliya.core.mapreduce;

import com.synhaptein.kaliya.core.worker.Worker;

/**
 * The Reduce task that shape a request and send it to workers.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class ReduceTask<T> extends Task<T> {
    public ReduceTask(String p_jobName, String p_key, T p_value) {
        super(p_jobName, p_key, p_value);
    }

    @Override
    public void writeTask(Worker p_worker) {
        MapReduceRequest<T> request = new MapReduceRequest<T>();
        request.type = "REDUCE";
        request.job = m_job;
        request.id = "uniqueIdTest";
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
