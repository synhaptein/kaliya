package com.synhaptein.kaliya.modules.md5cracker;

import com.synhaptein.kaliya.core.job.Job;
import com.synhaptein.kaliya.core.mapreduce.MapReducer;

import java.util.Iterator;
import java.util.Map;

/**
 * Demo of a md5cracker algorithm
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class Md5Cracker extends Job<String, String, String> {
    private String m_encodeString;

    /**
     * Construct a new md5cracker with an encode string and a pool of client
     * to crack it.
     * @param p_encodeString encode string
     */
    public Md5Cracker(String p_encodeString) {
        this.m_encodeString = p_encodeString;
    }

    /**
     * Get the description of this job for the job listing on the console page
     * @return the description of the job
     */
    @Override
    public String toString() {
        return this.m_encodeString;
    }

    @Override
    public String getJobName() {
        return "md5Cracker";
    }

    @Override
    public void initMapReducer(MapReducer<String, String, String> p_mapReducer) {
        
    }

    @Override
    public Iterator<Map.Entry<String, String>> getIterator() {
        return null;
    }
}
