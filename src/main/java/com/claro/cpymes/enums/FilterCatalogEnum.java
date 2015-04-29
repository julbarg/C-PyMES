package com.claro.cpymes.enums;

public enum FilterCatalogEnum {

   PYMES("PYMES");

   private String value;

   private FilterCatalogEnum(String value) {
      this.value = value;
   }

   public String getValue() {
      return value;
   }

}
