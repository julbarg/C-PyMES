package com.claro.cpymes.ejb;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.dao.AlarmCatalogDAORemote;
import com.claro.cpymes.dao.AlarmPymesDAORemote;
import com.claro.cpymes.dao.LogsDAORemote;
import com.claro.cpymes.dto.KeyCatalogDTO;
import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.ejb.remote.LogUtilEJBRemote;
import com.claro.cpymes.ejb.remote.ProcessEJBRemote;
import com.claro.cpymes.entity.AlarmCatalogEntity;
import com.claro.cpymes.entity.AlarmPymesEntity;
import com.claro.cpymes.entity.LogEntity;
import com.claro.cpymes.enums.FilterCatalogEnum;
import com.claro.cpymes.enums.ProcessEnum;
import com.claro.cpymes.enums.SeverityEnum;
import com.claro.cpymes.enums.StateEnum;
import com.claro.cpymes.rule.Correlacion;
import com.claro.cpymes.rule.Filtrado;
import com.claro.cpymes.util.Constant;
import com.claro.cpymes.util.Util;


/**
 * Bean que ejecuta el proceso de ejecucion de reglas
 * de filtrado y correlacion
 * @author jbarragan
 *
 */
@Stateless
@LocalBean
public class ProcessEJB implements ProcessEJBRemote {

   private static Logger LOGGER = LogManager.getLogger(ProcessEJB.class.getName());

   @EJB
   private LogsDAORemote logsDAORemote;

   @EJB
   private AlarmPymesDAORemote alarmPymesDAORemote;

   @EJB
   private AlarmCatalogDAORemote alarmCatalogDAO;

   @EJB
   private LogUtilEJBRemote logUtil;

   private HashMap<KeyCatalogDTO, AlarmCatalogEntity> catalog;

   private Filtrado filtrado;

   private Correlacion correlacion;

   private ArrayList<LogEntity> listAlarms;

   private ArrayList<LogDTO> listLogDTOs;

   private ArrayList<LogDTO> listLogDTOCorrelate;

   @PostConstruct
   private void initialize() {

      try {
         createCatalog();
         listAlarms = new ArrayList<LogEntity>();
         listLogDTOs = new ArrayList<LogDTO>();
         filtrado = new Filtrado();
         filtrado.initialize(catalog);
         correlacion = new Correlacion();
         correlacion.initialize();
      } catch (Exception e) {
         LOGGER.error("Error Iniciando Proceso: ", e);
      }
   }

   private void createCatalog() throws Exception {
      KeyCatalogDTO key;
      catalog = new HashMap<KeyCatalogDTO, AlarmCatalogEntity>();
      ArrayList<AlarmCatalogEntity> listAlarmCatalog = alarmCatalogDAO.findByFilter(FilterCatalogEnum.PYMES.getValue());
      for (AlarmCatalogEntity alarmCatalog : listAlarmCatalog) {
         key = getKey(alarmCatalog);
         if (!key.isEmpty()) {
            catalog.put(key, alarmCatalog);
         }
      }
   }

   private KeyCatalogDTO getKey(AlarmCatalogEntity alarmCatalog) {
      String OID = alarmCatalog.getOid().trim();
      String criticality = alarmCatalog.getCriticality().trim();
      if (OID != null && criticality != null) {
         return new KeyCatalogDTO(OID, criticality);
      }
      return new KeyCatalogDTO();
   }

   /**
    * Metodo principal que llama paso a paso el flujo del proceso
    * de filtrado y correlacion
    */
   public void procesar() {
      try {
         getListAlarmsEntity();
         mapearListLogDTO();
         filtrar();
         saveAlarm();

         /**cleanMemory();
         correlate();
         saveOrUpdateCEP();**/
      } catch (Exception e) {
         LOGGER.error("Error Procesando Alarmas", e);
      }

   }

