package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.views.{Scalate, View}
import com.synhaptein.scalator.controllers.Controller
import com.synhaptein.kaliya.listener.KaliyaServerListener
import com.synhaptein.kaliya.core.Information

/**
 * Render management console
 *
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       kaliya
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */

class ConsoleController extends Controller {
  override def index() = {
    Information.getParameterMap.
    val view = new View("console.ssp") with Scalate
    
    return view
  }
}