package com.claro.cpymes.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * Define las propiedades para establecer conexion a BD Listener MySQL
 * @author jbarragan
 *
 */
public class Connection {

   private static Logger LOGGER = LogManager.getLogger(Connection.class.getName());

   private static final String HOSTNAME = "hostname";

   private static final String PORT = "port";

   private static final String USER_NAME = "username";

   private static final String PASSWORD = "password";

   private String hostname;

   private int port;

   private String user;

   private String password;

   /**
    * Establece propiedades
    * @param configProperties Archivo de propiedades donde lee la informacion
    */
   public Connection(final String configProperties) {
      final Properties prop = new Properties();
      InputStream input = null;

      try {

         input = new FileInputStream(configProperties);
         prop.load(input);
         this.hostname = prop.getProperty(Connection.HOSTNAME);
         this.port = Integer.parseInt(prop.getProperty(Connection.PORT));
         this.user = prop.getProperty(Connection.USER_NAME);
         this.password = prop.getProperty(Connection.PASSWORD);

      } catch (final IOException ex) {
         LOGGER.error("Connection", ex);
      } finally {
         if (input != null) {
            try {
               input.close();
            } catch (final IOException e) {
               LOGGER.error("Connection", e);
            }
         }

      }
   }

   public String getHostname() {
      return this.hostname;
   }

   public void setHostname(final String hostname) {
      this.hostname = hostname;
   }

   public int getPort() {
      return this.port;
   }

   public void setPort(final int port) {
      this.port = port;
   }

   public String getUser() {
      return this.user;
   }

   public void setUser(final String user) {
      this.user = user;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(final String password) {
      this.password = password;
   }

   @Override
   public String toString() {
      String result = this.hostname + " " + this.port + " " + this.user + " " + this.password;
      return result;
   }

}
