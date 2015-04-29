package com.claro.cpymes.dao;

import java.util.ArrayList;

import javax.ejb.Remote;

import com.claro.cpymes.entity.LogEntity;


/**
 * Interface remota de LogsDAO
 * @author jbarragan
 *
 */
@Remote
public interface LogsDAORemote {

   public LogEntity findOne(long id);

   public void create(LogEntity entity);

   public LogEntity update(LogEntity entity);

   public void delete(LogEntity entity);

   public void deleteById(long entityId);

   public ArrayList<LogEntity> findByEstado(String estado);
   
   public void updateList(ArrayList<LogEntity> listEntity);

}
