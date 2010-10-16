package com.synhaptein.kaliya.core.job;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The job scheduler contains and manages the job list.
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       core.job
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class JobScheduler extends Thread {
    private BlockingQueue<Job> m_jobList;
    private List<Job> m_jobListDone;
    private Job m_runningJob;
    
    /**
     * Construct a new JobScheduler and start running it.
     */
    public JobScheduler() {
        this.m_jobList = new LinkedBlockingQueue<Job>();
        this.m_jobListDone = Collections.synchronizedList(new LinkedList<Job>());
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
            while(!this.isInterrupted()) {
                this.m_runningJob = this.m_jobList.take();
                this.m_runningJob.startJob();
                this.m_runningJob.setStatus(Job.JobStatus.RUNNING);
                this.m_runningJob.waitJobEnding();
                this.m_jobListDone.add(this.m_runningJob);
                this.m_runningJob = null;
            }
        }
        catch (InterruptedException iex) {}
        System.out.println("Job Scheduler is stopped.");
    }
    
    /**
     * Return the next job in the queue and remove it from the queue
     * @return the next job
     */
    public Job getNextJob() {
        try {
            return this.m_jobList.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
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
            this.m_runningJob.stopJob();
        }
        this.interrupt();
    }
    
}
