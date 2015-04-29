package com.claro.cpymes.ejb.remote;

import javax.ejb.Remote;


/**
 * Interface remota para ProcessEJBRemote
 * @author jbarragan
 *
 */
@Remote
public interface ProcessEJBRemote {

   public void procesar();

}
