package com.synhaptein.kaliya.core.job;

import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The job scheduler contains and manages the job list.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class JobScheduler extends Thread {
    private BlockingQueue<Job> m_jobList;
    private List<Job> m_jobListDone;
    private Job m_runningJob;
    private WorkerServer m_server;
    
    /**
     * Construct a new JobScheduler and start running it.
     */
    public JobScheduler(WorkerServer p_server) {
        super("Kaliya-JobScheduler");
        this.m_jobList = new LinkedBlockingQueue<Job>();
        this.m_jobListDone = Collections.synchronizedList(new LinkedList<Job>());
        m_server = p_server;
        this.start();
    }
    
    /**
     * Add a job to the queue of the jobScheduler.
     * @param p_job a job
     */
    public void addJob(Job p_job) {
        try {
            this.m_jobList.put(p_job);
        } catch (Exception e) {
            e.printStackTrace();
        }
                
    }
    
    /**
     * When the job scheduler is started, it runs in batch the job in the queue
     */
    public void run() {
        try {
            while(!isInterrupted()) {
                m_runningJob = this.m_jobList.take();
                m_runningJob.setStatus(Job.JobStatus.RUNNING);
                m_runningJob.runJob(m_server);
                m_jobListDone.add(this.m_runningJob);
                m_runningJob = null;
            }
        }
        catch (InterruptedException iex) {}
        System.out.println("Job Scheduler is stopped.");
    }
    
    /**
     * Return the list of all the jobs, waiting, running and finished/failed
     * @return a list of job
     */
    public List<Job> getJobList() {
        List<Job> jobList = new LinkedList<Job>();
        if(this.m_runningJob != null){
        	jobList.add(this.m_runningJob);
        }
        jobList.addAll(this.m_jobList);
        jobList.addAll(this.m_jobListDone);
        
        return jobList;
    }

    public void stopJobScheduler() {
        if(this.m_runningJob != null) {
            //this.m_runningJob.stopJob();
        }
        this.interrupt();
    }
    
}
