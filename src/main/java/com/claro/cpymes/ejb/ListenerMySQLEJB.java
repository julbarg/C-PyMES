package com.claro.cpymes.ejb;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.ejb.remote.ListenerMySQLEJBRemote;
import com.claro.cpymes.ejb.remote.ProcessEJBRemote;
import com.claro.cpymes.listener.Connection;
import com.claro.cpymes.listener.SNMPTTBinaryLogClient;
import com.claro.cpymes.listener.SQLOperator;
import com.claro.cpymes.util.Constant;
import com.claro.cpymes.util.Util;
import com.github.shyiko.mysql.binlog.BinaryLogClient.EventListener;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.event.QueryEventData;


/**
 * Bean Singleton Asincrono que inicia el Listener a BD MySQL configurada
 * Se encarga de lanzar un listener a la BD de KOU, ejecuta todo el proceso
 * de filtrado y correlacion cada vez que se presenta un evento en la base de
 * datos configurada (KOU)
 * Si se configuro el metodo listener
 * @author jbarragan
 *
 */
@Singleton
@Asynchronous
@LocalBean
public class ListenerMySQLEJB implements ListenerMySQLEJBRemote {

   private static Logger LOGGER = LogManager.getLogger(ListenerMySQLEJB.class.getName());

   private ArrayList<String> operacionesAEscuchar = new ArrayList<String>();

   @EJB
   private ProcessEJBRemote processEJBRemote;

   @PostConstruct
   public void inicializar() {
      operacionesAEscuchar.add(SQLOperator.INSERT.getOperation());
      inicializarListener();
   }

   /**
    * Inicia la conexion a la base de datos e inicia el listener
    */
   public void inicializarListener() {
      try {
         Connection conexion = new Connection(Constant.PATH_CONNECTION_LISTENER);
         SNMPTTBinaryLogClient client = new SNMPTTBinaryLogClient(conexion);
         client.registerEventListener(new EventListener() {

            // Este metodo se ejecuta cada vez que se dispara un envento en BD
            @Override
            public void onEvent(Event event) {
               if (Util.evaluarEvento(EventType.QUERY, event)) {
                  String sql = ((QueryEventData) event.getData()).getSql().toUpperCase();
                  if (Util.evaluarOperacion(sql, operacionesAEscuchar)) {
                     LOGGER.info("Event: " + event);
                     procesarDB();
                  }
               }

            }
         });
         LOGGER.info("Conexion ListenerMySQL Ready!");
         client.connect();
      } catch (final IOException e) {
         LOGGER.error("Error al intentar conectarse", e);
      }
   }

   /**
    * Ejecucion del proceso
    */
   public void procesarDB() {
      processEJBRemote.procesar();
   }

}
