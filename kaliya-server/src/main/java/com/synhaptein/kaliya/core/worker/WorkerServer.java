package com.synhaptein.kaliya.core.worker;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.Server;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Implementation of the worker server.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class WorkerServer extends Server {
    private BlockingQueue<Worker> m_lstWorkerIdle = new LinkedBlockingQueue<Worker>();

    /**
     * Construct a new worker server on a specific port
     * @param p_port port
     */
    public WorkerServer(int p_port) {
        super(p_port);
        m_threadPool = Executors.newFixedThreadPool(Integer.valueOf(Information.getParameter("workerThreadPoolSize")));
        start();
    }

    /**
     * Add the clients to the thread pool when they connect
     */
    public void runServer() throws InterruptedException {
            while (!isInterrupted()) {
                try {
                    new Handler(this, m_threadPool, m_serverSocket.accept(), m_communicationBuffer);
                }
                catch (IOException iox) {
                    break;
                }
            }
    }

    public Worker getIdleWorker() throws InterruptedException {
        Worker worker = m_lstWorkerIdle.take();
        worker.setStatus(Worker.Status.WORKING);
        return worker;
    }

    public void setIdleWorker(Worker p_worker) {
        p_worker.setStatus(Worker.Status.IDLE);
        m_lstWorkerIdle.offer(p_worker);
    }

    public void addClient(Client p_worker) {
        super.addClient(p_worker);
        m_lstWorkerIdle.offer((Worker) p_worker);
    }

    public void removeClient(Client p_worker) {
        super.removeClient(p_worker);
        m_lstWorkerIdle.remove(p_worker);
    }
}
