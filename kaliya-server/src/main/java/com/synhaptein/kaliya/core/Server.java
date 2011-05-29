package com.synhaptein.kaliya.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The abstraction of the server for workers.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public abstract class Server extends Thread {
    /**
     * The client list connected to this server
     */
    protected Map<String, Client> m_clientList;
    /**
     * Server socket
     */
    protected ServerSocket m_serverSocket;
    /**
     * The communication queue that contains the message from the clients
     */
    protected BlockingQueue<Message> m_communicationBuffer;
    /**
     * The thread pool
     */
    protected ExecutorService m_threadPool;


    /**
     * Construct a new server on a specific port
     * @param p_port server port
     */
    public Server(int p_port) {
        super("Kaliya-Server");
        m_clientList = new LinkedHashMap<String, Client>();
        m_communicationBuffer = new LinkedBlockingQueue<Message>();
        try {
            m_serverSocket = new ServerSocket(p_port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * When an object extends this class, it must implements a run method
     */
    public abstract void runServer() throws InterruptedException;

    public void run() {
        try {
            runServer();
        }
        catch (InterruptedException iex) {
            try {
                m_serverSocket.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        KaliyaLogger.logAdmin("Server is stopped.");
    }
    
    /**
     * Return the list of the client connected to this server
     * @return a client list
     */
    public Map<String, Client> getClientList() {
        return m_clientList;
    }
    
    /**
     * Return the communication buffer that contains the incoming message
     * from the clients
     * @return the communication buffer
     */
    public BlockingQueue<Message> getCommunicationBuffer() {
        return m_communicationBuffer;
    }
    
    /**
     * Return the number of client connected to the server
     * @return client count
     */
    public int getClientCount() {
        return m_clientList.size();
    }
    
    /**
     * Add a client to the client list
     * @param p_client a client
     */
    public void addClient(Client p_client) {
        m_clientList.put(p_client.getIdClient(), p_client);
    }
    
    /**
     * Clear the communication buffer
     */
    public void clearCommunicationBuffer() {
    	m_communicationBuffer.clear();
    }
    
    /**
     * Disconnect a client from the server and remove it from the client list
     * @param p_client a client
     */
    public void removeClient(Client p_client) {
        m_clientList.remove(p_client.getIdClient());
        p_client.closeConnection();
    }

    /**
     * Send a message to all the client connected to the server
     * @param sMsg Raw string message
     */
    public void sendAllMsg(String sMsg) {
        try {
            for (Client c : m_clientList.values()) {
                c.sendMsg(sMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop the server
     */
    public void stopServer() {
        try {
            m_serverSocket.close();
            interrupt();
            join();
            List<Client> clientList = new ArrayList<Client>(m_clientList.values());
            for(Client client : clientList) {
                client.closeConnection();
            }
            m_threadPool.shutdownNow();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
