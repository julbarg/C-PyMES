package com.claro.cpymes.dao;

import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.claro.cpymes.entity.AlarmCatalogEntity;
import com.claro.cpymes.util.Constant;


@Stateless
@LocalBean
public class AlarmCatalogDAO extends TemplateDAO<AlarmCatalogEntity> implements AlarmCatalogDAORemote {

   @Override
   @TransactionAttribute(TransactionAttributeType.REQUIRED)
   public ArrayList<AlarmCatalogEntity> findByFilter(String filter) throws Exception {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      TypedQuery<AlarmCatalogEntity> query = entityManager.createNamedQuery("AlarmCatalogEntity.findByFilter",
         AlarmCatalogEntity.class);
      query.setParameter("filter", filter);
      ArrayList<AlarmCatalogEntity> results = (ArrayList<AlarmCatalogEntity>) query.setMaxResults(
         Constant.MAXIME_RESULT_ALARM).getResultList();
      entityManager.close();

      return results;
   }

}
