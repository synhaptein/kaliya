package com.synhaptein.kaliya.core.mapreduce;

/**
 * Simple pair for the map response and reduce response.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class Pair<K, V> {
    public K key;
    public V value;
}
