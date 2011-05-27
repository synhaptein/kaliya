package com.synhaptein.kaliya.modules.md5cracker;

import com.synhaptein.kaliya.core.mapreduce.Pair;

import java.util.Iterator;

/**
 * The wordIterator generate word block for the clients.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class WordIterator implements Iterator<Pair<String, char[]>> {
    private final static char FIRST = '0';
    private final static char LAST = 'z';
    private String m_key;
    private char[] m_charlist = new char[0];
    private boolean m_emptyString = false;

    public WordIterator(String p_key) {
        m_key = p_key;
    }

    public boolean hasNext() {
        return true;
    }

    public Pair<String, char[]> next() {
        if(m_emptyString) {
            incr(m_charlist.length - 1);
        }
        else {
            m_emptyString = true;
        }
        Pair<String, char[]> pair = new Pair<String, char[]>();
        pair.key = m_key;
        pair.value = m_charlist;
        return pair;
    }

    private void incr(int p_index) {
        if(p_index == -1) {
            increaseArray();
        }
        else if(m_charlist[p_index] + 1 > LAST) {
            m_charlist[p_index] = FIRST;
            incr(p_index - 1);
        }
        else {
            ++m_charlist[p_index];
        }
    }

    private void increaseArray() {
        char[] charlist = new char[m_charlist.length + 1];
        for(int i = charlist.length - 1; i != 0; --i) {
            charlist[i] = m_charlist[i-1];
        }
        charlist[0] = FIRST;
        m_charlist = charlist;
    }

    public void remove() {
    }
}
