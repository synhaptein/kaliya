package com.synhaptein.kaliya.core.mapreduce;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.InstructionEvaluator;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.worker.WorkerServer;

/**
 * Listen for finish task from the workers.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class MapReducerListener<Vint, Vout> extends Thread {
    private WorkerServer m_server;
    private InstructionEvaluator m_instructionEvaluator;
    private MapReducer<?, Vint, Vout> m_mapReducer;
    
    public MapReducerListener(MapReducer p_mapReducer, WorkerServer p_server) {
        setName("MapReducerListener");
        m_mapReducer = p_mapReducer;
        m_server = p_server;
        m_instructionEvaluator = new MapReducerInstructionEvaluator<Vint, Vout>(p_mapReducer);
    }

    public void run() {
        try {
            while (!this.isInterrupted()) {
                Message message = m_server.getCommunicationBuffer().take();
                Client client = message.getClient();
                Message messageOut = this.m_instructionEvaluator.eval(message);
                if(messageOut != null && !"".equals(messageOut.toString())) {
                    if(messageOut.getTarget() == Message.Target.UNIQUE) {
                        client.sendMsg(messageOut.toString());
                    } else {
                        m_server.sendAllMsg(messageOut.toString());
                    }
                }

                if(!m_mapReducer.allReduceSent() && m_mapReducer.allMapSent() && !m_mapReducer.isCurrentTaskRemaining()) {
                    m_mapReducer.startReduce();
                }
                else if(m_mapReducer.allReduceSent() && !m_mapReducer.isCurrentTaskRemaining()) {
                    break;
                }
            }
        }
        catch (InterruptedException e) {}
        System.out.println("MapReducerListener is finished.");
    }
}
