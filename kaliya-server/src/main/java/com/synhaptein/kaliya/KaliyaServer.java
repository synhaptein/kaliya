package com.synhaptein.kaliya;

import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.KaliyaLogger;
import com.synhaptein.kaliya.core.job.JobScheduler;
import com.synhaptein.kaliya.core.worker.WorkerServer;

/**
 * Get an instance of the Kaliya server.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class KaliyaServer {
    private JobScheduler m_jobScheduler;
    private WorkerServer m_workerServer;

    public KaliyaServer(String p_confPath) {
        Information.initParameter(p_confPath);

        // Initialisation of the services
        m_workerServer = new WorkerServer(Integer.valueOf(Information.getParameter("workerPort")));
        KaliyaLogger.init(m_workerServer);
        m_jobScheduler = new JobScheduler(m_workerServer);
        //new ClientKeepAlive(this.m_workerServer);
    }

    public JobScheduler getJobScheduler() {
        return m_jobScheduler;
    }

    public WorkerServer getWorkerServer() {
        return m_workerServer;
    }

    public void stopServer() {
        m_workerServer.stopServer();
        m_jobScheduler.stopJobScheduler();
    }
}
