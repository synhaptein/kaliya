package com.synhaptein.kaliya.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Abstraction of a client.
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
public abstract class Client implements Runnable {

    /**
     * Client's socket
     */
    protected Socket m_socket;
    /**
     * Socket's input stream
     */
    protected BufferedReader m_readerIn;
    /**
     * Socket's output stream 
     */
    protected PrintWriter m_writerOut;
    /**
     * Client's id
     */
    protected String m_id;
    /**
     * Client's server
     */
    protected Server m_server;

    /**
     * Construct a new client with its server and its socket
     * @param p_server The client server
     * @param p_socket The client socket
     */
    public Client(Server p_server, Socket p_socket) {
        try {
            this.m_socket = p_socket;
            this.m_server = p_server;
            this.m_readerIn = new BufferedReader(new InputStreamReader(p_socket.getInputStream()));
            this.m_writerOut = new PrintWriter(p_socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Return the string that identify the client
     * @return The client id
     */
    public String getIdClient() {
        return this.m_id;
    }
    
    /**
     * This asbtract class extends the class Thread. When an object
     * extends this abstract class, it must implements this method.
     */
    public abstract void run();

    /**
     * Return the client's ip
     * @return The client's ip
     */
    public String getIp() {
        String ip = ("" + this.m_socket.getRemoteSocketAddress()).split(":")[0];
        return ip.substring(1, ip.length());
    }

    /**
     * Send a string to the client
     * @param p_sMsg The message
     */
    public synchronized void sendMsg(String p_sMsg) {
        try {
            this.m_writerOut.write(p_sMsg);
            this.m_writerOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the connection and disconnect the client from the server
     */
    public synchronized void closeConnection() {
        try {
            this.m_socket.close();
            this.m_readerIn.close();
            this.m_writerOut.close();
        } catch (Exception e) {
            System.out.println("Error while removing client : " + this.getIdClient());
        }
    }
}
