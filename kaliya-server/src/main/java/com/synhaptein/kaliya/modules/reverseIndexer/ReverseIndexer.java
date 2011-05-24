package com.synhaptein.kaliya.modules.reverseIndexer;

import com.synhaptein.kaliya.core.job.Job;
import com.synhaptein.kaliya.core.mapreduce.MapReducer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

public class ReverseIndexer extends Job<String, String, String> {

    @Override
    public String toString() {
        return "Reverse index of...";
    }

    @Override
    public String getJobName() {
        return "reverseIndexer";
    }

    @Override
    public void initMapReducer(MapReducer<String, String, String> p_mapReducer) {
        //p_mapReducer.setMapOnly();
        //p_mapReducer.setStopOnFirstMap();
    }

    @Override
    public Iterator<Map.Entry<String, String>> getIterator() {
        Map<String, String> texts = new HashMap<String ,String>();
        texts.put("fichier1", "The quick brown fox jumped over the lazy grey dogs.");
        texts.put("fichier2", "That's one small step for a man, one giant leap for mankind.");
        texts.put("fichier3", "Mary had a little lamb, Its fleece was white as snow; And everywhere that Mary went, The lamb was sure to go.");
        texts.put("fichier4", "The quick brown fox jumped over the lazy grey dogs.");
        texts.put("fichier5", "That's one small step for a man, one giant leap for mankind.");
        texts.put("fichier6", "Mary had a little lamb, Its fleece was white as snow; And everywhere that Mary went, The lamb was sure to go.");

        return texts.entrySet().iterator();
    }
}
