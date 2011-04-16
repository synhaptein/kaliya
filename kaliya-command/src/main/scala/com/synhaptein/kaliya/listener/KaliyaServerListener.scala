package com.synhaptein.kaliya.listener

import javax.servlet.{ServletContextEvent, ServletContextListener}
import com.synhaptein.kaliya.KaliyaServer
import com.synhaptein.kaliya.core.Information
import io.Source

/**
 * Start the Kaliya server.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

object KaliyaServerListener {
  var kaliyaServer: KaliyaServer = null
}
class KaliyaServerListener extends ServletContextListener
{
  override def contextDestroyed(event : ServletContextEvent) = {
    println("Stop Kaliya server");
    KaliyaServerListener.kaliyaServer.stopServer;
  }

	override def contextInitialized(event : ServletContextEvent) = {
		println("Start Kaliya server");
    try {
      val path = event.getServletContext().getRealPath("/WEB-INF/KaliyaServerConf.xml")
      KaliyaServerListener.kaliyaServer = new KaliyaServer(path)
      Information.setCrossDomain(Source.fromFile(event.getServletContext().getRealPath(Information.pathToCrossDomain)).mkString)
    }
    catch {
      case e : Exception => e.printStackTrace
    }
	}
}