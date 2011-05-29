package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.views.{Scalate, View}
import com.synhaptein.scalator.controllers.Controller
import com.synhaptein.kaliya.listener.KaliyaServerListener
import com.synhaptein.kaliya.core.{KaliyaLogger, Information}

/**
 * Render management console
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

class Console extends Controller {
  override def index() = {
    val view = new View("console.ssp") with Scalate
    view.addObjects(
      "lstJobs" -> KaliyaServerListener.kaliyaServer.getJobScheduler.getJobList,
      "mapParamsConf" -> Information.getParameterMap,
      "mapClients" -> KaliyaServerListener.kaliyaServer.getWorkerServer.getClientList,
      "messagesConsole" -> KaliyaLogger.getMessages)
    view
  }
}