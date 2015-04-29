package com.claro.cpymes.enums;

/**
 * ProcessEnum - Define los estados de los logs
 * @author jbarragan
 *
 */
public enum ProcessEnum {

   PROCESADO("S", "Procesado"), 
   NO_PROCESADO("N", "No Procesado"), 
   FILTRADO("F", "Filtrado"), 
   RECONOCIDO("R", "Reconocido"),
   ACTIVO("A", "Activo"), 
   DEFAULT("", "Default");

   private String name;

   private String value;

   private ProcessEnum(String value, String name) {
      this.setName(name);
      this.setValue(value);
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }

}
