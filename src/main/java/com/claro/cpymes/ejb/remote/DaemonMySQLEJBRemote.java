package com.claro.cpymes.ejb.remote;

import javax.ejb.Asynchronous;
import javax.ejb.Remote;


/**
 * Interfa remota para DaemonMySQLEJB
 * @author jbarragan
 *
 */
@Asynchronous
@Remote
public interface DaemonMySQLEJBRemote {

   public void inicializarDaemon(int timer);

}
