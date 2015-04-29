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
import com.claro.cpymes.util.LogUtil;
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

   private HashMap<KeyCatalogDTO, AlarmCatalogEntity> catalog;

   private Filtrado filtrado;

   private Correlacion correlacion;

   private int procesadas;

   @PostConstruct
   private void initialize() {

      try {
         createCatalog();
         filtrado = new Filtrado();
         filtrado.initialize(catalog);
         correlacion = new Correlacion();
         correlacion.initialize();
      } catch (Exception e) {
         LOGGER.error("Error iniciando Rules: ", e);
      }
   }

   private void createCatalog() throws Exception {
      KeyCatalogDTO key;
      catalog = new HashMap<KeyCatalogDTO, AlarmCatalogEntity>();
      ArrayList<AlarmCatalogEntity> listAlarmCatalog = alarmCatalogDAO.findByFilter(FilterCatalogEnum.PYMES.getValue());
      for (AlarmCatalogEntity alarmCatalog : listAlarmCatalog) {
         if ((key = getKey(alarmCatalog)) != null) {
            catalog.put(key, alarmCatalog);
         }
      }
   }

   private KeyCatalogDTO getKey(AlarmCatalogEntity alarmCatalog) {
      String OID = alarmCatalog.getOid().trim();
      String nameEvent = alarmCatalog.getTextAlarm().trim();
      String criticality = alarmCatalog.getCriticality().trim();
      if (OID != null && nameEvent != null && criticality != null) {
         return new KeyCatalogDTO(OID, criticality);
      }
      return null;

   }

   /**
    * Metodo principal que llama paso a paso el flujo del proceso
    * de filtrado y correlacion
    */
   public void procesar() {
      try {
         procesarCli();
      } catch (Exception e) {
         LOGGER.error("Error Procesando Alarmas", e);
      }

   }

   private void procesarCli() {
      LOGGER.info("INICIO PROCESO PRINCIPAL");
      ArrayList<LogEntity> listAlarms = getListAlarmsEntity();
      ArrayList<LogDTO> listLogDTOs = mapearListLogDTO(listAlarms);
      listLogDTOs = filtrar(listLogDTOs);

      saveAlarmCatalog(listLogDTOs);
      /* saveAlarm(listLogDTOs);
       * cleanMemory();
       * correlate(listLogDTOs); */
      LOGGER.info("FIN");
      LOGGER.info("-----------------------------------------------");
   }

   private void saveAlarmCatalog(ArrayList<LogDTO> listLogDTOs) {
      ArrayList<AlarmPymesEntity> listAlarmCreate = new ArrayList<AlarmPymesEntity>();
      listLogDTOs = alarmPymesDAORemote.validateSimilar(listLogDTOs);
      for (LogDTO logDTO : listLogDTOs) {

         if (logDTO.getSeverity() != null && logDTO.isRelevant()) {
            try {
               AlarmPymesEntity alarmEntity = new AlarmPymesEntity();
               alarmEntity.setIp(logDTO.getIp());
               alarmEntity.setOid(logDTO.getOID());
               alarmEntity.setName(logDTO.getName());
               alarmEntity.setNodo(logDTO.getNodo());
               alarmEntity.setEventName(logDTO.getNameEvent());
               alarmEntity.setPriority(logDTO.getPriority());
               alarmEntity.setMessage(logDTO.getMessageDRL());
               // TODO
               String severity = logDTO.getSeverity();
               if (SeverityEnum.AS.getValue().equals(severity) || SeverityEnum.NAS.getValue().equals(severity)
                  || SeverityEnum.PAS.getValue().equals(severity)) {
                  alarmEntity.setEstado(StateEnum.ACTIVO.getValue());
                  LOGGER.info("FIND IN CATALOG:" + logDTO.getNameEvent() + " " + logDTO.getSeverity());
               } else {
                  alarmEntity.setEstado(StateEnum.NO_SAVE.getValue());
               }

               alarmEntity.setSeverity(severity);
               Date today = new Date();
               alarmEntity.setDate(today);

               listAlarmCreate.add(alarmEntity);

            } catch (Exception e) {
               LOGGER.error("Error guardando Alarma", e);
            }

         }

      }
      alarmPymesDAORemote.createList(listAlarmCreate);
      LOGGER.info("CORRELACION - Alarmas Procesadas: " + procesadas);

   }

   /**
    * Obtiene los logs con estado NO PROCESADO de la Base de Datos
    * KOU
    * @return ArrayList<LogEntity> Lista de logs obtenidas
    */
   private ArrayList<LogEntity> getListAlarmsEntity() {
      ArrayList<LogEntity> listAlarms = new ArrayList<LogEntity>();
      try {
         listAlarms = logsDAORemote.findByEstado(ProcessEnum.NO_PROCESADO.getValue());
         LOGGER.info("FIND BY ESTADO - Alarmas Encontradas KOU: " + listAlarms.size());
      } catch (Exception e) {
         LOGGER.error("Obtenido Registro de Logs: ", e);
      }
      return listAlarms;
   }

   /**
    * Mapea los registros obtenidos en la base de datos KOU,
    * Atravez de string obtiene la informacion para ser mapaeada a
    * objetos java. IP, OID, EventName, NameDevice, Priority, Interface, Nodo
    * Description a el objeto LogDTO
    * @param listAlarms Lista de logs a procesar
    * @return ArrayList<LogEntity> Mapeados
    */
   private ArrayList<LogDTO> mapearListLogDTO(ArrayList<LogEntity> listAlarms) {
      int mapeadas = 0;
      ArrayList<LogDTO> listLogDTOs = new ArrayList<LogDTO>();
      String procesados = ProcessEnum.PROCESADO.getValue();
      for (LogEntity logEntity : listAlarms) {
         LogDTO logDTO = LogUtil.mapearLogEntity(logEntity);
         listLogDTOs.add(logDTO);
         logEntity.setProcesados(procesados);
         if (logDTO.isMapeado()) {
            mapeadas++;
         }
      }
      logsDAORemote.updateList(listAlarms);
      LOGGER.info("MAPEADAS - Alarmas Mapeadas: " + mapeadas);
      return listLogDTOs;
   }

   /**
    * Hace el llamado a la implementacion Drools para las reglas
    * de filtrado, ejecutando uno a uno
    * Son analizadas regla por regla y cambiando el estado en el 
    * campo relevant y messageDrl del objeto LogDTO
    * @param listLogDTOs Lista de logs a ejecutar filtros
    * @return ArrayList<LogDTO> con la informacion modificada resultado del filtrado
    */
   private ArrayList<LogDTO> filtrar(ArrayList<LogDTO> listLogDTOs) {
      try {
         procesadas = 0;
         for (LogDTO log : listLogDTOs) {
            if (log.isMapeado()) {
               log = filtrado.filtrar(log);
               procesadas++;
            }
         }

         return listLogDTOs;
      } catch (Exception e) {
         LOGGER.error("Error Filtrando", e);
         return listLogDTOs;
      }

   }

   /**
    * Guarda la informacion de las alarmas que han cambiado
    * su estado a relevante, una vez ejecutada todas las 
    * reglas de filtrado
    * @param listLogDTOs Lista de logs modificados
    */
   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
   private void saveAlarm(ArrayList<LogDTO> listLogDTOs) {
      int relevantes = 0;
      ArrayList<AlarmPymesEntity> listAlarmCreate = new ArrayList<AlarmPymesEntity>();

      listLogDTOs = alarmPymesDAORemote.validateSimilar(listLogDTOs);
      for (LogDTO logDTO : listLogDTOs) {
         if (logDTO.isRelevant()) {
            try {
               AlarmPymesEntity alarmEntity = new AlarmPymesEntity();
               alarmEntity.setIp(logDTO.getIp());
               alarmEntity.setOid(logDTO.getOID());
               alarmEntity.setName(logDTO.getName());
               alarmEntity.setNodo(logDTO.getNodo());
               alarmEntity.setEventName(logDTO.getNameEvent());
               alarmEntity.setPriority(logDTO.getPriority());
               alarmEntity.setMessage(logDTO.getMessageDRL());
               alarmEntity.setEstado(StateEnum.ACTIVO.getValue());
               Date today = new Date();
               alarmEntity.setDate(today);

               listAlarmCreate.add(alarmEntity);
               relevantes++;

            } catch (Exception e) {
               LOGGER.error("Error guardando Alarma", e);
            }

         }

      }
      alarmPymesDAORemote.createList(listAlarmCreate);
      LOGGER.info("FILTRADO - Alarmas Procesadas: " + procesadas);
      LOGGER.info("FILTRADO - Alarmas Relevantes: " + relevantes);

   }

   /**
    * Hace el llamado a la implemtacion Drools para las reglas
    * de correlacion. Solo se ejecutan las que han sido marcadas 
    * como correlacionable y relevante en el proceso
    * de filtrado
    * @param listLogDTOs Lista de logs a ejecutar
    */
   private void correlate(ArrayList<LogDTO> listLogDTOs) {
      for (LogDTO logCEP : listLogDTOs) {
         if (logCEP.isCorrelation() && logCEP.isRelevant()) {
            // Si fueron marcados con correlacion y son relevantes
            // se almacenan en MemoryEntryPoint para tener en cuenta
            // en la proxima ejecucion de correlacion
            logCEP.setContCorrelate(1);
            correlacion.insertToEntryPoint(logCEP);
         }
      }
      processAlarmsCEP(correlacion.getListLogsCorrelation());

   }

   /**
    * Limpia las alarmas que han sido marcadas como reconocidas por el usuario.
    * Se extraen del WorkingMemoryEntryPoint
    */
   private void cleanMemory() {
      Date endDate = new Date();
      Date startDate = Util.restarFecha(endDate, Constant.TIME_RECOGNIZE_CORRELATION);
      ArrayList<AlarmPymesEntity> listAlarmsReconocidas = alarmPymesDAORemote.findSimiliarCEPReconocidas(startDate, endDate);
      for (AlarmPymesEntity alarmEntity : listAlarmsReconocidas) {
         correlacion.retract(alarmEntity.getNodo(), alarmEntity.getNameCorrelation());
      }

   }

   /**
    * Una vez ejecutadas las reglas de filtrado son procesadas las
    * reglas correlacionadas que tengan mayor o igual de numero de alarmas correlacionadas
    * configuradas en el sistema
    * @param listLogDTOs
    */
   @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
   private void processAlarmsCEP(ArrayList<LogDTO> listLogDTOs) {
      int news = 0;
      int update = 0;
      for (LogDTO logDTO : listLogDTOs) {
         if (isVerificable(logDTO)) {
            try {
               ArrayList<AlarmPymesEntity> listAlarmsSimilar = findAlarmCPE(logDTO);
               if (listAlarmsSimilar.size() > 0) {
                  updateAlarmCEP(logDTO, listAlarmsSimilar.get(0));
                  update++;
               } else {
                  saveAlarmCEP(logDTO);
                  news++;
               }
            } catch (Exception e) {
               LOGGER.error("Error guardando Alarma", e);
            }
         }
      }
      LOGGER.info("CORRELACION - Alarmas Procesadas: " + listLogDTOs.size());
      LOGGER.info("CORRELACION - Alarmas Correlacionadas Nuevas: " + news);
      LOGGER.info("CORRELACION - Alarmas Correlacionadas Actualizadas: " + update);
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
   private ArrayList<AlarmPymesEntity> findAlarmCPE(LogDTO logDTO) {
      Date date = logDTO.getDate();
      String nodo = logDTO.getNodo();
      String nameCorrelation = logDTO.getNameCorrelation();
      ArrayList<AlarmPymesEntity> listAlarmsSimilar = alarmPymesDAORemote.findSimiliarCEP(nodo, nameCorrelation, date);
      return listAlarmsSimilar;
   }

   /**
    * Actualiza la alarma correlacionada
    * @param logDTO Log para ampliar informacion
    * @param alarmEntity Alarma a actualizar
    */
   private void updateAlarmCEP(LogDTO logDTO, AlarmPymesEntity alarmEntity) {
      alarmEntity.setMessage(logDTO.getMessageDRL());
      alarmEntity.setEstado(StateEnum.ACTIVO.getValue());

      alarmPymesDAORemote.update(alarmEntity);

   }

   /**
    * Crea una alarma correlacionada
    * @param logDTO Log que contiene informacion
    */
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
