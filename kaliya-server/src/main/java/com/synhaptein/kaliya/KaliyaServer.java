package com.synhaptein.kaliya;

import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.job.JobScheduler;
import com.synhaptein.kaliya.core.worker.WorkerServer;
import com.synhaptein.kaliya.modules.md5cracker.Md5Cracker;
import com.synhaptein.kaliya.modules.reverseIndexer.ReverseIndexer;

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
        m_jobScheduler = new JobScheduler(m_workerServer);
        //new ClientKeepAlive(this.m_workerServer);

        // Start the main project
        m_jobScheduler.addJob(new ReverseIndexer());
        m_jobScheduler.addJob(new ReverseIndexer());
        m_jobScheduler.addJob(new ReverseIndexer());
        m_jobScheduler.addJob(new Md5Cracker("b1f4f9a523e36fd969f4573e25af4540"));
        m_jobScheduler.addJob(new Md5Cracker("5bdc53d8f514f4beedb00d3536d86ac2"));
        m_jobScheduler.addJob(new Md5Cracker("73ae356a65a0fc82b3bcf8504ce7b18b"));
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
