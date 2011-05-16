package com.synhaptein.kaliya.core.mapreduce;

/**
 * JSON POJO: the request that will be send to the client.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class MapReduceRequest<T> {
    public String type;
    public String job;
    public String id;
    public String key;
    public Object value;
}
