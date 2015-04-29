package com.claro.cpymes.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.ejb.remote.DaemonMySQLEJBRemote;
import com.claro.cpymes.ejb.remote.ListenerMySQLEJBRemote;
import com.claro.cpymes.util.Constant;
import com.claro.cpymes.util.Util;


/**
 * Bean Singleton StartUp 
 * Se ejecuta cuando se inicia la aplicacion
 * Es el encargado de ejecutar los Bean de proceso de 
 * acuerdo al metodo configurado
 * @author jbarragan
 *
 */
@Singleton
@Startup
public class MainEJB {

   private static Logger LOGGER = LogManager.getLogger(MainEJB.class.getName());

   @EJB
   private ListenerMySQLEJBRemote listenerMySQLEJBRemote;

   @EJB
   private DaemonMySQLEJBRemote daemonMySQLEJBRemote;

   /**
    * Inicia el proceso de chequeo dependiendo del metodo configurado
    * daemon - listener
    */
   @PostConstruct
   @Asynchronous
   public void init() {

      String method = Util.getProperties(Constant.METHOD);
      if (method.equals(Constant.METHOD_DAEMON)) {
         LOGGER.info("CPyMES Iniciado con el metodo: " + Constant.METHOD_DAEMON);
         daemonMySQLEJBRemote.inicializarDaemon(Constant.TIMER_DAEMON);

      } else if (method.equals(Constant.METHOD_LISTENER)) {
         LOGGER.info("CPyMES Iniciado con el metodo: " + Constant.METHOD_LISTENER);
         listenerMySQLEJBRemote.inicializarListener();

      } else {
         LOGGER.error("Metodo de Ejecución no encontrado");
         LOGGER.info("Revisar el archivo CPyMES.properties: " + Constant.PATH_CONFIG_PROPERTIES + ". Propiedad: "
            + Constant.METHOD);
         LOGGER.info("Valores permitidos: " + Constant.METHOD_DAEMON + ", " + Constant.METHOD_LISTENER);
      }

   }
}
