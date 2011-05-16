package com.synhaptein.kaliya.core.mapreduce;

import java.util.List;

/**
 * JSON POJO: The response sent back by the client.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class MapReduceResponse<Vint, Vout> {
    public String id;
    public String type;
    public String job;
    public List<Pair<String, Vint>> pairList;
    public Pair<String, Vout> pair;
}
