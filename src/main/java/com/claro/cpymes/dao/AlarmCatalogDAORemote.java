package com.claro.cpymes.dao;

import java.util.ArrayList;

import javax.ejb.Remote;

import com.claro.cpymes.entity.AlarmCatalogEntity;


@Remote
public interface AlarmCatalogDAORemote {

   public ArrayList<AlarmCatalogEntity> findByFilter(String filter) throws Exception;

}
