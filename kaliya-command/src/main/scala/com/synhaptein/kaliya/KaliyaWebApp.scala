package com.synhaptein.kaliya

import com.synhaptein.scalator.WebApp

/**
 * Kaliya webApp.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

class KaliyaWebApp extends WebApp
{
  override def bootstrap() = {
    setControllerPackage("com.synhaptein.kaliya.controller")
  }

  override def destroy() = {}
}