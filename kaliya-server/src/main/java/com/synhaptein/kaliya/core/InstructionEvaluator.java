package com.synhaptein.kaliya.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstraction of an instruction manager to construct modules.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public abstract class InstructionEvaluator {
    /**
     * The link between String and Integer of the command that the
     * instruction evaluator implements
     */
    protected Map<String, Integer> m_instructionMap;
    
    /**
     * Construct a new instruction evaluator
     */
    public InstructionEvaluator() {
        this.m_instructionMap = new HashMap<String, Integer>();
    }
    
    /**
     * When an object extends this class, it must offer a function that
     * evaluate the message and construct an answer to this message
     * @param p_message A message that the server receives from a client
     * @return the message that the server must return to the client
     */
    public abstract Message eval(Message p_message);
    
    /**
     * Return the integer value of a string command
     * @param p_command the command string from the client
     * @return the integer value of the command
     */
    protected int getCommand(String p_command){
        return this.m_instructionMap.get(p_command) != null ? this.m_instructionMap.get(p_command) : -1;
    }
}
