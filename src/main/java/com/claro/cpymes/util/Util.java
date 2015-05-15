package com.claro.cpymes.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.listener.Connection;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;


/**
 * Util para el manejo grafico de la aplicacion
 * @author jbarragan
 *
 */
public class Util {

   private static Logger LOGGER = LogManager.getLogger(Connection.class.getName());

   /**
    * Adicionar Fatal Message a la vista
    * @param fatalMsg
    */
   public static void addMessageFatal(String fatalMsg) {
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_FATAL, fatalMsg, null);
      FacesContext.getCurrentInstance().addMessage(null, message);
   }

   /**
    * Obtener propertie
    * @param propertie
    * @return Propertie
    */
   public static String getProperties(String propertie) {

      final Properties prop = new Properties();
      InputStream input = null;
      try {
         input = new FileInputStream(Constant.PATH_CONFIG_PROPERTIES);
         prop.load(input);
         return prop.getProperty(propertie);
      } catch (final IOException ex) {
         LOGGER.error("Error getProperties", ex);
         return propertie;
      } finally {
         if (input != null) {
            try {
               input.close();
            } catch (final IOException e) {
               LOGGER.error("Error getProperties", e);
            }
         }
      }

   }

   /**
    * Obtener propertie entero
    * @param propertie
    * @return Propertie Int
    */
   public static int getPropertiesInt(String propertie) {

      final Properties prop = new Properties();
      InputStream input = null;
      int propertieInt = 0;
      try {
         input = new FileInputStream(Constant.PATH_CONFIG_PROPERTIES);
         prop.load(input);
         String propertieString = prop.getProperty(propertie);
         propertieInt = Integer.parseInt(propertieString);
         return propertieInt;

      } catch (final IOException ex) {
         LOGGER.error("Error getPropertiesInt", ex);
         return propertieInt;
      } finally {
         if (input != null) {
            try {
               input.close();
            } catch (final IOException e) {
               LOGGER.error("getPropertiesInt", e);
            }
         }
      }

   }

   /**
    * Restarle a fecha minutos
    * @param date
    * @param minutes
    * @return Date restada
    */
   public static Date restarFecha(Date date, int minutes) {
      long ONE_MINUTE_IN_MILLIS = 60000;
      long newFechaL = date.getTime() - (minutes * ONE_MINUTE_IN_MILLIS);
      Date newFecha = new Date(newFechaL);

      return newFecha;
   }

   /**
    * Evalua la equivalencia de eventos configurados y
    * el evento presentado
    * @param eventType
    * @param event
    * @return Resultado de la evaluacion
    */
   public static boolean evaluarEvento(EventType eventType, Event event) {
      return eventType.equals(event.getHeader().getEventType());
   }

   /**
    * Evalua las operaciones a escuchar del Listener MySQL
    * @param sql
    * @param operacionesAEscuchar
    * @return Resultado de la Evaluacion
    */
   public static boolean evaluarOperacion(String sql, ArrayList<String> operacionesAEscuchar) {
      for (String operacion : operacionesAEscuchar) {
         int index = sql.indexOf(operacion);
         if (index != -1) {
            return true;
         }
      }
      return false;
   }

}
