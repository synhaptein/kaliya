package com.synhaptein.kaliya.core.mapreduce;

/**
 * The Map task that shape a request and send it to workers.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class MapTask<T> extends Task<T> {
    public MapTask(String p_jobName, String p_taskIdId, String p_key, T p_value) {
        super(p_jobName, p_taskIdId, p_key, p_value);
    }

    @Override
    protected void setType(MapReduceRequest<T> p_mapReduceRequest) {
        p_mapReduceRequest.type = "MAP";
    }
}
