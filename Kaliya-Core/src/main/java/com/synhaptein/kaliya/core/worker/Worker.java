package com.synhaptein.kaliya.core.worker;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.Server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

/**
 * Implementation of the worker client.
 *
 * Kaliya : (http://www.synhaptein.com/Kaliya)
 * Copyright 2010, SynHaptein (http://www.synhaptein.com)
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/Kaliya Kaliya Project
 * @package       core.worker
 * @since         Kaliya 1.0
 * @license       http://www.synhaptein.com/Kaliya/license.html
 */
public class Worker extends Client {

    private BlockingQueue<Message> m_communicationBuffer;

    /**
     * Construct a new worker
     * @param p_server worker server
     * @param p_socket client socket
     * @param p_communicationBuffer communication buffer
     */
    public Worker(Server p_server, Socket p_socket, BlockingQueue<Message> p_communicationBuffer) {
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
            this.sendMsg("OK!\n");
            while (this.m_readerIn.read(tab, 0, 1) != -1) {
                sReceived += tab[0];
                if (tab[0] == '\0' && sReceived.length() > 1) {
                    sReceived = sReceived.substring(1, sReceived.length() - 1);
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

    
}
