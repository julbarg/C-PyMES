package com.claro.cpymes.ejb;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.dto.KeyCatalogDTO;
import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.ejb.remote.LogUtilEJBRemote;
import com.claro.cpymes.entity.LogEntity;
import com.claro.cpymes.enums.PriorityEnum;
import com.claro.cpymes.util.Constant;
import com.claro.cpymes.util.FormatLogException;


@Stateless
@LocalBean
public class LogUtilEJB implements LogUtilEJBRemote {

   private static Logger LOGGER = LogManager.getLogger(LogUtilEJB.class.getName());

   private StringTokenizer tokenMain;

   private StringTokenizer tokenCodeServiceIp;

   @Override
   public LogDTO mapearLog(LogEntity logEntity) {
      LogDTO logDTO = new LogDTO();
      try {
         getMessage(logEntity);
         mapearLogDTO(logDTO);
         adicionarInformacionLogDTO(logEntity, logDTO);
      } catch (FormatLogException e) {

      }

      return logDTO;
   }

   private void getMessage(LogEntity logEntity) {
      String mensaje = logEntity.getMsg();
      tokenMain = new StringTokenizer(mensaje, Constant.DELIMETER_SNMPTT);

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
    * @param logDTO 
    * @param token
    * @return LogDTO mapeado
    * @throws FormatLogException 
    */
   private void mapearLogDTO(LogDTO logDTO) throws FormatLogException {
      if (tokenMain.countTokens() < 6) {
         throw new FormatLogException("Formato de Log KOU Invalido");
      }
      String codeServiceIp = validateNextTokenMain();
      tokenCodeServiceIp = new StringTokenizer(codeServiceIp, Constant.DELIMETER_IP);
      validateNextTokenCodeServiceIp().trim(); // codeService
      String ip = validateNextTokenCodeServiceIp().trim();
      if (ip.length() > 30) {
         ip = ip.substring(0, 29);
      }
      String name = validateNextTokenMain().trim();
      String translatedLine = validateNextTokenMain(); // translatedLine
      if (tokenMain.countTokens() > 6) {
         validateNextTokenMain(); // marca
      }
      validateNextTokenMain();
      procesarTranslatedLine(logDTO, translatedLine);
      logDTO.setTranslatedLine(translatedLine);
      String OID = validateNextTokenMain().trim();
      String nameEvent = validateNextTokenMain().trim();
      logDTO.setIp(ip);
      logDTO.setName(name);
      logDTO.setOID(OID);
      logDTO.setNameEvent(nameEvent);
      logDTO.setKey(new KeyCatalogDTO(OID, null));
      logDTO.setNodo(getNodo(name));
      logDTO.setMapeado(true);

   }

   private void adicionarInformacionLogDTO(LogEntity logEntity, LogDTO logDTO) {
      logDTO.setSeq(logEntity.getSeq());
      String priority = logEntity.getPriority();
      if (PriorityEnum.CRITIC.getValue().equals(priority)) {
         priority = PriorityEnum.CRITICAL.getValue();
      }
      logDTO.setPriority(priority);
      if (logDTO.getKey() != null) {
         logDTO.getKey().setCriticality(priority);
      }

   }

   /**
    * Obtiene el nodo a partir del NameDevice
    * @param name
    * @return Nodo
    */
   private String getNodo(String name) {
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

   private LogDTO procesarTranslatedLine(LogDTO logDTO, String translatedLine) {
      logDTO.setInterFace(getInterface(translatedLine));
      logDTO.setDescriptionAlarm(getDescripcionAlarma(translatedLine));

      return logDTO;
   }

   /**
    * Valida si existe un proximo token
    * @param token
    * @return Validacion
    */
   private String validateNextTokenCodeServiceIp() {
      try {
         String value = tokenCodeServiceIp.nextToken();
         return value != null ? value : "";
      } catch (Exception e) {
         LOGGER.error("Validando Tokenizer: " + e);
         return "";
      }

   }

   private String validateNextTokenMain() {
      try {
         String value = tokenMain.nextToken();
         return value != null ? value : "";
      } catch (Exception e) {
         LOGGER.error("Validando Tokenizer: " + e);
         return "";
      }

   }

   public String getInterface(String translatedLine) {
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
   public String getDescripcionAlarma(String translatedLine) {
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
