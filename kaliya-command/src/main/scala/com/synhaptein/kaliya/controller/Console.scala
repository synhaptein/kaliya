package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.controllers.Controller
import com.synhaptein.kaliya.listener.KaliyaServerListener
import com.synhaptein.kaliya.core.{KaliyaLogger, Information}
import com.synhaptein.scalator.views.{View, Scalate}
import util.matching.Regex.Match

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
      "messagesConsole" -> KaliyaLogger.getMessages
    )
    view
  }

  def job(idJob:String): View = {
    val view = new View("job.ssp") with Scalate
    view.addObjects(
      "job" -> KaliyaServerListener.kaliyaServer.getJobScheduler.getJobMap.get(idJob.toInt)
    )
    view
  }

  def addJob(): View = {
    val jobtype = context.parameter("jobtype", "")
    jobtype match {
      case "md5cracker" => new View("md5cracker.ssp") with Scalate
      case "reverseIndexer" => new View("reverseIndexer.ssp") with Scalate
    }
  }
}