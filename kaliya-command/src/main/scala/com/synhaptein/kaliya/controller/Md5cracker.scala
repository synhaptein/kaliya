package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.controllers.Controller
import com.synhaptein.kaliya.listener.KaliyaServerListener
import com.synhaptein.scalator.views.{Redirection, View}

/**
 * Controller that add a new md5cracker job.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

class Md5cracker extends Controller {
  override def index() = {
    val md5hash = context.parameter("md5hash", "")
    KaliyaServerListener.kaliyaServer.getJobScheduler.addJob(
      new com.synhaptein.kaliya.modules.md5cracker.Md5Cracker(md5hash))
    new View("/console") with Redirection
  }
}