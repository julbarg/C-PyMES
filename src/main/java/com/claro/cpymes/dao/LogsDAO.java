package com.claro.cpymes.dao;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

   /**
   * Obtiene las entidades LogEntity por estado
   * @param procesado Filtro con el que se realiza la consulta
   * @return ArrayList<LogEntity> Lista de entidades encontradas
   */
   @Override
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public ArrayList<LogEntity> findLogsNoProcesados() {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      TypedQuery<LogEntity> query = entityManager.createNamedQuery("LogEntity.findLogsNoProcesados", LogEntity.class);
      ArrayList<LogEntity> results = (ArrayList<LogEntity>) query.setMaxResults(Constant.MAXIME_RESULT_LOGS)
         .getResultList();
      entityManager.close();

      return results;

   }

   @Override
   public void updateList(ArrayList<LogEntity> listEntity) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      int i = 0;
      for (LogEntity log : listEntity) {
         Query query = entityManager.createQuery("UPDATE LogEntity e SET e.procesados = 'S' WHERE e.seq = :seq");
         query.setParameter("seq", log.getSeq());
         query.executeUpdate();
      }
      entityManager.getTransaction().commit();
      entityManager.close();
   }
}
