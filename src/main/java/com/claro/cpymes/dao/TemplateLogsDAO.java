package com.claro.cpymes.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;


/**
 * TemplateDAO - Define los metodos genericos en entidades
 * de la base de datos CPyMES
 * @author jbarragan
 *
 * @param <T>
 */
public class TemplateLogsDAO<T> {

   private Class<T> clase;

   @PersistenceUnit(unitName = "LogsPU")
   public EntityManagerFactory entityManagerFactory;

   public final void setClase(Class<T> entity) {
      this.clase = entity;
   }

   public T findOne(long id) {

      EntityManager entityManager = entityManagerFactory.createEntityManager();
      T t = entityManager.find(this.clase, id);
      entityManager.close();

      return t;
   }

   public void create(T entity) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      EntityTransaction entityTransaction = entityManager.getTransaction();
      entityTransaction.begin();
      entityManager.persist(entity);
      entityTransaction.commit();
      entityManager.close();
   }

   public T update(T entity) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      T t = entityManager.merge(entity);
      entityManager.getTransaction().commit();
      entityManager.close();

      return t;
   }

   public void delete(T entity) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
      entityManager.getTransaction().commit();
      entityManager.close();
   }

   public void deleteById(long entityId) {
      EntityManager entityManager = entityManagerFactory.createEntityManager();
      entityManager.getTransaction().begin();
      T entity = this.findOne(entityId);
      this.delete(entity);
      entityManager.getTransaction().commit();
      entityManager.close();

   }

}
