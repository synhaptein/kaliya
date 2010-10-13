package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.views.{Jsp, View}
import com.synhaptein.scalator.controllers.Controller

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
    new View("console.jsp") with Jsp
  }
}