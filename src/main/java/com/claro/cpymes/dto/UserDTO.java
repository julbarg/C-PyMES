package com.claro.cpymes.dto;

import java.io.Serializable;


/**
 * UserDTO
 * @author jbarragan
 *
 */
public class UserDTO implements Serializable {

   /**
    * 
    */
   private static final long serialVersionUID = -8134728024247578499L;

   private String userName;

   private String password;

   public String getUserName() {
      return userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

}
