package com.claro.cpymes.ejb.remote;

import javax.ejb.Remote;

import com.claro.cpymes.dto.UserDTO;


/**
 * Interface remota para LoginEJBRemote
 * @author jbarragan
 *
 */
@Remote
public interface LoginEJBRemote {

   public boolean authenticate(UserDTO user);

}
