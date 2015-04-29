package com.claro.cpymes.listener;

/**
 * Describe las operacion a tener en cuenta. Eventos de BD
 * @author jbarragan
 *
 */
public enum SQLOperator {

   INSERT("INSERT"), 
   UPDATE("UPDATE"), 
   DELETE("DELETE");

   private String operation;

   private SQLOperator(final String operation) {
      this.operation = operation;
   }

   public String getOperation() {
      return this.operation;
   }

}
