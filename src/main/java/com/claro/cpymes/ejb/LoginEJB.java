package com.claro.cpymes.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import com.claro.cpymes.dto.UserDTO;
import com.claro.cpymes.ejb.remote.LoginEJBRemote;


/**
 * Bean que controla la autenticacion al Sistema
 * @author jbarragan
 *
 */
@Stateless
@LocalBean
public class LoginEJB implements LoginEJBRemote {

   @Override
   public boolean authenticate(UserDTO user) {
      return true;
   }

}
