package com.synhaptein.kaliya;

import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.job.JobScheduler;
import com.synhaptein.kaliya.core.worker.WorkerServer;
import com.synhaptein.kaliya.modules.md5cracker.Md5Cracker;

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
        this.m_jobScheduler = new JobScheduler();
        this.m_workerServer = new WorkerServer(Integer.valueOf(Information.getParameter("workerPort")));
        //new ClientKeepAlive(this.m_workerServer);

        // Start the main project
        this.m_jobScheduler.addJob(new Md5Cracker("b1f4f9a523e36fd969f4573e25af4540", this.m_workerServer));
        this.m_jobScheduler.addJob(new Md5Cracker("5bdc53d8f514f4beedb00d3536d86ac2", this.m_workerServer));
        this.m_jobScheduler.addJob(new Md5Cracker("73ae356a65a0fc82b3bcf8504ce7b18b", this.m_workerServer));
    }

    public JobScheduler getJobScheduler() {
        return this.m_jobScheduler;
    }

    public WorkerServer getWorkerServer() {
        return this.m_workerServer;
    }

    public void stopServer() {
        this.m_workerServer.stopServer();
        this.m_jobScheduler.stopJobScheduler();
    }
}
