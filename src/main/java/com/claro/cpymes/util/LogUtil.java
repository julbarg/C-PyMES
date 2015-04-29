package com.claro.cpymes.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.dto.KeyCatalogDTO;
import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.entity.LogEntity;
import com.claro.cpymes.enums.PriorityEnum;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;


public class LogUtil {

   private static Logger LOGGER = LogManager.getLogger(LogUtil.class.getName());

   public static LogDTO mapearLogEntity(LogEntity logEntity) {
      String mensaje = logEntity.getMsg();
      StringTokenizer token = new StringTokenizer(mensaje, Constant.DELIMETER_SNMPTT);

      LogDTO logDTO = mapearLogDTO(token, mensaje);
      logDTO.setSeq(logEntity.getSeq());
      logDTO.setPriority(logEntity.getPriority());
      if (logDTO.getKey() != null) {
         String priority = logEntity.getPriority();
         if (PriorityEnum.CRITIC.getValue().equals(priority))
            priority = PriorityEnum.CRITICAL.getValue();
         logDTO.getKey().setCriticality(priority);
      }

      return logDTO;

   }

   /* 'TFM0775 - httpd: ZTE 172.30.15.195 |
    * ZAC-BOG.TRIARA-CP2 |
    * Niveles estables en la ventana de operacion de potencia optica OLT Cliente 43 P000889 - RAE0003-4$ |
    * |
    * |
    * .1.3.6.1.4.1.3902.1012.3.45.106 |
    * zxGponOltDOWiRestore' */
   /**
    * Metodo encargado de mapear la informacion de los logs
    * Obtiene la IP, NameDevice, NameEvent, OID, Nodo
    * @param token
    * @return LogDTO mapeado
    */
   private static LogDTO mapearLogDTO(StringTokenizer token, String mensaje) {
      int count = token.countTokens();

      if (count < 6) {
         LOGGER.error("TranslatedLine no cumple el formato");
         LOGGER.error("MESSAGE: " + mensaje);
         return new LogDTO();
      }

      String codeServiceIp = validateSplit(token);
      StringTokenizer token2 = new StringTokenizer(codeServiceIp, Constant.DELIMETER_IP);

      validateSplit(token2).trim(); // codeService
      String ip = validateSplit(token2).trim();
      if (ip.length() > 30) {
         ip = ip.substring(0, 29);
      }

      String name = validateSplit(token).trim();
      String translatedLine = validateSplit(token); // translatedLine

      if (count > 6) {
         validateSplit(token); // marca
      }

      validateSplit(token);
      LogDTO logDTO = procesarTranslatedLine(translatedLine);

      logDTO.setTranslatedLine(translatedLine);

      String OID = validateSplit(token).trim();
      String nameEvent = validateSplit(token).trim();

      logDTO.setIp(ip);
      logDTO.setName(name);
      logDTO.setOID(OID);
      logDTO.setNameEvent(nameEvent);

      logDTO.setKey(new KeyCatalogDTO(OID, null));

      logDTO.setNodo(getNodo(name));

      logDTO.setMapeado(true);

      return logDTO;
   }

   /**
    * Obtiene el nodo a partir del NameDevice
    * @param name
    * @return Nodo
    */
   private static String getNodo(String name) {
      String expresion = "[.][A-Z_0-9]+[-]";
      String nodo = "";

      Pattern pattern = Pattern.compile(expresion);
      Matcher matcher = pattern.matcher(name);

      if (matcher.find()) {
         nodo = matcher.group();
         nodo = nodo.replace("-", "");
         nodo = nodo.replace(".", "");
      }
      return nodo;
   }

   private static LogDTO procesarTranslatedLine(String translatedLine) {
      LogDTO logDTO = new LogDTO();
      logDTO.setInterFace(getInterface(translatedLine));
      logDTO.setDescriptionAlarm(getDescripcionAlarma(translatedLine));

      return logDTO;
   }

   /**
    * Valida si existe un proximo token
    * @param token
    * @return Validacion
    */
   private static String validateSplit(StringTokenizer token) {
      try {
         String value = token.nextToken();
         return value != null ? value : "";
      } catch (Exception e) {
         LOGGER.error("Validando Tokenizer: " + e);
         return "";
      }

   }

   /**
    * Evalua las operaciones a escuchar del Listener MySQL
    * @param sql
    * @param operacionesAEscuchar
    * @return Resultado de la Evaluacion
    */
   public static boolean evaluarOperacion(String sql, ArrayList<String> operacionesAEscuchar) {
      for (String operacion : operacionesAEscuchar) {
         int index = sql.indexOf(operacion);
         if (index != -1) {
            return true;
         }
      }
      return false;
   }

   /**
    * Evalua la equivalencia de eventos configurados y
    * el evento presentado
    * @param eventType
    * @param event
    * @return Resultado de la evaluacion
    */
   public static boolean evaluarEvento(EventType eventType, Event event) {
      return eventType.equals(event.getHeader().getEventType());
   }

   public static String getInterface(String translatedLine) {
      Pattern pattern = Pattern.compile(Constant.REGEX_INTERFACE);
      Matcher matcher = pattern.matcher(translatedLine);

      if (matcher.find()) {
         return matcher.group();
      } else {
         return "";
      }
   }

   /**
    * Obtien la descripcion apartir de translatedLine
    * @param translatedLine
    * @return Descripcion de Alarma
    */
   public static String getDescripcionAlarma(String translatedLine) {
      String descripcionAlarma = "";
      try {
         descripcionAlarma = translatedLine.replaceAll(Constant.REGEX_INTERFACE, "");
         descripcionAlarma = descripcionAlarma.replaceAll(Constant.REGEX_LOWECASE, "");
         descripcionAlarma = descripcionAlarma.replaceAll(Constant.REGEX_UPPERCASECASE_ALONE, "");
         descripcionAlarma = descripcionAlarma.replaceAll(Constant.REGEX_UPPERCASECASE_ALONE_2, "");
         descripcionAlarma = descripcionAlarma.replaceAll(Constant.REGEX_GUION, "");
         descripcionAlarma = descripcionAlarma.replaceAll(Constant.REGEX_WHITESPACE_INIT, "");
         descripcionAlarma = descripcionAlarma.replaceAll(Constant.REGEX_WHITESPACE, " ");

         return descripcionAlarma;
      } catch (Exception e) {
         return descripcionAlarma;
      }
   }

}
