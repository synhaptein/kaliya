package com.synhaptein.kaliya.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The abstraction of the server for worker, manager... 
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
     * Server thread
     */
    protected volatile Thread m_thread;
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
        this.m_clientList = new LinkedHashMap<String, Client>();
        this.m_communicationBuffer = new LinkedBlockingQueue<Message>();
        try {
            this.m_serverSocket = new ServerSocket(p_port);
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
                this.m_serverSocket.close();
            }
            catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        System.out.println("Server is stopped.");
    }
    
    /**
     * Return the list of the client connected to this server
     * @return a client list
     */
    public Map<String, Client> getClientList() {
        return this.m_clientList;
    }
    
    /**
     * Return the communication buffer that contains the incoming message
     * from the clients
     * @return the communication buffer
     */
    public BlockingQueue<Message> getCommunicationBuffer() {
        return this.m_communicationBuffer;
    }
    
    /**
     * Return the number of client connected to the server
     * @return client count
     */
    public int getClientCount() {
        return this.m_clientList.size();
    }
    
    /**
     * Add a client to the client list
     * @param p_client a client
     */
    public void addClient(Client p_client) {
        this.m_clientList.put(p_client.getIdClient(), p_client);
    }
    
    /**
     * Clear the communication buffer
     */
    public void clearCommunicationBuffer() {
    	this.m_communicationBuffer.clear();
    }
    
    /**
     * Disconnect a client from the server and remove it from the client list
     * @param p_client a client
     */
    public void removeClient(Client p_client) {
        this.m_clientList.remove(p_client.getIdClient());
        p_client.closeConnection();
    }

    /**
     * Send a message to all the client connected to the server
     * @param sMsg Raw string message
     */
    public void sendAllMsg(String sMsg) {
        try {
            for (Client c : this.m_clientList.values()) {
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
        this.m_threadPool.shutdownNow();
        /*for(Client client : this.m_clientList.values()) {
            this.removeClient(client);
        } */
        this.m_thread.interrupt();
        try {
            this.m_serverSocket.close();
        }
        catch (Exception e) {}
    }
}
