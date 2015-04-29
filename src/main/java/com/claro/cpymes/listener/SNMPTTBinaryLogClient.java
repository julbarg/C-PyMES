package com.claro.cpymes.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;


/**
 * BinaryLogCliente de Listener MySQL
 * @author jbarragan
 *
 */
public class SNMPTTBinaryLogClient extends BinaryLogClient {

   public SNMPTTBinaryLogClient(final Connection conexion) {
      super(conexion.getHostname(), conexion.getPort(), conexion.getUser(), conexion.getPassword());
   }

}
