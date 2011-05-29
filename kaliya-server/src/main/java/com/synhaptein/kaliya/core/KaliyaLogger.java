package com.synhaptein.kaliya.core;

import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Manage the logs to be able to see them in console and on the administration console in the application.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class KaliyaLogger {
    private static int MAX_LOG = 100;
    private static KaliyaLogger m_logger;
    private WorkerServer m_server;
    private List<String> m_lstMessage = Collections.synchronizedList(new LinkedList<String>());

    private KaliyaLogger(WorkerServer p_server) {
        m_server = p_server;
    }

    public static void init(WorkerServer p_server) {
        m_logger = new KaliyaLogger(p_server);
    }

    public static void log(String p_message) {
        m_logger.m_server.sendAllMsg(p_message);
        logAdmin(p_message);
    }

    public static void logAdmin(String p_message) {
        System.out.println(p_message);
        if(m_logger != null) {
            m_logger.m_lstMessage.add(p_message);
            if(m_logger.m_lstMessage.size() > MAX_LOG) {
                m_logger.m_lstMessage.remove(0);
            }
        }
    }

    public static List<String> getMessages() {
        return m_logger.m_lstMessage;
    }
}

