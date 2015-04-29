package com.claro.cpymes.entity;

import java.io.Serializable;

import javax.persistence.*;

import java.sql.Time;
import java.util.Date;


/** The persistent class for the alarm_pymes database table. */
@Entity
@Table(name = "alarm_pymes")
@NamedQueries({
   @NamedQuery(name = "AlarmPymesEntity.findAll", query = "SELECT a FROM AlarmPymesEntity a"),
   @NamedQuery(name = "AlarmPymesEntity.findSimiliar", query = "SELECT a FROM AlarmPymesEntity a WHERE a.eventName=:eventName AND a.name=:name AND a.date BETWEEN :startDate AND :endDate"),
   @NamedQuery(name = "AlarmPymesEntity.findSimiliarCEP", query = "SELECT a FROM AlarmPymesEntity a WHERE a.nodo=:nodo AND a.estado=:estado AND a.nameCorrelation=:nameCorrelation AND a.date BETWEEN :startDate AND :endDate"),
   @NamedQuery(name = "AlarmPymesEntity.findSimiliarCEPByDate", query = "SELECT a FROM AlarmPymesEntity a WHERE a.nodo=:nodo AND a.estado=:estado AND a.nameCorrelation=:nameCorrelation AND a.date=:date"),
   @NamedQuery(name = "AlarmPymesEntity.findSimiliarCEPReconocidas", query = "SELECT a FROM AlarmPymesEntity a WHERE a.nodo IS NOT NULL AND a.estado=:estado AND a.nameCorrelation IS NOT NULL AND a.datetimeAcknowledge BETWEEN :startDate AND :endDate"),
   @NamedQuery(name = "AlarmPymesEntity.findByEstado", query = "SELECT a FROM AlarmPymesEntity a WHERE a.estado=:estado ORDER BY a.id DESC "),
   @NamedQuery(name = "AlarmPymesEntity.findByPriority", query = "SELECT a FROM AlarmPymesEntity a WHERE a.estado=:estado AND a.priority IN :listPriority ORDER BY a.id DESC ") })
public class AlarmPymesEntity implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private long id;

   @Column(name = "code_service")
   private String codeService;

   @Temporal(TemporalType.TIMESTAMP)
   private Date date;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "datetime_acknowledge")
   private Date datetimeAcknowledge;

   private String estado;

   @Column(name = "event_name")
   private String eventName;

   private String facility;

   private String ip;

   @Column(name = "ip_user_acknowledge")
   private String ipUserAcknowledge;

   private String level;

   @Lob
   private String message;

   private String name;

   @Column(name = "name_correlation")
   private String nameCorrelation;

   private String nodo;

   private String oid;

   private String priority;

   private String program;

   private String severity;

   private Time time;

   @Column(name = "user_acknowledge")
   private String userAcknowledge;

   public AlarmPymesEntity() {
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }

   public String getCodeService() {
      return this.codeService;
   }

   public void setCodeService(String codeService) {
      this.codeService = codeService;
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

   public String getEstado() {
      return this.estado;
   }

   public void setEstado(String estado) {
      this.estado = estado;
   }

   public String getEventName() {
      return this.eventName;
   }

   public void setEventName(String eventName) {
      this.eventName = eventName;
   }

   public String getFacility() {
      return this.facility;
   }

   public void setFacility(String facility) {
      this.facility = facility;
   }

   public String getIp() {
      return this.ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
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

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNameCorrelation() {
      return this.nameCorrelation;
   }

   public void setNameCorrelation(String nameCorrelation) {
      this.nameCorrelation = nameCorrelation;
   }

   public String getNodo() {
      return this.nodo;
   }

   public void setNodo(String nodo) {
      this.nodo = nodo;
   }

   public String getOid() {
      return this.oid;
   }

   public void setOid(String oid) {
      this.oid = oid;
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

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

   @Override
   public String toString() {
      String mensaje = "IP: " + ip + " Name: " + name + " EventName: " + eventName;
      return mensaje;
   }

}