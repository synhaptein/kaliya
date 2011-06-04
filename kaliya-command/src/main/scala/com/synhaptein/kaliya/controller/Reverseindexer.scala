package com.synhaptein.kaliya.controller

import com.synhaptein.scalator.controllers.Controller
import com.synhaptein.kaliya.listener.KaliyaServerListener
import com.synhaptein.scalator.views.{Redirection, View}
import org.codehaus.jackson.`type`.TypeReference
import com.synhaptein.kaliya.core.mapreduce.{Pair, Task}

/**
 * Controller that add a new reverseIndexer job.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

class Reverseindexer extends Controller {
  override def index() = {
    val strlstfiles = context.parameter("lstfiles", "")
    try {
      val lstFiles:java.util.List[Pair[String, String]] =
        Task.mapper.readValue(strlstfiles, new TypeReference[java.util.List[Pair[String, String]]] {})
      if(lstFiles.size() == 0) throw new Exception
      KaliyaServerListener.kaliyaServer.getJobScheduler.addJob(
        new com.synhaptein.kaliya.modules.reverseIndexer.ReverseIndexer(lstFiles))
      new View("/console") with Redirection
    }
    catch {
      case _ => new View("/console/addJob?jobtype=reverseIndexer") with Redirection
    }
  }
}