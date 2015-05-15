package com.claro.cpymes.ejb.remote;

import javax.ejb.Remote;

import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.entity.LogEntity;


@Remote
public interface LogUtilEJBRemote {

   LogDTO mapearLog(LogEntity logEntity);

}
