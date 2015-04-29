package com.claro.cpymes.ejb.remote;

import java.util.ArrayList;

import javax.ejb.Remote;

import com.claro.cpymes.dto.AlarmPymesDTO;
import com.claro.cpymes.dto.PriorityCountDTO;


/**
 * Interface remota para CPYMESEJB
 * @author jbarragan
 *
 */
@Remote
public interface CPYMESEJBRemote {

   public ArrayList<AlarmPymesDTO> loadAlarm() throws Exception;

   public void update(AlarmPymesDTO alarmDTO) throws Exception;

   public PriorityCountDTO countAlarm(ArrayList<AlarmPymesDTO> listAlarm);

   public ArrayList<AlarmPymesDTO> loadAlarmByPriority(ArrayList<String> listPrioritySelect);

}
