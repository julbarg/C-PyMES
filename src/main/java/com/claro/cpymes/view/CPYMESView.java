package com.claro.cpymes.view;

import java.util.ArrayList;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.dao.LogsDAORemote;
import com.claro.cpymes.dto.AlarmPymesDTO;
import com.claro.cpymes.dto.PriorityCountDTO;
import com.claro.cpymes.ejb.remote.CPYMESEJBRemote;
import com.claro.cpymes.ejb.remote.ProcessEJBRemote;
import com.claro.cpymes.enums.PriorityEnum;
import com.claro.cpymes.enums.ProcessEnum;
import com.claro.cpymes.util.Util;


/**
 * Controla la vista cpymes.xhtml
 * @author jbarragan
 *
 */
@ManagedBean(name = "cpymes")
@SessionScoped
public class CPYMESView {

   private static final int INTERVAL = 30;

   private static final String BUTTON_PRIORITY = "button_priority";

   private static final String BUTTON_PRIORITY_ACTIVE = "button_priority_active";

   private static Logger LOGGER = LogManager.getLogger(CPYMESView.class.getName());

   private ArrayList<AlarmPymesDTO> listAlarm;

   private ArrayList<AlarmPymesDTO> listAlarmSelect;

   @EJB
   private CPYMESEJBRemote cpymesEJB;

   @EJB
   private LogsDAORemote logsDAO;

   @EJB
   private ProcessEJBRemote processEJB;

   private String priorityAction;

   private ArrayList<String> listPrioritySelect;

   private ArrayList<String> prioritys;

   private boolean viewPause;

   private boolean viewPlay;

   private int interval;

   private PriorityCountDTO priorityCount;

   private String alertStyle;

   private String infoStyle;

   private String criticStyle;

   private String warningStyle;

   private String noticeStyle;

   @PostConstruct
   public void initial() {
      play();
      listAlarm = new ArrayList<AlarmPymesDTO>();
      listAlarmSelect = new ArrayList<AlarmPymesDTO>();
      listPrioritySelect = new ArrayList<String>();
      prioritys = new ArrayList<String>();
      load();
      initializePriority();
      initializePrioritys();
   }

   /**
    * Carga las alarmas disponibles de la base de datos
    * KOU, en estado NO PROCESADO
    */
   public void load() {
      try {
         listAlarm = cpymesEJB.loadAlarm();
         priorityCount = cpymesEJB.countAlarm(listAlarm);
         initializePriority();
         initializePrioritys();
      } catch (Exception e) {
         LOGGER.info("Erro cargando alarmas: ", e);
      }
   }

   /**
    * Inicializa los estados de los botones de
    * filtrado por prioridad
    */
   private void initializePriority() {
      alertStyle = BUTTON_PRIORITY;
      infoStyle = BUTTON_PRIORITY;
      criticStyle = BUTTON_PRIORITY;
      warningStyle = BUTTON_PRIORITY;
      noticeStyle = BUTTON_PRIORITY;
   }

   /**
    * Inicializa el listado del select de prioridades de filtrado
    */
   private void initializePrioritys() {
      listPrioritySelect = new ArrayList<String>();
      prioritys = new ArrayList<String>();
      prioritys.add(PriorityEnum.ALERT.getValue());
      prioritys.add(PriorityEnum.CRITIC.getValue());
      prioritys.add(PriorityEnum.INFO.getValue());
      prioritys.add(PriorityEnum.NOTICE.getValue());
      prioritys.add(PriorityEnum.WARNING.getValue());
   }

   /**
    * Reconoce las alarmas selecionadas por el usuario
    * Cambia el estado de la alarma a RECONOCIDO
    */
   public void reconocer() {
      if (listAlarmSelect.size() == 0) {
         Util.addMessageFatal("Debe selecionar por lo menos un registro para reconocer.");
         return;
      }
      for (AlarmPymesDTO alarmDTO : listAlarmSelect) {
         alarmDTO.setEstado(ProcessEnum.RECONOCIDO.getValue());
         alarmDTO.setDatetimeAcknowledge(new Date());
         try {
            cpymesEJB.update(alarmDTO);
         } catch (Exception e) {
            LOGGER.info("Error reconociendo alarma: ", e);
         }
      }
      load();
   }

   public void search() {

   }

   /**
    * Filtra las alarmas por prioridad
    */
   public void filterByPriority() {
      try {
         configureListPriority();
         if (listPrioritySelect.size() > 0) {
            listAlarm = cpymesEJB.loadAlarmByPriority(listPrioritySelect);
         } else {
            listAlarm = cpymesEJB.loadAlarm();
         }
      } catch (Exception e) {
         LOGGER.error("Error filtrando por prioridades", e);
      }
   }

