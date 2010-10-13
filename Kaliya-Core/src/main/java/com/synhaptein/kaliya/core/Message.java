package com.synhaptein.kaliya.core;

/**
 * Message that go from client <-> server.
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
public class Message {

    private Client m_client;
    private String m_command;
    private String m_parameters[];
    private Target m_target;
    
    /**
     * Construct a new Message to send to one or all the client
     * @param p_message the message
     * @param p_target the target value
     */
    public Message(String p_message, Target p_target) {
        this.m_command = p_message;
        this.m_target = p_target;
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
     * Construct a new Message that come from a client
     * @param p_client client that send the message
     * @param p_command the command
     * @param p_args the arguments to the command
     */
    public Message(Client p_client, String p_command, String p_args[]) {
        this.m_client = p_client;
        this.m_command = p_command;
        this.m_parameters = p_args;
    }

    /**
     * Construct a new Message by parsing the string that the client send
     * to the server
     * @param p_client the client that send the message
     * @param p_message the raw string message send by the client
     */
    public Message(Client p_client, String p_message) {
        this.parseIncomingMessage(p_message);
        this.m_client = p_client;
    }

    /**
     * Return the client that send this message
     * @return a client
     */
    public Client getClient() {
        return this.m_client;
    }

    /**
     * Return the command contains in this message
     * @return a command
     */
    public String getCommand() {
        return this.m_command;
    }

    /**
     * Return an array of argument
     * @return arguments
     */
    public String[] getArgs() {
        return this.m_parameters;
    }

    /**
     * Return the target of this message
     * @return the target
     */
    public Target getTarget() {
        return this.m_target;
    }

    /**
     * Set the target of this message
     * @param p_target a target
     */
    public void setTarget(Target p_target) {
        this.m_target = p_target;
    }

    /**
     * Construct a string form the command and arguments contains in this
     * message
     * @return the complet message
     */
    public String toString() {
        String result = "";
        if (this.m_command != null && this.m_command.length() > 0) {
            result += this.m_command + " ";
            if (this.m_parameters != null) {
                for (String arg : this.m_parameters) {
                    result += arg + " ";
                }
            }
            result.substring(0, result.length() - 1);
        }
        return result;
    }

    /**
     * Parse a raw string message in a command and an array of parameters
     * @param p_message the raw message
     */
    private void parseIncomingMessage(String p_message) {
        String command = p_message.split(" ")[0];
        String parameters = p_message.substring(command.length());
        if(parameters.length() > 0) {
            parameters = parameters.substring(1);
        }
        String parameterLst[] = parameters.split(" ");
        this.m_command = command;
        this.m_parameters = parameterLst;
    }
}
