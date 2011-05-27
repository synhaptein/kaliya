package com.synhaptein.kaliya.core.job;

import com.synhaptein.kaliya.core.mapreduce.MapReducer;
import com.synhaptein.kaliya.core.mapreduce.MapReducerListener;
import com.synhaptein.kaliya.core.mapreduce.Pair;
import com.synhaptein.kaliya.core.worker.WorkerServer;

import java.util.Iterator;
import java.util.List;

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

public abstract class Job<Vin, Vint, Vout> extends Thread {
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
    private List<Pair<String, Vout>> m_results;
    protected boolean m_mapOnly = false;
    
    /**
     * Construct a new job
     * @param p_jobId id of the job
     */
    public Job(int p_jobId) {
        this.m_jobId = p_jobId;
    }
    
    /**
     * Construct a new job for a specific server
     */
    public Job() {
        this.m_jobId = JobIdGenerator.getNextId();
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
    
    public abstract String getJobName();

    public abstract void initMapReducer(MapReducer<Vin, Vint, Vout> p_mapReducer);

    public void runJob(WorkerServer p_server) throws InterruptedException {
        MapReducer<Vin, Vint, Vout> mapReducer =
                new MapReducer<Vin, Vint, Vout>(getJobName(), String.valueOf(m_jobId), p_server, getIterator());
        if(m_mapOnly) {
            mapReducer.setMapOnly();
        }
        initMapReducer(mapReducer);
        MapReducerListener<Vint, Vout> mapReducerListener = new MapReducerListener<Vint, Vout>(mapReducer, p_server);
        mapReducerListener.start();
        mapReducer.start();
        try {
            mapReducer.join();
            mapReducerListener.join();
        }
        catch (InterruptedException e) {}
        setResults(mapReducer.getResults());
        System.out.println("Job " + m_jobId + " is finished.");
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

    public abstract Iterator<Pair<String, Vin>> getIterator();

    public void setResults(List<Pair<String, Vout>> p_results) {
        m_results = p_results;
    }

    public List<Pair<String, Vout>> getResults() {
        return m_results;
    }
}
