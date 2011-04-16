package com.synhaptein.kaliya.core.job;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.Server;

/**
 * Abstraction of a job. To create a new module, extend this class.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public abstract class Job extends Thread {
    /**
     * An enumeration of the different status that a job can take
     */
    public enum JobStatus {
        /**
         * Initial state
         */
        WAITING,
        /**
         * Stop state
         */
        STOPED, 
        /**
         * Running state
         */
        RUNNING, 
        /**
         * Pause state
         */
        PAUSED, 
        /**
         * Finished state
         */
        FINISHED, 
        /**
         * Failed state
         */
        FAILED;
    }
    
    private JobStatus m_status = JobStatus.WAITING;
    private int m_jobId;
    private BlockingQueue<Message> m_communicationBuffer;
    private volatile Thread m_thread;
    private Map<String, Client> m_clientList;
    private Server m_workerServer;
    
    /**
     * Construct a new job
     * @param p_jobId id of the job
     * @param p_workerServer worker server
     */
    public Job(int p_jobId, Server p_workerServer) {
        super("Kaliya-Job-" + p_jobId);
        this.m_jobId = p_jobId;
        this.init(p_workerServer);
    }
    
    /**
     * Construct a new job for a specific server
     * @param p_workerServer worker server
     */
    public Job(Server p_workerServer) {
        this.m_jobId = JobIdGenerator.getNextId();
        this.init(p_workerServer);
    }
    
    /**
     * Initialized the job
     * @param p_workerServer worker server
     */
    private void init(Server p_workerServer) {
        this.m_workerServer = p_workerServer;
        this.m_communicationBuffer = this.m_workerServer.getCommunicationBuffer();
        this.m_clientList = this.m_workerServer.getClientList();
    }
    
    /**
     * Return the communication buffer
     * @return communication buffer
     */
    protected BlockingQueue<Message> getCommunicationBuffer() { 
        return this.m_communicationBuffer;
    }
    
    /**
     * Return the client list
     * @return client list
     */
    protected Map<String, Client> getClientList() {
        return this.m_clientList;
    }
    
    /**
     * Return the worker server
     * @return worker server
     */
    protected Server getWorkerServer() {
        return this.m_workerServer;
    }
    
    /**
     * Set the status of this job
     * @param p_status job status
     */
    public void setStatus(JobStatus p_status) {
        this.m_status = p_status;
    }
    
    /**
     * When an object extends this class, it must implements this method for the
     * console page that list the job. It will be use to print the job.
     * @return a description of the job
     */
    public abstract String toString();
    
    public void waitJobEnding() throws InterruptedException {
        this.m_thread.join();
    }
    /**
     * When an object extends this class, it must implements this method. It will
     * be run when the job scheduler will launch this job.
     */
    public abstract void runJob() throws InterruptedException;

    public void run() {
        try {
            this.runJob();
        }
        catch (InterruptedException iex) {}
        System.out.println("Current job is stopped.");
    }
    
    /**
     * Start running this job
     */
    public void startJob() {
        this.m_thread = new Thread(this);
        this.m_thread.start();
        this.setStatus(JobStatus.RUNNING);
    }
    
    /**
     * Return this job id
     * @return job id
     */
    public int getJobId() {
        return this.m_jobId;
    }
    
    /**
     * Return this job status
     * @return
     */
    public JobStatus getStatus() {
        return this.m_status;
    }

    /**
     * Stop the current job
     */
    public void stopJob() {
        this.m_thread.interrupt();
    }
}
