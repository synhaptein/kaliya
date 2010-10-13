package com.synhaptein.kaliya.core.manager;

import com.synhaptein.kaliya.core.Client;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.Server;
import java.net.Socket;

/**
 *
 * This is the implementation of the manager client.
 * The manager client is a remote management console.
 *
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
public class Manager extends Client {

    private ManagerInstructionEvaluator m_instructionEvaluator;

    /**
     * Create a new Manager
     * @param p_server manager server
     * @param p_socket manager socket
     * @param p_workerServer worker server
     */
    public Manager(Server p_server, Socket p_socket, Server p_workerServer) {
        super(p_server, p_socket);
        try {
            this.m_id = "Console" + this.m_server.getClientCount();
            this.m_server.addClient(this);
            this.m_instructionEvaluator = new ManagerInstructionEvaluator((ManagerServer)this.m_server);
            
            System.out.println(this.m_id + " connected");
            this.sendMsg("OK\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Receive messages from the client and put it in the communication buffer
     */
    public void run() {
        try {
            String sReceived = "";
            char[] tab = new char[1];
            while (this.m_readerIn.read(tab, 0, 1) != -1) {
                sReceived += tab[0];
                if (tab[0] == '\n') {
                    //Remove \n at the end
                    sReceived = sReceived.substring(0,sReceived.length()-1);
                    System.out.println(sReceived);
                    //Parse instruction
                    this.evalMessage(new Message(this, sReceived));
                    sReceived = "";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.m_server.removeClient(this);
            System.out.println(this.m_id + " deconnected");
        }
    }
    
    /**
     * Evaluate a message when the client send it
     * @param p_message message from the client
     */
    private void evalMessage(Message p_message) {
        String output = this.m_instructionEvaluator.eval(p_message).toString();
        if (output.length() > 0) {
            p_message.getClient().sendMsg(output + "\n");
        }
    }

}

