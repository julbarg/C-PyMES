package com.claro.cpymes.ejb.remote;

import javax.ejb.Asynchronous;
import javax.ejb.Remote;


/**
 * Interfa remota para ListenerMySQLEJB
 * @author jbarragan
 *
 */
@Remote
@Asynchronous
public interface ListenerMySQLEJBRemote {

   public void inicializarListener();

}
