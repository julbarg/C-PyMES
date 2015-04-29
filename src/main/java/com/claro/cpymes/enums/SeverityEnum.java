package com.claro.cpymes.enums;

public enum SeverityEnum {

   AS("AS", "Afectacion"), 
   NAS("NAS", "No afectacion"), 
   PAS("PAS", "Posible Afectacion"),
   NI("NI", "No Identificado");

   private String value;

   private String name;

   private SeverityEnum(String value, String name) {
      this.setValue(value);
      this.setName(name);
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

}
