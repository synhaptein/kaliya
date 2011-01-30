package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.views.{View, Scalate}
import com.synhaptein.scalator.controllers.Controller

/**
 * Render the client view
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

class IndexController extends Controller
{
  override def index() = {
    new View("index.ssp") with Scalate
  }
}