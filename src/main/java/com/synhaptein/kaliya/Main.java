package com.synhaptein.kaliya;

import com.synhaptein.kaliya.core.ClientKeepAlive;
import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.job.JobScheduler;
import com.synhaptein.kaliya.core.manager.ManagerServer;
import com.synhaptein.kaliya.core.worker.WorkerServer;
import com.synhaptein.kaliya.modules.md5cracker.Md5Cracker;

/**
 * Main class for test purpose.
 *
 * It starts the server and add some jobs.
 *
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       kaliya
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class Main {

    public static void main(String[] args) {
        System.out.println(Information.frameworkVersion());
        
        // Initialisation of the services
        JobScheduler jobScheduler = new JobScheduler();
        WorkerServer workerServer = new WorkerServer(Integer.valueOf(Information.getParameter("workerPort")));
        new ManagerServer(jobScheduler, workerServer,
                Integer.valueOf(Information.getParameter("managerPort")));
        new ClientKeepAlive(workerServer);
        
        // Start the main project
        jobScheduler.addJob(new Md5Cracker("b1f4f9a523e36fd969f4573e25af4540", workerServer));
        jobScheduler.addJob(new Md5Cracker("5bdc53d8f514f4beedb00d3536d86ac2", workerServer));
        jobScheduler.addJob(new Md5Cracker("73ae356a65a0fc82b3bcf8504ce7b18b", workerServer));
    }
}
