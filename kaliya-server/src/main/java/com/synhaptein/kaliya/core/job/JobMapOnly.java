package com.synhaptein.kaliya.core.job;

/**
 * Abstraction of a job that only use the map step. To create a new module, extend this class.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public abstract class JobMapOnly<Vin, Vout> extends Job<Vin, Vout, Vout> {
    public JobMapOnly() {
        m_mapOnly = true;
    }
}
