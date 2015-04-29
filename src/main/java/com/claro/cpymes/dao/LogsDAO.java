package com.claro.cpymes.dao;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.claro.cpymes.entity.LogEntity;
import com.claro.cpymes.util.Constant;


/**
 * LogsDAO - DAO que controla las transaciones a base 
 * de datos de la entidad LogEntity
 * @author jbarragan
 *
 */
@Stateless
@LocalBean
public class LogsDAO extends TemplateLogsDAO<LogEntity> implements LogsDAORemote {

   private static Logger LOGGER = LogManager.getLogger(LogsDAO.class.getName());

   /**
    * Obtiene las entidades LogEntity por estado
    * @param procesado Filtro con el que se realiza la consulta
    * @return ArrayList<LogEntity> Lista de entidades encontradas
    */
   @Override
   public ArrayList<LogEntity> findByEstado(String procesado) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      TypedQuery<LogEntity> query = entityManager.createNamedQuery("LogEntity.findByProcesado", LogEntity.class);
      query.setParameter("procesados", procesado);
      ;
      ArrayList<LogEntity> results = (ArrayList<LogEntity>) query.setMaxResults(Constant.MAXIME_RESULT_LOGS).getResultList();
      entityManager.getTransaction().commit();
      entityManager.close();

      return results;

   }

   /**
    * Persiste la entidad
    * @param entity Entidad a persistir
    */
   @Override
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public LogEntity update(LogEntity entity) {
      LogEntity logEntity = new LogEntity();
      try {
         logEntity = super.update(entity);
      } catch (Exception e) {
         LOGGER.error("Error actualizando registro: " + e);
      }

      return logEntity;
   }

   @Override
   public void updateList(ArrayList<LogEntity> listEntity) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      for (LogEntity log : listEntity) {
         entityManager.merge(log);
      }
      entityManager.getTransaction().commit();
      entityManager.close();
   }

}
