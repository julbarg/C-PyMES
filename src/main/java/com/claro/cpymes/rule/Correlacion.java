package com.claro.cpymes.rule;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.util.Constant;


/**
 * Clase se encarga de ejecutar las reglas de correlacio Drools
 * @author jbarragan
 *
 */
public class Correlacion {

   private static Logger LOGGER = LogManager.getLogger(Correlacion.class.getName());

   KnowledgeBase kbase;

   StatefulKnowledgeSession ksession;

   WorkingMemoryEntryPoint workingLogCorrelation;

   private final static String ENTRY_POINT_CORRELATION = "logs-correlation";

   public void initialize() throws Exception {
      kbase = Correlacion.readKnowledgeBase(Constant.PATH_DRL_FILE_CEP);
      ksession = kbase.newStatefulKnowledgeSession();workingLogCorrelation = ksession.getWorkingMemoryEntryPoint(ENTRY_POINT_CORRELATION);
      ksession.fireAllRules();
   }

   public LogDTO insertToEntryPoint(LogDTO log) {
      workingLogCorrelation.insert(log);
      ksession.fireAllRules();

      return log;
   }

   public void retract(String nodo, String nameCorrelation) {
      ArrayList<LogDTO> listLogs = getListLogsCorrelation();
      ArrayList<LogDTO> listDelete = new ArrayList<LogDTO>();
      for (LogDTO log : listLogs) {
         if (log.getNodo().equals(nodo) && log.getNameCorrelation().equals(nameCorrelation)
            && log.getContCorrelate() >= Constant.NUMBER_ALARMS_CORRELATE) {
            listDelete.add(log);
         }
      }
      for (LogDTO log : listDelete) {
         workingLogCorrelation.retract(workingLogCorrelation.getFactHandle(log));
      }
   }

   public ArrayList<LogDTO> getListLogsCorrelation() {
      ArrayList<LogDTO> listLogs = new ArrayList<LogDTO>();
      Collection<Object> collectionLogs = workingLogCorrelation.getObjects();
      for (Object obj : collectionLogs) {
         if (obj instanceof LogDTO) {
            listLogs.add((LogDTO) obj);
         }
      }
      return listLogs;
   }

   public int getSizeListLogsCorrelation() {
      Collection<Object> collectionLogs = workingLogCorrelation.getObjects();
      return collectionLogs.size();
   }

   private static KnowledgeBase readKnowledgeBase(String drlFile) throws Exception {
      KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
      config.setOption(EventProcessingOption.STREAM);

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
      KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);
      kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
      return kbase;
   }

}