   /**
    * Obtiene los logs con estado NO PROCESADO de la Base de Datos KOU
    * @return ArrayList<LogEntity> Lista de logs obtenidas
    */
   private ArrayList<LogEntity> getListAlarmsEntity() {
      listAlarms.clear();
      try {
         listAlarms = logsDAORemote.findLogsNoProcesados();
         LOGGER.info("OBTENIDAS DE KOU: " + listAlarms.size());
      } catch (Exception e) {
         LOGGER.error("Error Obtenido Registro de Logs: ", e);
      }
      return listAlarms;
   }

   /**
    * Hace el llamado a la implementacion Drools para las reglas
    * de filtrado, ejecutando uno a uno
    */
   private void filtrar() {
      try {
         for (LogDTO log : listLogDTOs) {
            if (log.isMapeado()) {
               log = filtrado.filtrar(log);
            }
         }
      } catch (Exception e) {
         LOGGER.error("Error Filtrando", e);
      }

   }

   private void saveAlarm() {
      int guardadas = 0;
      ArrayList<AlarmPymesEntity> listAlarmCreate = new ArrayList<AlarmPymesEntity>();
      listLogDTOs = alarmPymesDAORemote.validateSimilar(listLogDTOs);
      for (LogDTO logDTO : listLogDTOs) {

         if (logDTO.getSeverity() != null && logDTO.isRelevant()) {
            try {
               AlarmPymesEntity alarmEntity = getAlarmaEntity(logDTO);
               listAlarmCreate.add(alarmEntity);
               if (StateEnum.ACTIVO.getValue().equals(alarmEntity.getEstado()))
                  guardadas++;
            } catch (Exception e) {
               LOGGER.error("Error guardando Alarma", e);
            }

         }

      }
      alarmPymesDAORemote.createList(listAlarmCreate);
      LOGGER.info("FILTRADO - Alarmas Filtradas: " + guardadas);

   }

   private AlarmPymesEntity getAlarmaEntity(LogDTO logDTO) {
      AlarmPymesEntity alarmEntity = new AlarmPymesEntity();
      alarmEntity.setIp(logDTO.getIp());
      alarmEntity.setOid(logDTO.getOID());
      alarmEntity.setName(logDTO.getName());
      alarmEntity.setNodo(logDTO.getNodo());
      alarmEntity.setEventName(logDTO.getNameEvent());
      alarmEntity.setPriority(logDTO.getPriority());
      alarmEntity.setMessage(logDTO.getMessageDRL());
      String severity = logDTO.getSeverity();
      alarmEntity.setEstado(defineState(severity));
      alarmEntity.setSeverity(severity);
      Date today = new Date();
      alarmEntity.setDate(today);
      return alarmEntity;
   }

   private String defineState(String severity) {
      if (validateSeverity(severity)) {
         return StateEnum.ACTIVO.getValue();
      }
      return StateEnum.NO_SAVE.getValue();

   }

   private boolean validateSeverity(String severity) {
      return SeverityEnum.AS.getValue().equals(severity) || SeverityEnum.NAS.getValue().equals(severity)
         || SeverityEnum.PAS.getValue().equals(severity);
   }

   /**
    * Mapea los registros obtenidos en la base de datos KOU,
    */
   private void mapearListLogDTO() {
      int mapeadas = 0;
      listLogDTOs.clear();
      for (LogEntity logEntity : listAlarms) {
         LogDTO logDTO = logUtil.mapearLog(logEntity);
         listLogDTOs.add(logDTO);
         if (logDTO.isMapeado()) {
            mapeadas++;
         }
      }
      LOGGER.info("MAPEADAS - Alarmas Mapeadas: " + mapeadas);
      logsDAORemote.updateList(listAlarms);
      LOGGER.info("ACTUALIZADAS - Alarmas Actualizadas: " + mapeadas);

   }

   /**
    * Limpia las alarmas que han sido marcadas como reconocidas por el usuario.
    * Se extraen del WorkingMemoryEntryPoint
    */
   private void cleanMemory() {
      Date endDate = new Date();
      Date startDate = Util.restarFecha(endDate, Constant.TIME_RECOGNIZE_CORRELATION);
      ArrayList<AlarmPymesEntity> listAlarmsReconocidas = alarmPymesDAORemote.findSimiliarCEPReconocidas(startDate,
         endDate);
      for (AlarmPymesEntity alarmEntity : listAlarmsReconocidas) {
         correlacion.retract(alarmEntity.getNodo(), alarmEntity.getNameCorrelation());
      }

   }

