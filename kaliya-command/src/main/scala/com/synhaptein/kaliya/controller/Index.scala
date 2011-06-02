package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.views.{View, Scalate}
import com.synhaptein.scalator.controllers.Controller
import com.synhaptein.kaliya.core.Information

/**
 * Render the client view
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

class Index extends Controller
{
  override def index() = {
    val view = new View("index.ssp") with Scalate
    view.addObjects(
      "serverHost" -> context.request.getServerName,
      "serverPort" -> Information.getParameterMap.get("workerPort")
    )
    view
  }
}