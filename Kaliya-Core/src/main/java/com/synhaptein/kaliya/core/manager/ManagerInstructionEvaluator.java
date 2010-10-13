package com.synhaptein.kaliya.core.manager;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.Information;
import com.synhaptein.kaliya.core.InstructionEvaluator;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.job.Job;

import java.util.Map.Entry;


/**
 * Implementation of the instruction evaluation for the management console.
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       core.manager
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class ManagerInstructionEvaluator extends InstructionEvaluator {
    private static final int NUMCLIENT = 0;
    private static final int CLIENTLIST = 1;
    private static final int FRAMEWORKVERSION = 2;
    private static final int JOBLIST = 3;
    private static final int CONFIGPARAMETERS = 4;
    private static final int GETJOBLIST = 5;
    
    private ManagerServer m_server;
    
    /**
     * Construct a new instruction evaluation for the manager client
     * @param p_server manager server
     */
    public ManagerInstructionEvaluator(ManagerServer p_server) {
        super();
        this.m_server = p_server;
        this.constructInstructionMap();
    }
    
    /**
     * Construct a map of equivalence between command string and integer
     */
    private void constructInstructionMap(){
        this.m_instructionMap.put("NUMCLIENT", NUMCLIENT);
        this.m_instructionMap.put("CLIENTLIST", CLIENTLIST);
        this.m_instructionMap.put("FRAMEWORKVERSION", FRAMEWORKVERSION);
        this.m_instructionMap.put("JOBLIST", JOBLIST);
        this.m_instructionMap.put("CONFIGPARAMETERS", CONFIGPARAMETERS);
        this.m_instructionMap.put("GETJOBLIST", GETJOBLIST);
    }
 
    /**
     * Evaluate the message receive from the client
     * @param p_message the message from the client
     * @return the message to send back
     */
    public Message eval(Message p_message) {
        Message output = null;
        switch (this.getCommand(p_message.getCommand())) {
            case NUMCLIENT:
                output = new Message(String.valueOf(this.m_server.getWorkerServer().getClientCount()),
                        Message.Target.UNIQUE);
                break;
            case CLIENTLIST:
                output = new Message(createClientListXML(), Message.Target.UNIQUE);
                break;
            case FRAMEWORKVERSION:
                output = new Message(Information.frameworkVersion(), Message.Target.UNIQUE);
                break;
            case CONFIGPARAMETERS:
                output = new Message(this.createConfigParametersList(), Message.Target.UNIQUE);
                break;
            case GETJOBLIST:
                output = new Message(this.createJobList(), Message.Target.UNIQUE);
                break;
            default:
                try {
                    if(Information.useManagerBuffer()) {
                        this.m_server.getCommunicationBuffer().put(p_message);
                    } else {
                        output = new Message("null", Message.Target.UNIQUE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                break;
        }
        return output;
    }
    
    /**
     * Create a XML answer to send the job list
     * @return XML job list
     */
    private String createJobList() {
        String result = "<jobs>";
        for(Job job : this.m_server.getJobScheduler().getJobList()) {
            result += "<job>";
            result += "<id>" + job.getJobId() + "</id>";
            result += "<job>" + job.toString() + "</job>";
            result += "<status>" + job.getStatus() + "</status>";
            result += "</job>";
        }
        result += "</jobs>";
        return result;
    }
    
    /**
     * Create a XML answer to send the parameter list
     * @return XML parameters list
     */
    private String createConfigParametersList() {
        String result = "<parameters>";
        for(Entry<String, String> param : Information.getParameterMap().entrySet()) {
            result += "<parameter>";
            result += "<key>" + param.getKey() + "</key>"; 
            result += "<value>" + param.getValue() + "</value>"; 
            result += "</parameter>";
        }
        result += "</parameters>";
        return result;
    }
    
    /**
     * Create a XML answer to send the client list
     * @return XML client list
     */
    private String createClientListXML(){
        String result = "<clients>";
        for(Client client : this.m_server.getWorkerServer().getClientList().values()){
            result += "<client>";
            result += "<id>" + client.getIdClient() + "</id>"; 
            result += "<ip>" + client.getIp() + "</ip>"; 
            result += "</client>";
        }
        result = result + "</clients>";
        return result;
    }
}
