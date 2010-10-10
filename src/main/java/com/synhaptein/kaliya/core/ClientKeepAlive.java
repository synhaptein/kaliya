package com.synhaptein.kaliya.core;

import java.util.Map;

/**
 * The ClientKeppAlive class is a watchdog that keep the client list clean
 * by disconnecting dead client.
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
public class ClientKeepAlive extends Thread {

    private Map<String, Client> m_clientList;
    private Thread m_thread;
    private Server m_server;
    
    /**
     * Construct a new watchdog for a specific server
     * @param p_server
     */
    public ClientKeepAlive(Server p_server) {
        this.m_clientList = p_server.getClientList();
        this.m_server = p_server;
        this.m_thread = new Thread(this);
        this.m_thread.start();
    }

    /**
     * Stop the watchdog
     */
    public void stopThis() {
        this.m_thread = null;
    }

    /**
     * Send a ping request to all the clients at a fixed frequency
     */
    public void run() {
        Thread currentThread = Thread.currentThread();
        while (currentThread == this.m_thread) {
            for (Client client : this.m_clientList.values()) {
                try {
                    client.sendMsg("PING!\n");
                } catch (Exception e) {
                    this.m_server.removeClient(client);
                    e.printStackTrace();
                }
            }
            try {
                sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