   /**
    * Configura la lista de prioridades seleccionadas
    */
   private void configureListPriority() {
      boolean exits = false;
      changeStyle(priorityAction);
      for (String priority : listPrioritySelect) {
         if (priorityAction.equals(priority)) {
            exits = true;
         }
      }
      if (exits) {
         listPrioritySelect.remove(priorityAction);
      } else {
         listPrioritySelect.add(priorityAction);
      }
   }

   /**
    * Cambia los estilos del boton de prioridad seleccionadof
    * @param priority Value del boton a cambiar
    */
   private void changeStyle(String priority) {
      if (priority.equals(PriorityEnum.ALERT.getValue())) {
         alertStyle = alertStyle.equals(BUTTON_PRIORITY) ? BUTTON_PRIORITY_ACTIVE : BUTTON_PRIORITY;
      } else if (priority.equals(PriorityEnum.CRITIC.getValue())) {
         criticStyle = criticStyle.equals(BUTTON_PRIORITY) ? BUTTON_PRIORITY_ACTIVE : BUTTON_PRIORITY;
      } else if (priority.equals(PriorityEnum.INFO.getValue())) {
         infoStyle = infoStyle.equals(BUTTON_PRIORITY) ? BUTTON_PRIORITY_ACTIVE : BUTTON_PRIORITY;
      } else if (priority.equals(PriorityEnum.NOTICE.getValue())) {
         noticeStyle = noticeStyle.equals(BUTTON_PRIORITY) ? BUTTON_PRIORITY_ACTIVE : BUTTON_PRIORITY;
      } else if (priority.equals(PriorityEnum.WARNING.getValue())) {
         warningStyle = warningStyle.equals(BUTTON_PRIORITY) ? BUTTON_PRIORITY_ACTIVE : BUTTON_PRIORITY;
      }
   }

   /**
    * Limpia las pantalla, de las alarmas hasta el proximo recargue 
    * automatico o manual
    */
   public void clean() {
      listAlarm = new ArrayList<AlarmPymesDTO>();
      listAlarmSelect = new ArrayList<AlarmPymesDTO>();
   }

   /**
    * Pausa la ejecucion periodica de refresh
    */
   public void pause() {
      viewPlay = true;
      viewPause = false;
      interval = INTERVAL * 1000;
   }

   /**
    * Reanuda la ejecucion periodica de refresh
    */
   public void play() {
      viewPlay = false;
      viewPause = true;
      interval = INTERVAL;
   }

   public void filterPriority() {
      listAlarm = new ArrayList<AlarmPymesDTO>();
   }

   public ArrayList<AlarmPymesDTO> getListAlarm() {
      return listAlarm;
   }

   public void setListAlarm(ArrayList<AlarmPymesDTO> listAlarm) {
      this.listAlarm = listAlarm;
   }

   public ArrayList<AlarmPymesDTO> getListAlarmSelect() {
      return listAlarmSelect;
   }

   public void setListAlarmSelect(ArrayList<AlarmPymesDTO> listAlarmSelect) {
      this.listAlarmSelect = listAlarmSelect;
   }

   public boolean isViewPause() {
      return viewPause;
   }

   public void setViewPause(boolean viewPause) {
      this.viewPause = viewPause;
   }

   public boolean isViewPlay() {
      return viewPlay;
   }

   public void setViewPlay(boolean viewPlay) {
      this.viewPlay = viewPlay;
   }

   public int getInterval() {
      return interval;
   }

   public void setInterval(int interval) {
      this.interval = interval;
   }

   public PriorityCountDTO getPriorityCount() {
      return priorityCount;
   }

   public void setPriorityCount(PriorityCountDTO priorityCount) {
      this.priorityCount = priorityCount;
   }

   public void setPriorityAction(String priorityAction) {
      this.priorityAction = priorityAction;
   }

   public ArrayList<String> getListPrioritySelect() {
      return listPrioritySelect;
   }

   public void setListPrioritySelect(ArrayList<String> listPrioritySelect) {
      this.listPrioritySelect = listPrioritySelect;
   }

   public String getAlertStyle() {
      return alertStyle;
   }

   public void setAlertStyle(String alertStyle) {
      this.alertStyle = alertStyle;
   }

   public String getInfoStyle() {
      return infoStyle;
   }

   public void setInfoStyle(String infoStyle) {
      this.infoStyle = infoStyle;
   }

   public String getCriticStyle() {
      return criticStyle;
   }

   public void setCriticStyle(String criticStyle) {
      this.criticStyle = criticStyle;
   }

   public String getWarningStyle() {
      return warningStyle;
   }

   public void setWarningStyle(String warningStyle) {
      this.warningStyle = warningStyle;
   }

   public String getNoticeStyle() {
      return noticeStyle;
   }

   public void setNoticeStyle(String noticeStyle) {
      this.noticeStyle = noticeStyle;
   }

   public ArrayList<String> getPrioritys() {
      return prioritys;
   }

   public void setPrioritys(ArrayList<String> prioritys) {
      this.prioritys = prioritys;
   }

}
