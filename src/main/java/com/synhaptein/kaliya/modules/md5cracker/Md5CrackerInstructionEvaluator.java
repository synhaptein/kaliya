package com.synhaptein.kaliya.modules.md5cracker;

import com.synhaptein.kaliya.core.InstructionEvaluator;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.job.Job;

/**
 * Instruction evaluator of the MD5 Cracker module.
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
public class Md5CrackerInstructionEvaluator extends InstructionEvaluator {
    private static final int GETBLOCK = 0;
    private static final int FOUND = 1;
    private static final int GETENCODESTRING = 2;
    private WordIterator m_wordIterator;
    private Job m_job;
    
    /**
     * Construct a new instruction evaluator to evaluate the message receive
     * from the client.
     * @param p_job job
     */
    public Md5CrackerInstructionEvaluator(Job p_job) {
        super();
        this.m_job = p_job;
        this.m_wordIterator = new WordIterator();
        this.constructInstructionMap();
    }
    
    /**
     * Construct a map of equivalence between command string and integer
     */
    private void constructInstructionMap(){
        this.m_instructionMap.put("GETBLOCK", GETBLOCK);
        this.m_instructionMap.put("FOUND", FOUND);
        this.m_instructionMap.put("GETENCODESTRING", GETENCODESTRING);
    }
    
    @Override
    public Message eval(Message p_message) {
        Message output = null;
        switch (this.getCommand(p_message.getCommand())) {
            case GETBLOCK:
                output = new Message(this.getNextBlock(), Message.Target.UNIQUE);
                break;
            case FOUND:
                output = new Message("ANSWER " + p_message.getArgs()[0], Message.Target.ALL);
                this.m_job.setStatus(Job.JobStatus.FINISHED);
                break;
            case GETENCODESTRING:
                output = new Message("ENCODESTRING " + this.m_job.toString(), Message.Target.UNIQUE);
                break;
            default:
                break;
        }

        return output;
    }
    
    /**
     * Return a block of string to test
     * @return a list of word to test
     */
    private String getNextBlock() {
        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < 999; ++i) {
            result.append("\"");
            result.append(this.m_wordIterator.next());
            result.append("\",");
        }
        result.append("\"");
        result.append(this.m_wordIterator.next());
        result.append("\"]");
        return result.toString();
    }

}
