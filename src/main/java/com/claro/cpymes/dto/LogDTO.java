package com.claro.cpymes.dto;

import java.io.Serializable;
import java.util.Date;


/**
 * LogDTO - DTO que ayuda en el mapeo de los logs obtenidos
 * en el KOU
 * @author jbarragan
 *
 */
public class LogDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -7710710013852148814L;

   private int seq;

   private String priority;

   private String codeService;

   private String ip;

   private String name;

   private String nameDeviceCorrelate;

   private String translatedLine;

   private String marca;

   private String description;

   private String OID;

   private String nameEvent;

   private boolean relevant;

   private boolean correlation;

   private String messageDRL;

   private String descriptionAlarm;

   private String interFace;

   private int contCorrelate;

   private String nodo;

   private String nameCorrelation;

   private Date date;

   private boolean mapeado;

   private KeyCatalogDTO key;

   private String severity;

   public LogDTO() {

   }

   public LogDTO(String ip, String name, String OID, String nameEvent) {
      this.ip = ip;
      this.name = name;
      this.OID = OID;
      this.nameEvent = nameEvent;
   }

   public int getSeq() {
      return seq;
   }

   public void setSeq(int seq) {
      this.seq = seq;
   }

   public String getPriority() {
      return priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
   }

   public String getCodeService() {
      return codeService;
   }

   public void setCodeService(String codeService) {
      this.codeService = codeService;
   }

   public String getIp() {
      return ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getTranslatedLine() {
      return translatedLine;
   }

   public void setTranslatedLine(String translatedLine) {
      this.translatedLine = translatedLine;
   }

   public String getMarca() {
      return marca;
   }

   public void setMarca(String marca) {
      this.marca = marca;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getOID() {
      return OID;
   }

   public void setOID(String oID) {
      OID = oID;
   }

   public String getNameEvent() {
      return nameEvent;
   }

   public void setNameEvent(String nameEvent) {
      this.nameEvent = nameEvent;
   }

   public static long getSerialversionuid() {
      return serialVersionUID;
   }

   public boolean isRelevant() {
      return relevant;
   }

   public void setRelevant(boolean relevant) {
      this.relevant = relevant;
   }

   public String getMessageDRL() {
      return messageDRL;
   }

   public void setMessageDRL(String messageDRL) {
      this.messageDRL = messageDRL;
   }

   public String getDescriptionAlarm() {
      return descriptionAlarm;
   }

   public void setDescriptionAlarm(String descriptionAlarm) {
      this.descriptionAlarm = descriptionAlarm;
   }

   public String getInterFace() {
      return interFace != null ? interFace : "-";
   }

   public void setInterFace(String interFace) {
      this.interFace = interFace;
   }

   @Override
   public String toString() {
      String message = "IP: " + ip + " Name: " + name + " EventName: " + nameEvent + " Message" + messageDRL;
      return message;
   }

   public boolean isCorrelation() {
      return correlation;
   }

   public void setCorrelation(boolean correlation) {
      this.correlation = correlation;
   }

   public int getContCorrelate() {
      return contCorrelate;
   }

   public void setContCorrelate(int contCorrelate) {
      this.contCorrelate = contCorrelate;
   }

   public String getNodo() {
      return nodo;
   }

   public void setNodo(String nodo) {
      this.nodo = nodo;
   }

   public String getNameCorrelation() {
      return nameCorrelation;
   }

   public void setNameCorrelation(String nameCorrelation) {
      this.nameCorrelation = nameCorrelation;
   }

   public String getNameDeviceCorrelate() {
      return nameDeviceCorrelate;
   }

   public void setNameDeviceCorrelate(String nameDeviceCorrelate) {
      this.nameDeviceCorrelate = nameDeviceCorrelate;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public boolean isMapeado() {
      return mapeado;
   }

   public void setMapeado(boolean mapeado) {
      this.mapeado = mapeado;
   }

   public KeyCatalogDTO getKey() {
      return key;
   }

   public void setKey(KeyCatalogDTO key) {
      this.key = key;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

}
