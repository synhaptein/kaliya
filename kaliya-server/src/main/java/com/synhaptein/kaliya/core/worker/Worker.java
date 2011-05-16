package com.synhaptein.kaliya.core.worker;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.mapreduce.Task;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of the worker client.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class Worker extends Client<WorkerServer> {
    public enum Status {
        WORKING,
        IDLE;
    }

    private Status m_status = Status.IDLE;
    private BlockingQueue<Message> m_communicationBuffer;

    /**
     * Construct a new worker
     * @param p_server worker server
     * @param p_socket client socket
     * @param p_communicationBuffer communication buffer
     */
    public Worker(WorkerServer p_server, Socket p_socket, BlockingQueue<Message> p_communicationBuffer) {
        super(p_server, p_socket);
        try {
            this.m_communicationBuffer = p_communicationBuffer;
            this.m_id = "Node" + p_server.getClientCount();
            this.m_server.addClient(this);
            
            System.out.println(this.m_id + " connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive messages from the client and put it in the communication buffer
     */
    public void runClient() throws InterruptedException {
        try {
            String sReceived = "";
            char[] tab = new char[1];
            while (this.m_readerIn.read(tab, 0, 1) != -1) {
                sReceived += tab[0];
                if (tab[0] == '\0' && sReceived.length() > 1) {
                    this.m_communicationBuffer.put(new Message(this, sReceived));
                    System.out.println(this.m_id + ": " + sReceived);
                    sReceived = "";
                } else if (tab[0] == '\0') {
                    sReceived = "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            this.m_server.removeClient(this);
            System.out.println(this.m_id + " deconnected " + this.m_server.getClientCount());
        }
    }

    public Status getStatus() {
        return m_status;
    }

    public void setStatus(Status p_status) {
        m_status = p_status;
    }

    public void sendTask(Task p_task) {
        setStatus(Status.WORKING);
        p_task.writeTask(this);
    }
}