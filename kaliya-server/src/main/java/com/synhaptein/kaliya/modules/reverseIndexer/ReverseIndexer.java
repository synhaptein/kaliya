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
        List<Pair<String, String>> texts = new ArrayList<Pair<String, String>>();
        Pair<String, String> pair = new Pair<String, String>();
        pair.key = "fichier1";
        pair.value = "The quick brown fox jumped over the lazy grey dogs.";
        texts.add(pair);

        pair = new Pair<String, String>();
        pair.key = "fichier2";
        pair.value = "That's one small step for a man, one giant leap for mankind.";
        texts.add(pair);

        pair = new Pair<String, String>();
        pair.key = "fichier3";
        pair.value = "The quick brown fox jumped over the lazy grey dogs.";
        texts.add(pair);

        pair = new Pair<String, String>();
        pair.key = "fichier4";
        pair.value = "Mary had a little lamb, Its fleece was white as snow; And everywhere that Mary went, The lamb was sure to go.";
        texts.add(pair);

        pair = new Pair<String, String>();
        pair.key = "fichier5";
        pair.value = "That's one small step for a man, one giant leap for mankind.";
        texts.add(pair);

        pair = new Pair<String, String>();
        pair.key = "fichier6";
        pair.value = "Mary had a little lamb, Its fleece was white as snow; And everywhere that Mary went, The lamb was sure to go.";
        texts.add(pair);

        return texts.iterator();
    }
}
