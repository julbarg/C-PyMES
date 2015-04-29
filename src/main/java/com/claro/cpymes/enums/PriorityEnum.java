package com.claro.cpymes.enums;

/**
 * Tipos de prioridades en alarmas
 * @author jbarragan
 *
 */
public enum PriorityEnum {

   ALERT("alert"), 
   CRITIC("crit"), 
   CRITICAL("critic"),
   INFO("info"), 
   NOTICE("notice"), 
   WARNING("warning");

   private String value;

   private PriorityEnum(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

}
