package com.claro.cpymes.ejb;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.claro.cpymes.dao.AlarmPymesDAORemote;
import com.claro.cpymes.dto.AlarmPymesDTO;
import com.claro.cpymes.dto.PriorityCountDTO;
import com.claro.cpymes.ejb.remote.CPYMESEJBRemote;
import com.claro.cpymes.entity.AlarmPymesEntity;
import com.claro.cpymes.enums.PriorityEnum;
import com.claro.cpymes.enums.StateEnum;


/**
 * CPYMESEJB - Bean que controla acciones de CPyMES
 * @author jbarragan
 *
 */
@Stateless
@LocalBean
public class CPYMESEJB implements CPYMESEJBRemote {

   @EJB
   private AlarmPymesDAORemote alarmPymesDAO;

   @PostConstruct
   private void initialize() {

   }

   /**
    * Consulta alarmas en estado ACTIVO
    * @return ArrayList<AlarmPymesDTO> Alarmas encontradas
    */
   @Override
   public ArrayList<AlarmPymesDTO> loadAlarm() throws Exception {
      ArrayList<AlarmPymesDTO> listAlarmPymesDTO = new ArrayList<AlarmPymesDTO>();
      ArrayList<AlarmPymesEntity> listAlarmEntity = alarmPymesDAO.findByEstado(StateEnum.ACTIVO.getValue());
      for (AlarmPymesEntity alarmEntity : listAlarmEntity) {

         AlarmPymesDTO alarmDTO = new AlarmPymesDTO();
         alarmDTO.setId(alarmEntity.getId());
         alarmDTO.setPriority(alarmEntity.getPriority());
         alarmDTO.setIp(alarmEntity.getIp());
         alarmDTO.setNameEvent(alarmEntity.getEventName());
         alarmDTO.setOid(alarmEntity.getOid());
         alarmDTO.setMessage(alarmEntity.getMessage());
         alarmDTO.setDate(alarmEntity.getDate());
         alarmDTO.setNameCorrelation(alarmEntity.getNameCorrelation());
         alarmDTO.setNodo(alarmEntity.getNodo());
         alarmDTO.setSeverity(alarmEntity.getSeverity());

         if (alarmEntity.getNameCorrelation() == null) {
            alarmDTO.setName(alarmEntity.getName());
         } else {
            alarmDTO.setName(alarmEntity.getNodo());
         }

         listAlarmPymesDTO.add(alarmDTO);
      }
      return listAlarmPymesDTO;

   }

   /**
    * Actualiza alarma
    * @param alarmDTO Alarma a actualizar
    */
   @Override
   public void update(AlarmPymesDTO alarmDTO) throws Exception {
      AlarmPymesEntity alarmEntity = new AlarmPymesEntity();
      alarmEntity.setId(alarmDTO.getId());
      alarmEntity.setPriority(alarmDTO.getPriority());
      alarmEntity.setIp(alarmDTO.getIp());
      alarmEntity.setName(alarmDTO.getName());
      alarmEntity.setOid(alarmDTO.getOid());
      alarmEntity.setEventName(alarmDTO.getNameEvent());
      alarmEntity.setMessage(alarmDTO.getMessage());
      alarmEntity.setDatetimeAcknowledge(alarmDTO.getDatetimeAcknowledge());
      alarmEntity.setNodo(alarmDTO.getNodo());
      alarmEntity.setNameCorrelation(alarmDTO.getNameCorrelation());
      alarmEntity.setEstado(alarmDTO.getEstado());
      alarmEntity.setDate(alarmDTO.getDate());

      alarmPymesDAO.update(alarmEntity);

   }

   /**
    * Obtiene el numero de alarmas por Priority
    * @param listAlarm Lista de la cual se realiza el conteo
    * @return PriorityCountDTO con el conteo obtenido
    */
   @Override
   public PriorityCountDTO countAlarm(ArrayList<AlarmPymesDTO> listAlarm) {
      int count = 0;
      PriorityCountDTO priorityCountDTO = new PriorityCountDTO();
      for (AlarmPymesDTO alarmPymes : listAlarm) {
         String priority = alarmPymes.getPriority();
         if (priority.equals(PriorityEnum.ALERT.getValue())) {
            count = priorityCountDTO.getAlert() + 1;
            priorityCountDTO.setAlert(count);

         } else if (priority.equals(PriorityEnum.CRITIC.getValue())) {
            count = priorityCountDTO.getCrit() + 1;
            priorityCountDTO.setCrit(count);

         } else if (priority.equals(PriorityEnum.INFO.getValue())) {
            count = priorityCountDTO.getInfo() + 1;
            priorityCountDTO.setInfo(count);

         } else if (priority.equals(PriorityEnum.NOTICE.getValue())) {
            count = priorityCountDTO.getNotice() + 1;
            priorityCountDTO.setNotice(count);

         } else if (priority.equals(PriorityEnum.WARNING.getValue())) {
            count = priorityCountDTO.getWarning() + 1;
            priorityCountDTO.setWarning(count);

         }
      }
      return priorityCountDTO;
   }

   /**
    * Cargar alarmas por prioridades
    * @param listPrioritySelect Lista de prioridades a filtrar
    * @return ArrayList<AlarmPymesDTO> alarmas encontradas
    */
   @Override
   public ArrayList<AlarmPymesDTO> loadAlarmByPriority(ArrayList<String> listPrioritySelect) {
      ArrayList<AlarmPymesDTO> listAlarmPymesDTO = new ArrayList<AlarmPymesDTO>();
      ArrayList<AlarmPymesEntity> listAlarmEntity = alarmPymesDAO.findByPriority(listPrioritySelect);
      for (AlarmPymesEntity alarmEntity : listAlarmEntity) {

         AlarmPymesDTO alarmDTO = new AlarmPymesDTO();
         alarmDTO.setId(alarmEntity.getId());
         alarmDTO.setPriority(alarmEntity.getPriority());
         alarmDTO.setIp(alarmEntity.getIp());
         alarmDTO.setNameEvent(alarmEntity.getEventName());
         alarmDTO.setOid(alarmEntity.getOid());
         alarmDTO.setMessage(alarmEntity.getMessage());
         alarmDTO.setDate(alarmEntity.getDate());
         alarmDTO.setNameCorrelation(alarmEntity.getNameCorrelation());
         alarmDTO.setNodo(alarmEntity.getNodo());

         if (alarmEntity.getNameCorrelation() == null) {
            alarmDTO.setName(alarmEntity.getName());
         } else {
            alarmDTO.setName(alarmEntity.getNodo());
         }

         listAlarmPymesDTO.add(alarmDTO);
      }
      return listAlarmPymesDTO;
   }
}