   /**
    * Hace el llamado a la implemtacion Drools para las reglas de correlacion. 
    * Solo se ejecutan las que han sido marcadas como correlacionable y relevante
    */
   private void correlate() {
      for (LogDTO logCEP : listLogDTOs) {
         if (logCEP.isCorrelation() && logCEP.isRelevant()) {
            // Si fueron marcados con correlacion y son relevantes se almacenan en MemoryEntryPoint para tener
            // en cuenta en la proxima ejecucion de correlacion
            logCEP.setContCorrelate(1);
            correlacion.insertToEntryPoint(logCEP);
         }
      }
      listLogDTOCorrelate = correlacion.getListLogsCorrelation();

   }

   /**
   * Una vez ejecutadas las reglas de filtrado son procesadas las
   * reglas correlacionadas que tengan mayor o igual de numero de alarmas correlacionadas
   * configuradas en el sistema
   */
   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
   private void saveOrUpdateCEP() {
      for (LogDTO logDTO : listLogDTOCorrelate) {
         if (isVerificable(logDTO)) {
            try {
               ArrayList<AlarmPymesEntity> listAlarmsSimilar = findAlarmCPESavePrevious(logDTO);
               if (listAlarmsSimilar.size() > 0) {
                  updateAlarmCEP(logDTO, listAlarmsSimilar.get(0));
               } else {
                  saveAlarmCEP(logDTO);
               }
            } catch (Exception e) {
               LOGGER.error("Error guardando Alarma", e);
            }
         }
      }
   }

   /**
    * Validacion para poder guardar una alarma correlacionada
    * La alarma a guardar debe tener mayor o igual numero de alarmas
    * configuradas en el sistema
    * @param logDTO Contiene los datos a validar
    * @return Resultado de la verificacion
    */
   private boolean isVerificable(LogDTO logDTO) {
      return logDTO.getContCorrelate() >= Constant.NUMBER_ALARMS_CORRELATE && logDTO.getDate() != null;
   }

   /**
    * Busca alarmas por nodo, nameCorrelation y date
    * @param logDTO Log que contiene la informacion para realizar busqueda
    * @return ArrayList<AlarmPymesEntity> de alarmas encontradas
    */
   private ArrayList<AlarmPymesEntity> findAlarmCPESavePrevious(LogDTO logDTO) {
      Date date = logDTO.getDate();
      String nodo = logDTO.getNodo();
      String nameCorrelation = logDTO.getNameCorrelation();
      ArrayList<AlarmPymesEntity> listAlarmsSimilar = alarmPymesDAORemote.findSimiliarCEP(nodo, nameCorrelation, date);
      return listAlarmsSimilar;
   }

   private void updateAlarmCEP(LogDTO logDTO, AlarmPymesEntity alarmEntity) {
      alarmEntity.setMessage(logDTO.getMessageDRL());
      alarmEntity.setEstado(StateEnum.ACTIVO.getValue());

      alarmPymesDAORemote.update(alarmEntity);

   }

   private void saveAlarmCEP(LogDTO logDTO) {
      AlarmPymesEntity alarmEntity = new AlarmPymesEntity();
      alarmEntity.setIp(logDTO.getIp());
      alarmEntity.setOid(logDTO.getOID());
      alarmEntity.setName(logDTO.getName());
      alarmEntity.setEventName(logDTO.getNameEvent());
      alarmEntity.setPriority(logDTO.getPriority());
      alarmEntity.setMessage(logDTO.getMessageDRL());
      alarmEntity.setNameCorrelation(logDTO.getNameCorrelation());
      alarmEntity.setEstado(StateEnum.ACTIVO.getValue());
      alarmEntity.setNodo(logDTO.getNodo());
      alarmEntity.setDate(logDTO.getDate());

      alarmPymesDAORemote.create(alarmEntity);
   }

}
