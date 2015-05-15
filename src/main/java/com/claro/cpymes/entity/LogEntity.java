package com.claro.cpymes.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Time;
import java.util.Date;


/** The persistent class for the logs database table. */
@Entity
@Table(name = "logs")
@NamedQueries({
   @NamedQuery(name = "LogEntity.findAll", query = "SELECT l FROM LogEntity l"),
   @NamedQuery(name = "LogEntity.findLogsNoProcesados", query = "SELECT l FROM LogEntity l WHERE l.procesados IS NULL ORDER BY l.seq ASC") })
public class LogEntity implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int seq;

   @Temporal(TemporalType.DATE)
   private Date date;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "datetime_acknowledge")
   private Date datetimeAcknowledge;

   private String facility;

   private String host;

   @Column(name = "ip_user_acknowledge")
   private String ipUserAcknowledge;

   private String level;

   @Lob
   private String msg;

   private String priority;

   private String procesados;

   private String program;

   private String tag;

   private Time time;

   @Column(name = "user_acknowledge")
   private String userAcknowledge;

   public LogEntity() {
   }

   public int getSeq() {
      return this.seq;
   }

   public void setSeq(int seq) {
      this.seq = seq;
   }

   public Date getDate() {
      return this.date;
   }

   public void setDate(Date date) {
      this.date = date;
   }

   public Date getDatetimeAcknowledge() {
      return this.datetimeAcknowledge;
   }

   public void setDatetimeAcknowledge(Date datetimeAcknowledge) {
      this.datetimeAcknowledge = datetimeAcknowledge;
   }

   public String getFacility() {
      return this.facility;
   }

   public void setFacility(String facility) {
      this.facility = facility;
   }

   public String getHost() {
      return this.host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public String getIpUserAcknowledge() {
      return this.ipUserAcknowledge;
   }

   public void setIpUserAcknowledge(String ipUserAcknowledge) {
      this.ipUserAcknowledge = ipUserAcknowledge;
   }

   public String getLevel() {
      return this.level;
   }

   public void setLevel(String level) {
      this.level = level;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   public String getPriority() {
      return this.priority;
   }

   public void setPriority(String priority) {
      this.priority = priority;
   }

   public String getProgram() {
      return this.program;
   }

   public void setProgram(String program) {
      this.program = program;
   }

   public String getTag() {
      return this.tag;
   }

   public void setTag(String tag) {
      this.tag = tag;
   }

   public Time getTime() {
      return this.time;
   }

   public void setTime(Time time) {
      this.time = time;
   }

   public String getUserAcknowledge() {
      return this.userAcknowledge;
   }

   public void setUserAcknowledge(String userAcknowledge) {
      this.userAcknowledge = userAcknowledge;
   }

   public String getProcesados() {
      return procesados;
   }

   public void setProcesados(String procesados) {
      this.procesados = procesados;
   }

   @Override
   public String toString() {
      String message = "ID: " + this.seq + " Procesado: " + this.procesados;
      return message;
   }

}