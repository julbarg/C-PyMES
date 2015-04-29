package com.claro.cpymes.rule;

import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import com.claro.cpymes.dto.KeyCatalogDTO;
import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.entity.AlarmCatalogEntity;
import com.claro.cpymes.util.Constant;


/**
 * Clase que ejecuta las reglas de filtrado de Drools
 * @author jbarragan
 *
 */
public class Filtrado {

   private static Logger LOGGER = LogManager.getLogger(Filtrado.class.getName());

   KnowledgeBase kbase;

   StatefulKnowledgeSession ksession;

   public void initialize(HashMap<KeyCatalogDTO, AlarmCatalogEntity> catalog) throws Exception {
      kbase = Filtrado.readKnowledgeBase(Constant.PATH_DRL_FILE);
      ksession = kbase.newStatefulKnowledgeSession();
      ksession.setGlobal("catalog", catalog);
   }

   public LogDTO filtrar(LogDTO log) {
      ksession.insert(log);
      ksession.fireAllRules();

      return log;
   }

   private static KnowledgeBase readKnowledgeBase(String drlFile) throws Exception {
      FileInputStream fis = new FileInputStream(drlFile);

      KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
      kbuilder.add(ResourceFactory.newInputStreamResource(fis), ResourceType.DRL);
      KnowledgeBuilderErrors errors = kbuilder.getErrors();
      if (errors.size() > 0) {
         for (KnowledgeBuilderError error : errors) {
            LOGGER.info(error);
         }
         throw new IllegalArgumentException("Could not parse knowledge.");
      }
      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
      kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
      return kbase;

   }

}
