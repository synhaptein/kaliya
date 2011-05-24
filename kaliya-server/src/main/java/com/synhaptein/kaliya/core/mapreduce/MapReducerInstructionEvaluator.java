package com.synhaptein.kaliya.core.mapreduce;

import com.synhaptein.kaliya.core.InstructionEvaluator;
import com.synhaptein.kaliya.core.Message;
import com.synhaptein.kaliya.core.worker.Worker;
import org.codehaus.jackson.type.TypeReference;

/**
 * Evaluate message form worker.
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

public class MapReducerInstructionEvaluator<Vint, Vout>
        extends InstructionEvaluator<MapReducerInstructionEvaluator.Instruction> {
    MapReducer<?, Vint, Vout> m_mapReducer;
    MapReducerListener<Vint, Vout> m_mapReducerListener;
    public enum Instruction {
        MAPRETURN,
        REDUCERETURN,
        ERROR;
    }

    public MapReducerInstructionEvaluator(MapReducer<?, Vint, Vout> p_mapReducer,
                                          MapReducerListener<Vint, Vout> p_mapReducerListener) {
        super();
        m_mapReducer = p_mapReducer;
        m_mapReducerListener = p_mapReducerListener;
    }

    private void setInstructions() {
        for(Instruction instruction : Instruction.values()) {
            m_instructionMap.put(instruction.toString(), instruction);
        }
    }

    @Override
    public Message eval(Message p_message) {
        try {
            MapReduceResponse<Vint, Vout> response = Task.mapper.readValue(p_message.getMessage(),
                    new TypeReference<MapReduceResponse<Vint, Vout>>() {});
            Worker worker = (Worker)p_message.getClient();

            if(response.id.equals(m_mapReducer.getIdJob())) {
                if("MAP".equals(response.type) && !m_mapReducerListener.isMapFinished()) {
                    m_mapReducer.addFinishedMapper((Worker)p_message.getClient(), response.pairList);
                }
                else if("REDUCE".equals(response.type)) {
                    m_mapReducer.addFinishedReducer((Worker)p_message.getClient(), response.pair);
                }
            }

            if(worker.getStatus() == Worker.Status.WORKING) {
                m_mapReducer.release(worker);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
