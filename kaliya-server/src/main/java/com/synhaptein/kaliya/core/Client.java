package com.synhaptein.kaliya.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Abstraction of a client.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public abstract class Client<T> implements Runnable {

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
    protected T m_server;

    /**
     * Construct a new client with its server and its socket
     * @param p_server The client server
     * @param p_socket The client socket
     */
    public Client(T p_server, Socket p_socket) {
        try {
            m_socket = p_socket;
            m_server = p_server;
            m_readerIn = new BufferedReader(new InputStreamReader(p_socket.getInputStream()));
            m_writerOut = new PrintWriter(p_socket.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Return the string that identify the client
     * @return The client id
     */
    public String getIdClient() {
        return m_id;
    }
    
    /**
     * This asbtract class extends the class Thread. When an object
     * extends this abstract class, it must implements this method.
     */
    public abstract void runClient() throws InterruptedException;

    public void run() {
        try {
            runClient();
        }
        catch (InterruptedException e) {}
        KaliyaLogger.logAdmin("Disconnecting client: " + getIdClient());
    }

    /**
     * Return the client's ip
     * @return The client's ip
     */
    public String getIp() {
        String ip = ("" + m_socket.getRemoteSocketAddress()).split(":")[0];
        return ip.substring(1, ip.length());
    }

    /**
     * Send a string to the client
     * @param p_sMsg The message
     */
    public synchronized void sendMsg(String p_sMsg) {
        try {
            DataOutputStream os = new DataOutputStream(m_socket.getOutputStream());
            os.writeUTF(p_sMsg);
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close the connection and disconnect the client from the server
     */
    public synchronized void closeConnection() {
        try {
            m_socket.close();
            m_readerIn.close();
            m_writerOut.close();
        } catch (Exception e) {
            KaliyaLogger.logAdmin("Error while removing client : " + getIdClient());
        }
    }
}
