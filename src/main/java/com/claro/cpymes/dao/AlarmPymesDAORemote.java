package com.claro.cpymes.dao;

import java.util.ArrayList;
import java.util.Date;

import javax.ejb.Remote;

import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.entity.AlarmPymesEntity;


/**
 * Interface remota de AlarmPymesDAO
 * @author jbarragan
 *
 */
@Remote
public interface AlarmPymesDAORemote {

   public AlarmPymesEntity findOne(long id);

   public void create(AlarmPymesEntity entity);

   public AlarmPymesEntity update(AlarmPymesEntity entity);

   public void delete(AlarmPymesEntity entity);

   public void deleteById(long entityId);

   public ArrayList<AlarmPymesEntity> findByEstado(String estado);

   public ArrayList<AlarmPymesEntity> findSimiliar(String eventName, String name, Date startDate, Date endDate);

   public ArrayList<AlarmPymesEntity> findSimiliarCEP(String nodo, String nameCorrelation, Date startDate, Date endDate);

   public ArrayList<AlarmPymesEntity> findSimiliarCEP(String nodo, String nameCorrelation, Date date);

   public ArrayList<AlarmPymesEntity> findSimiliarCEPReconocidas(Date startDate, Date endDate);

   public ArrayList<AlarmPymesEntity> findByPriority(ArrayList<String> listPrioritySelect);

   public void createList(ArrayList<AlarmPymesEntity> listAlarm);

   public ArrayList<LogDTO> validateSimilar(ArrayList<LogDTO> listLogsDTO);

}
