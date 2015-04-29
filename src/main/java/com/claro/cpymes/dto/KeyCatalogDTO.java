package com.claro.cpymes.dto;

import java.io.Serializable;


public class KeyCatalogDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -8937070334700688107L;

   private String OID;

   private String criticality;

   public String getOID() {
      return OID;
   }

   public void setOID(String OID) {
      this.OID = OID;
   }

   public String getCriticality() {
      return criticality;
   }

   public void setCriticality(String criticality) {
      this.criticality = criticality;
   }

   public KeyCatalogDTO(String OID, String criticality) {
      this.OID = OID;
      this.criticality = criticality;
   }

   @Override
   public String toString() {
      String msg = OID + " - " + criticality;
      return msg;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj != null && obj instanceof KeyCatalogDTO) {
         KeyCatalogDTO key = (KeyCatalogDTO) obj;
         try {
            return OID.toUpperCase().equals(key.OID.toUpperCase())
               && criticality.toUpperCase().equals(key.criticality.toUpperCase());
         } catch (Exception e) {
            return false;
         }
      }
      return false;
   }

   @Override
   public int hashCode() {
      return (OID + criticality).hashCode();
   }
}
