package com.synhaptein.kaliya.core;

/**
 * Message that go from client <-> server.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class Message {

    private Client m_client;
    private String m_message;
    private Target m_target;
    
    /**
     * Construct a new Message to send to one or all the client
     * @param p_message the message
     * @param p_target the target value
     */
    public Message(String p_message, Target p_target) {
        m_message = p_message;
        m_target = p_target;
    }

    /**
     * List of the type of target possible
     */
    public enum Target {
        /**
         * All the client connect to the same server
         */
        ALL,
        /**
         * Only the client that send the original message
         */
        UNIQUE;
    }

    /**
     * Construct a new Message by parsing the string that the client send
     * to the server
     * @param p_client the client that send the message
     * @param p_message the raw string message send by the client
     */
    public Message(Client p_client, String p_message) {
        m_message = p_message;
        m_client = p_client;
    }

    /**
     * Return the client that send this message
     * @return a client
     */
    public Client getClient() {
        return m_client;
    }

    /**
     * Return the message
     * @return the message
     */
    public String getMessage() {
        return m_message;
    }

    /**
     * Return the target of this message
     * @return the target
     */
    public Target getTarget() {
        return m_target;
    }

    /**
     * Set the target of this message
     * @param p_target a target
     */
    public void setTarget(Target p_target) {
        m_target = p_target;
    }
}
