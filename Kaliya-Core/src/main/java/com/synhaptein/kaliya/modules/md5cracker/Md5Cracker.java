package com.synhaptein.kaliya.modules.md5cracker;

import com.synhaptein.kaliya.core.*;
import com.synhaptein.kaliya.core.job.Job;

/**
 * Core of the MD5 Cracker module.
 *
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       core
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class Md5Cracker extends Job {

    private InstructionEvaluator m_instructionEvaluator;
    private int cmp = 0;
    private String m_encodeString;

    /**
     * Construct a new md5cracker with an encode string and a pool of client
     * to crack it.
     * @param p_encodeString encode string
     * @param p_workerServer worker server
     */
    public Md5Cracker(String p_encodeString, Server p_workerServer) {
        super(p_workerServer);
        this.m_encodeString = p_encodeString;
        this.m_instructionEvaluator = new Md5CrackerInstructionEvaluator(this);
    }
    
    /**
     * When this job is started, it start to crack the encode string with
     * the client pool
     */
    public void run() {
        Message message = null;
        Client client = null;
        Message messageOut = null;
        long startTime = System.currentTimeMillis();
        this.getWorkerServer().clearCommunicationBuffer();
        this.getWorkerServer().sendAllMsg("OK!\0");
        while (true) {
            if (Information.isDebug()) {
                long time = System.currentTimeMillis() - startTime;
                if (time > 5000) {
                    System.out.println(1000 * cmp / ((double) time / 1000) + "/sec");
                    startTime = System.currentTimeMillis();
                    cmp = 0;
                }
                if(message != null && "GETBLOCK".equals(message.getCommand())) {
                    ++cmp;
                }
            }
            try {
                message = this.getCommunicationBuffer().take();
            } catch (Exception e) {
                e.printStackTrace();
            }
            client = message.getClient();
            messageOut = this.m_instructionEvaluator.eval(message);
            if(messageOut != null && !"".equals(messageOut.toString())) {
                if(messageOut.getTarget() == Message.Target.UNIQUE) {
                    client.sendMsg(messageOut.toString() + '\0');
                } else {
                    this.getWorkerServer().sendAllMsg(messageOut.toString() + '\0');
                }
            }
            if (Information.isDebug()) {
                System.out.println(message.getClient().getIdClient() + ": " + message.getCommand());
            }
            if(this.getStatus() == Job.JobStatus.FINISHED) {
                break;
            }
        }
        if(Information.isDebug()) {
            System.out.println("Job " + this.m_encodeString + " is finished.");
        }
    }

    /**
     * Get the description of this job for the job listing on the console page
     * @return the description of the job
     */
    public String toString() {
        return this.m_encodeString;
    }
}
