package com.synhaptein.kaliya.modules.reverseIndexer;

import com.synhaptein.kaliya.core.job.Job;
import com.synhaptein.kaliya.core.mapreduce.MapReducer;
import com.synhaptein.kaliya.core.mapreduce.Pair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Demo of a reverse index algorithm.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class ReverseIndexer extends Job<String, String, List<String>> {
    private List<Pair<String, String>> m_problem;

    public ReverseIndexer(List<Pair<String, String>> p_problem) {
        m_problem = p_problem;
    }

    @Override
    public String resultsToString() {
        String result = "";
        if(getStatus() == JobStatus.FINISHED) {
            for(Pair<String, List<String>> pair : m_results) {
                result += pair.key + ": ";
                for(Iterator<String> it = pair.value.iterator(); it.hasNext();) {
                    result += " " + it.next();
                    if(it.hasNext()) {
                        result += ",";
                    }
                }
                result += "<br/>";
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "Reverse index of...";
    }

    @Override
    public String getJobName() {
        return "reverseIndexer";
    }

    @Override
    public void initMapReducer(MapReducer<String, String, List<String>> p_mapReducer) {
        //p_mapReducer.setMapOnly();
        //p_mapReducer.setStopOnFirstMap();
    }

    @Override
    public Iterator<Pair<String, String>> getIterator() {
        return m_problem.iterator();
    }
}
