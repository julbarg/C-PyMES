package com.claro.cpymes.ejb;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.ejb.remote.DaemonMySQLEJBRemote;
import com.claro.cpymes.ejb.remote.ProcessEJBRemote;


/**
 * Bean Singleton Asyncrono que inicia el Daemon.
 * Se encarga de ejecutar periodicamente el proceso
 * de filtrado y correlacion segun el tiempo configurado para ello
 * Si se ha configurado la opcion dameon
 * @author jbarragan
 *
 */
@Singleton
@Asynchronous
@LocalBean
public class DaemonMySQLEJB implements DaemonMySQLEJBRemote {

   private static Logger LOGGER = LogManager.getLogger(DaemonMySQLEJB.class.getName());

   private boolean procesar;

   @EJB
   private ProcessEJBRemote processEJBRemote;

   @PostConstruct
   public void inicializar() {
      procesar = true;
   }

   /**
    * Inicalizador del daemon
    * @param timer Periodo de tiempo en el cual se ejecutara el proceso
    */
   public void inicializarDaemon(int timer) {
      try {
         LOGGER.info("Inicio Daemon");
         while (procesar) {
            procesarDB();
            Thread.sleep(timer);
         }

      } catch (final Exception e) {
         LOGGER.error("Error en el Daemon", e);
      }
   }

   /**
    * Ejecucion del proceso
    */
   private void procesarDB() {
      processEJBRemote.procesar();
   }

   @PreDestroy
   public void clear() {
      procesar = false;
   }

}
