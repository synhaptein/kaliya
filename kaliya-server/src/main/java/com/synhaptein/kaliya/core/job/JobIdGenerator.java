package com.synhaptein.kaliya.core.job;

/**
 * The job id generator.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class JobIdGenerator {
    private static int m_ids = -1;
    
    /**
     * It is a simple implementation of an algorithm to generate unique id for 
     * for a new job
     * @return A unique id to identify a new job
     */
    public static int getNextId() {
        return ++m_ids;
    }
}
