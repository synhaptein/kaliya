package com.synhaptein.kaliya.core.job;

import com.synhaptein.kaliya.core.KaliyaLogger;
import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.*;
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
        m_jobList = new LinkedBlockingQueue<Job>();
        m_jobListDone = Collections.synchronizedList(new LinkedList<Job>());
        m_server = p_server;
        start();
    }
    
    /**
     * Add a job to the queue of the jobScheduler.
     * @param p_job a job
     */
    public void addJob(Job p_job) {
        try {
            m_jobList.put(p_job);
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
                m_runningJob = m_jobList.take();
                m_runningJob.setStatus(Job.JobStatus.RUNNING);
                m_runningJob.runJob(m_server);
                m_jobListDone.add(m_runningJob);
                m_runningJob.setStatus(Job.JobStatus.FINISHED);
                try {
                    KaliyaLogger.log(m_runningJob.resultsToString());
                }
                catch (Exception e) {}
                KaliyaLogger.log("Job " + m_runningJob.getJobId() + " is finished.");
                m_runningJob = null;
            }
        }
        catch (InterruptedException iex) {}
        KaliyaLogger.logAdmin("Job Scheduler is stopped.");
    }
    
    /**
     * Return the list of all the jobs, waiting, running and finished/failed
     * @return a list of job
     */
    public synchronized List<Job> getJobList() {
        List<Job> jobList = new LinkedList<Job>();
        if(m_runningJob != null){
        	jobList.add(m_runningJob);
        }
        jobList.addAll(m_jobList);
        jobList.addAll(m_jobListDone);
        
        return jobList;
    }

    public synchronized Map<Integer, Job> getJobMap() {
        Map<Integer, Job> mapJob = new HashMap<Integer, Job>();
        if(m_runningJob != null){
        	mapJob.put(m_runningJob.getJobId(), m_runningJob);
        }
        for(Job job : m_jobList) {
            mapJob.put(job.getJobId(), job);
        }
        for(Job job : m_jobListDone) {
            mapJob.put(job.getJobId(), job);
        }

        return mapJob;
    }

    public void stopJobScheduler() {
        if(m_runningJob != null) {
            m_runningJob.stopJob();
        }
        interrupt();
        try {
            join();
        }
        catch (InterruptedException e) {}
    }
    
}
