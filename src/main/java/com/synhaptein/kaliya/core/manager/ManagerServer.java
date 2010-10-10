package com.synhaptein.kaliya.core.manager;

import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.Server;
import com.synhaptein.kaliya.core.job.JobScheduler;
import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.concurrent.Executors;

/**
 * Implementation of the server for the management console.
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       core.manager
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class ManagerServer extends Server {
    
    private WorkerServer m_workerServer;
    private JobScheduler m_jobScheduler;
    
    /**
     * Construct a new manager server on a specific port
     * @param p_jobScheduler job scheduler
     * @param p_workerServer worker server
     * @param port port
     */
    public ManagerServer(JobScheduler p_jobScheduler, WorkerServer p_workerServer, int port) {
        super(port);
        this.m_jobScheduler = p_jobScheduler;
        this.m_workerServer = p_workerServer;
        this.m_threadPool = Executors.newFixedThreadPool(Integer.valueOf(Information.getParameter("managerThreadPoolSize")));
        try {
            this.m_thread = new Thread(this);
            this.m_thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Return the worker server
     * @return worker server
     */
    public WorkerServer getWorkerServer() {
        return this.m_workerServer;
    }
    
    /**
     * Return the job scheduler
     * @return job scheduler
     */
    public JobScheduler getJobScheduler() {
        return this.m_jobScheduler;
    }
    
    /**
     * Add the client to the thread pool
     */
    public void run(){
        try {
        while (true) {
                this.m_threadPool.execute(new Manager(this, m_serverSocket.accept(), this.m_workerServer));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
