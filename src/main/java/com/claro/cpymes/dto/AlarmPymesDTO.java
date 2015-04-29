package com.claro.cpymes.dto;

import java.io.Serializable;
import java.util.Date;


/**
 * AlarmPymesDTO - DTO que ayuda a mapear las alarmas
 * de la base de datos CPyMES
 * @author jbarragan
 *
 */
public class AlarmPymesDTO implements Serializable {
   /**
    * 
    */
   private static final long serialVersionUID = -5336056317461707193L;

   private long id;

   private String priority;

   private String ip;

   private String name;

   private String nameCorrelation;

   private String oid;

   private String nameEvent;

   private String message;

   private String estado;

   private Date date;

   private Date datetimeAcknowledge;

   private String nodo;
   
   private String severity;

   public AlarmPymesDTO() {

   }

   public AlarmPymesDTO(String priority, String ip, String name, String oid, String nameEvent) {
      this.priority = priority;
      this.ip = ip;
      this.name = name;
      this.oid = oid;
      this.nameEvent = nameEvent;
   }

   public long getId() {
      return id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getPriority() {
      return priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
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

   public String getOid() {
      return oid;
   }

   public void setOid(String oid) {
      this.oid = oid;
   }

   public String getNameEvent() {
      return nameEvent;
   }

   public void setNameEvent(String nameEvent) {
      this.nameEvent = nameEvent;
   }

   public String getNameCorrelation() {
      return nameCorrelation;
   }

   public void setNameCorrelation(String nameCorrelation) {
      this.nameCorrelation = nameCorrelation;
   }

   public String getEstado() {
      return estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public Date getDate() {
      return date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Date getDatetimeAcknowledge() {
      return datetimeAcknowledge;
   }

   public void setDatetimeAcknowledge(Date datetimeAcknowledge) {
      this.datetimeAcknowledge = datetimeAcknowledge;
   }

   public String getNodo() {
      return nodo;
   }

   public void setNodo(String nodo) {
      this.nodo = nodo;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

}
