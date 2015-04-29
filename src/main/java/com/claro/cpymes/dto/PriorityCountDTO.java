package com.claro.cpymes.dto;

import java.io.Serializable;


/**
 * PriorityCountDTO . DTO para el conteo de alarmas
 * por prioridades
 * @author jbarragan
 *
 */
public class PriorityCountDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -335386748415755460L;

   private int alert;

   private int crit;

   private int info;

   private int notice;

   private int warning;

   public int getAlert() {
      return alert;
   }

   public void setAlert(int alert) {
      this.alert = alert;
   }

   public int getCrit() {
      return crit;
   }

   public void setCrit(int crit) {
      this.crit = crit;
   }

   public int getInfo() {
      return info;
   }

   public void setInfo(int info) {
      this.info = info;
   }

   public int getNotice() {
      return notice;
   }

   public void setNotice(int notice) {
      this.notice = notice;
   }

   public int getWarning() {
      return warning;
   }

   public void setWarning(int warning) {
      this.warning = warning;
   }

   @Override
   public String toString() {
      String message = "A-" + alert + " C-" + crit + " I-" + info + " N-" + notice + " W-" + warning;
      return message;
   }

}
