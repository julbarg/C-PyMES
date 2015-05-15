package com.claro.cpymes.view;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.dto.UserDTO;
import com.claro.cpymes.ejb.remote.LoginEJBRemote;
import com.claro.cpymes.util.Messages;
import com.claro.cpymes.util.Util;


/**
 * Controla la vista login.xhtml
 * @author jbarragan
 *
 */
@ManagedBean(name = "loginView")
@SessionScoped
public class LoginView {

   private static Logger LOGGER = LogManager.getLogger(LoginView.class.getName());

   private UserDTO user;

   @PostConstruct
   public void initialize() {
      user = new UserDTO();
   }

   @EJB
   private LoginEJBRemote loginEJB;

   /**
    * Verifica autenticacion
    * @return Redireccion de pagina
    */
   public String authenticate() {
      try {
         LOGGER.info("AUTENTICAR");
         if (loginEJB.authenticate(user)) {
            return "/pages/cpymes";
         }
         return null;

      } catch (Exception e) {
         LOGGER.error(Messages.AUTHENTICATION_ERROR, e);
         Util.addMessageFatal(Messages.AUTHENTICATION_ERROR);
         return null;
      }

   }

   public UserDTO getUser() {
      return user;
   }

   public void setUser(UserDTO user) {
      this.user = user;
   }

}
