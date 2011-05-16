package com.synhaptein.kaliya.modules.reverseIndexer;

import com.synhaptein.kaliya.core.job.Job;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * ****FILL THIS!!!!****.
 * <p/>
 * <p/>
 * scalator : (http://www.synhaptein.com/scalator)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * <p/>
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link http://www.synhaptein.com/scalator scalator project
 * @package ****FILL THIS!!!!****
 * @license http://www.synhaptein.com/scalator/license.html
 * @since scalator 0.1
 */

public class ReverseIndexer extends Job<String , String, String> {

    @Override
    public String toString() {
        return "Reverse index of...";
    }

    @Override
    public String getJobName() {
        return "reverseIndexer";
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
