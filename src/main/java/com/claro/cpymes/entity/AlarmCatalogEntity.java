package com.claro.cpymes.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the alarm_catalog database table.
 * 
 */
@Entity
@Table(name = "alarm_catalog")
@NamedQueries({ 
   @NamedQuery(name = "AlarmCatalogEntity.findAll", query = "SELECT a FROM AlarmCatalogEntity a"),
   @NamedQuery(name = "AlarmCatalogEntity.findByFilter", query = "SELECT a FROM AlarmCatalogEntity a WHERE a.filter=:filter") })
public class AlarmCatalogEntity implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;

   private String correlate;

   private String criticality;

   private String filter;

   private String manager;

   private String oid;

   private String severity;

   private String system;

   @Column(name = "text_alarm")
   private String textAlarm;

   public AlarmCatalogEntity() {
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getCorrelate() {
      return this.correlate;
   }

   public void setCorrelate(String correlate) {
      this.correlate = correlate;
   }

   public String getCriticality() {
      return this.criticality;
   }

   public void setCriticality(String criticality) {
      this.criticality = criticality;
   }

   public String getFilter() {
      return this.filter;
   }

   public void setFilter(String filter) {
      this.filter = filter;
   }

   public String getManager() {
      return this.manager;
   }

   public void setManager(String manager) {
      this.manager = manager;
   }

   public String getOid() {
      return this.oid;
   }

   public void setOid(String oid) {
      this.oid = oid;
   }

   public String getSeverity() {
      return this.severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

   public String getSystem() {
      return this.system;
   }

   public void setSystem(String system) {
      this.system = system;
   }

   public String getTextAlarm() {
      return this.textAlarm;
   }

   public void setTextAlarm(String textAlarm) {
      this.textAlarm = textAlarm;
   }

}