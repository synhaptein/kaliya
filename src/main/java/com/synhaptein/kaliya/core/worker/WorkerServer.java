package com.synhaptein.kaliya.core.worker;

import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.Server;

import java.util.concurrent.Executors;

/**
 * Implementation of the worker server.
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       core.worker
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class WorkerServer extends Server {

    /**
     * Construct a new worker server on a specific port
     * @param p_port port
     */
    public WorkerServer(int p_port) {
        super(p_port);
        this.m_threadPool = Executors.newFixedThreadPool(Integer.valueOf(Information.getParameter("workerThreadPoolSize")));
        try {
            this.m_thread = new Thread(this);
            this.m_thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Add the clients to the thread pool when they connect
     */
    public void run() {
        try {
            while (true) {
                new Handler(this, this.m_threadPool, this.m_serverSocket.accept(), this.m_communicationBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
