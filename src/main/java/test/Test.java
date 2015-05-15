package test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.claro.cpymes.dto.KeyCatalogDTO;
import com.claro.cpymes.dto.LogDTO;
import com.claro.cpymes.entity.AlarmCatalogEntity;
import com.claro.cpymes.entity.LogEntity;
import com.claro.cpymes.util.Util;


public class Test {

   public static void main(String[] args) throws IOException {

      KeyCatalogDTO key = new KeyCatalogDTO();
      System.out.println("Key...." + key.isEmpty());

      char space = 32;
      String delimiter = "|";

      String message = "TFM0775 - httpd: ZTE 172.30.110.69 | ZAC-MAN.MANIZALES-CP1 | 268566784 2 |  |  | .1.3.6.1.4.1.3902.1012.3.45.200 | zxGponOnuLoidAuthNotification";

      message = "TFM0775 - httpd: ZTE 172.30.98.9 | ZAC-BUC.RICAURTE-CP1 | Caida de potencia optica en OLT cliente : CENTRO P001984-CDAYE02 |  |  | .1.3.6.1.4.1.3902.1012.3.45.109 | ";
      message = "TFM0775 - httpd: HUAWEI 172.30.7.247 | HAC-BOG.TOBERIN-CP1 | Se ha recuperado el LOS del puerto FE 0/3/40 - SENA P000462 - SLE1646 |  | .1.3.6.1.4.1.2011.5.14.10.2.0.20 | hwAlarmOpticalPortRecoverFromLOS";
      StringTokenizer tokenizer = new StringTokenizer(message, delimiter);
      String[] spliter = message.split("|");
      System.out.println("Space: " + space);
      for (String splite : spliter) {
         System.out.println("Spliter:" + splite);
      }

      int i = 0;
      while (tokenizer.hasMoreElements()) {
         System.out.println(i + " Tokenizer: " + tokenizer.nextToken());
         i++;
      }

      delimiter = " : ";
      message = "Se reestablece potencia optica en cliente : IMPACT AND P000882 - IAL0019";
      spliter = message.split(delimiter);
      for (String splite : spliter) {
         System.out.println("S Interface - Desc= " + splite);
      }
      tokenizer = new StringTokenizer(message, delimiter);
      i = 0;
      while (tokenizer.hasMoreElements()) {
         System.out.println(i + " Tokenizer: " + tokenizer.nextToken());
         i++;
      }
      String expresion = "[0-9]{1,3}/[0-9]{1,3}/[0-9]{1,3}";
      message = "El puerto FE 00/3/65 ha subido - CRISTHIAN CAMI P000862 - FYP0001";
      spliter = message.split(expresion);
      for (String splite : spliter) {
         System.out.println("E Interface - Desc= " + splite);
      }

      Pattern pattern = Pattern.compile(expresion);
      Matcher matcher = pattern.matcher(message);

      boolean found = false;
      int sub = 0;
      while (matcher.find()) {
         System.out.println("Interface: " + matcher.group());
         System.out.println("Start: " + matcher.start());
         sub = matcher.end();
         System.out.println("Message: " + message);
         System.out.println("SubMessage: " + message.substring(sub));
         found = true;
      }
      if (!found) {
         System.out.println("No match found.%n");
      }
      System.out.println(".......................");
      expresion = "[a-z]+";
      message = " ha subido - CRISTHIAN CAMI P000862 - FYP0001";
      message = " esta Down revisar CODESS - GVI0016 ";
      String descripcion = message.replaceAll(expresion, "");
      System.out.println("Descripcion: " + descripcion);

      pattern = Pattern.compile(expresion);
      matcher = pattern.matcher(message);

      found = false;
      sub = 0;
      while (matcher.find()) {
         System.out.println("Interface: " + matcher.group());
         message = message.replaceAll(matcher.group(), "");
      }
      System.out.println("Message: " + message);

      System.out.println(".......................");
      message = "El puerto FE 0/2/18 ha subido - OPORTUNIDAD E  P000462 - OEMPR05";
      message = message.replaceAll("[0-9]{1,3}/[0-9]{1,3}/[0-9]{1,3}", "");
      message = message.replaceAll("[a-z]+", "");
      message = message.replaceAll("[\\s][A-Z]{1,2}[\\s]", "");
      message = message.replaceAll("^([A-Z]{1}[\\s])", "");
      message = message.replaceAll("-", "");
      message = message.replaceAll("^([\\s]+)", "");
      message = message.replaceAll("[\\s]+", " ");

      System.out.println("Descripción Alarma: " + message);

      System.out.println(".......................");

      System.out.println(new Date());
      Date today = new Date();

      long ONE_MINUTE_IN_MILLIS = 60000;

      long todayMenos30ML = (new Date()).getTime() - (30 * ONE_MINUTE_IN_MILLIS);
      Date todatMenos30M = new Date(todayMenos30ML);
      System.out.println("Today: " + today);
      System.out.println("Today -30: " + todatMenos30M);

      System.out.println(".......................");

      expresion = "[.][A-Z]+[-]";
      message = "ZAC-BOG.TRIARA-CP2";
      spliter = message.split(expresion);
      for (String splite : spliter) {
         System.out.println("E Interface - Desc= " + splite);
      }

      pattern = Pattern.compile(expresion);
      matcher = pattern.matcher(message);

      while (matcher.find()) {
         String nodo = matcher.group();
         System.out.println("Nodo: " + nodo);
         nodo = nodo.replace("-", "");
         nodo = nodo.replace(".", "");
         System.out.println("Nodo: " + nodo);
      }

      Date endDate = new Date();
      Date startDate = Util.restarFecha(endDate, 30000);
      System.out.println(startDate + " ---- " + endDate);

      ONE_MINUTE_IN_MILLIS = 60000;
      Date date = new Date();
      long newFechaL = date.getTime() - (30 * ONE_MINUTE_IN_MILLIS);
      Date newFecha = new Date(newFechaL);

      System.out.println(newFecha + " ---- " + date);

      String codeServiceIp = "TFM0775 - httpd: ZTE 172.30.15.195";
      StringTokenizer token2 = new StringTokenizer(codeServiceIp, "-");

      token2.nextElement().toString().trim(); // codeService
      String ip = token2.nextElement().toString().trim();
      if (ip.length() > 30) {
         ip = ip.substring(0, 29);
      }
      System.out.println("IP: " + ip);

      String[] eventNames = { "mplsLdpSessionDown", "mplsTunnelDown", "hwAlarmEthernetPortOffline",
         "hwOpticsPowerOutOfRangeAlarmTrap", "GonuLinkLOF", "GponnniOntLinkLosAlarm", "GponOltLOSAlarm",
         "hwGponEthernetPortLossOfSignelOccurAlarmTrap", "hwGponE1T1PortLOSOccurAlarmTrap", "hwGponProfOltTFAlarmTrap",
         "hwGponProfNumerousOntsPowerOffAlarmTrap", "zxGponUnCfgSnIdxcon", "linkDown", "tmnxEqPortFailure",
         "tmnxEqPortSFPRemoved", "hwAlarmEthernetMacLost", "hwAlarmLOSOccurs", "GonuLinkLOS", "GponOntSFAlarm",
         "GponOntLOSAlarm", "GponOntSUFAlarm", "GponOntConfigRecoveryFailAlarm", "GponOntResetByWatchdogAlarm",
         "hwGponONTGEMPortCfgChangeTrap", "zxAnUserOutCVid", "zxAnUserOutSVid", "linkDown", "tmnxEqPortFailure",
         "tmnxEqPortSFPRemoved", "hwAlarmEthernetMacLost", "hwAlarmLOSOccurs", "GonuLinkLOS", "GponOntSFAlarm",
         "GponOntLOSAlarm", "GponOntSUFAlarm", "GponOntConfigRecoveryFailAlarm", "GponOntResetByWatchdogAlarm",
         "hwGponONTGEMPortCfgChangeTrap", "zxAnUserOutCVid", "zxAnUserOutSVid", "tmnxEqPortError",
         "tmnxEqPortEtherAlarm", "tmnxEndPointTxActiveChanged", "svcPersistencyProblem", "hostConnectivityLost",
         "hwPortStateChangeInfoTrap", "hwAlarmUpstreamPortConnectfail", "hwGponOntHardwareFaultyAlarmTrap",
         "hwGponOntE1T1PortStatusAbnormalAlarmTrap", "hwGponTcontProfileOperateTrap", "hwXponPortStateChangeTrap",
         "hwGponProfOltOpticalTransceiverAbsentAlarmTrap", "hwGponProfOntSFAlarmTrap", "hwGponProfOntLOFAlarmTrap",
         "hwGponProfRogueOntDisapearAlarmTrap", "hwXponProfLocalOpticalModuleRecoverAlarmThresAlarmTrap",
         "hwXponProfLocalOpticalModuleRecoverWarnThresAlarmTrap", "hwGponProfOntE1T1PortStatusAbnormalAlarmTrap",
         "zxGponBWProfileIndex", "zxOnuTrafficMgmtUnitTcontUpBWIdxPtr", "zxGponGemPortType", "zxGponGemPortTcontIdx",
         "zxGponVlanPortIndex", "zxGponVlanPortMode", "zxGponVlanPortPvid", "tmnxEqPowerSupplyFailure",
         "tmnxEqPowerSupplyRemoved", "hwDevPowerInputFaultTrap", "hwDevPowerInputRecoverTrap", "powerConnFaultAlarm",
         "acCommFailAlarm", "acInputPowerOffAlarm", "acInputLoopSwtichAlarm", "hwSwitchoverTrap", "hwVlanifDownTrap",
         "hwGponProfNumerousOntsPowerOffAlarmTrap", "mplsLdpFailedInitSessionThresholdExceeded",
         "mplsLdpPathVectorLimitMismatch", "tmnxEqCardFailure", "tmnxEqCardRemoved", "tmnxEqFlashDataLoss",
         "sbiBootConfig", "sbiBootSnmpd", "tmnxSvcPEDiscPolServOperStatChg", "vRtrMplsStateChange",
         "vRtrMplsIfStateChange", "vRtrMplsLspDown", "vRtrMplsLspPathDown", "DevBoardDisappeared",
         "DevBoardStaChgOffLine", "DevBoardMisMatch", "DevBoardHardFault", "DevBoardFault", "DevBoardRestore",
         "DevBoardReplaced", "DevBoardHardRestore", "DevSubBoardMisMatch", "DevSubBoardReplaced",
         "hwAlarmEthernetPortLinkStatusChange", "hwAlarmLinkBetweenStackPortsRecover", "hwGponOltTFlarmTrap",
         "hwGponOltOpticalTransceiverAbsentAlarmTrap", "hwPortTypeAdaptFaultAlarmTrap", "hwGponProfOltLOSAlarmTrap",
         "linkDown", "tmnxEqPortFailure", "tmnxEqPortSFPRemoved", "hwAlarmEthernetMacLost", "hwAlarmLOSOccurs",
         "GonuLinkLOS", "GponOntSFAlarm", "GponOntLOSAlarm", "GponOntSUFAlarm", "GponOntConfigRecoveryFailAlarm",
         "GponOntResetByWatchdogAlarm", "hwGponONTGEMPortCfgChangeTrap", "zxAnUserOutCVid", "zxAnUserOutSVid",
         "tmnxEnvTempTooHigh", "envCommAlarm", "hwOpticsPortTemperatureHighAlarmTrap", "risingAlarm",
         "hwAlarmLOLOccurInBITSInputPort", "hwOpticsTxPowerOutOfRangeAlarmTrap", "hwOpticsRxPowerOutOfRangeAlarmTrap",
         "hwOpticsRxPowerRestoreAlarmTrap", "hwOpticsPowerOutOfRangeAlarmTrap", "GonuLinkLOF",
         "hwGonuTxPowerExceedThreAlarmTrap", "GonuRxPo", "GponnniOntLinkLosAlarm", "GponnniOntLinkLofAlarm",
         "GponnniOntLinkLosRestoreAlarm", "GponOltLOSAlarm", "GponOltLOSRecoverAlarm",
         "hwGponRemoteOpticalModuleExceedAlarmThresAlarmTrap", "hwGponLocalOpticalModuleExceedWarnThresAlarmTrap",
         "hwGponOntE1T1PortStatusRecoverAlarmTrap", "hwGponEthernetPortLossOfSignelOccurAlarmTrap",
         "hwGponE1T1PortLOSOccurAlarmTrap", "hwGponProfOntShellOpenedAlarmTrap", "hwGponProfOntIsRogueOntAlarmTrap",
         "hwXponProfLocalOpticalModuleExceedAlramThresAlarmTrap",
         "hwXponProfRemoteOpticalModuleExceedAlarmThresAlarmTrap",
         "hwXponProfLocalOpticalModuleExceedWarnThresAlarmTrap",
         "hwXponProfRemoteOpticalModuleExceedWarnThresAlarmTrap", "hwGponProfEthernetPortLossOfSignelOccurAlarmTrap",
         "hwGponProfE1T1PortLOSOccurAlarmTrap", "hwGponProfOltLOSAlarmTrap" };

      String[] nameDevices = { "HAC-BAQ.BQCENTRO-CP1", "HAC-BOG.ALAMOS-CP1", "HAC-BOG.ARANDA-CP1",
         "HAC-BOG.ARANDA-CP2", "HAC-BOG.CABRERA-CP1", "HAC-BOG.CENTRO-CP1", "HAC-BOG.CFINANCIERO-CP1",
         "HAC-BOG.CHAPINERO-CP1", "HAC-BOG.CHICOANT-CP2", "HAC-BOG.CHICOANT-CP3", "HAC-BOG.ESMERALDA-CP1",
         "HAC-BOG.FERIAS-CP1", "HAC-BOG.FONTIBON-CP2", "HAC-BOG.FONTIBONH-CP1", "HAC-BOG.GALERIAS-CP1",
         "HAC-BOG.GRANJAS-CP1", "HAC-BOG.PCB-CP1", "HAC-BOG.QUIRIGUA-CP1", "HAC-BOG.QUIRIGUA-CP2",
         "HAC-BOG.RESTREPO-CP1", "HAC-BOG.SANMATEO-CP1", "HAC-BOG.SUBA-CP1", "HAC-BOG.TELEPORT-CP1",
         "HAC-BOG.TEUSAQUILLO-CP1", "HAC-BOG.TOBERIN-CP1", "HAC-BOG.TRIARA-CP1", "HAC-BOG.VENECIA-CP1",
         "HAC-BUC.BUCARAMANGA-CP1", "HAC-CAL.DELICIAS-CP1", "HAC-CAL.ERMITA-CP1", "HAC-CAL.ERMITA-CP2",
         "HAC-CAL.ESTACION-CP1", "HAC-CAL.FLORA-CP1", "HAC-CAL.FLORA-CP2", "HAC-CAL.HOLGUINES-CP1",
         "HAC-CAL.PASARELA-CP1", "HAC-MED.BERRIO-CP1", "HAC-MED.ENVIGADO-CP1", "HAC-MED.ESPACIOSUR-CP1",
         "HAC-MED.ESTADIO-CP1", "HAC-MED.SANDIEGO-CP1", "HAC-NEI.NEIVACENTRO-CP1", "HAC-PER.DOSQBRADAS-CP1",
         "HAC-SIN.SINCELEJO-CP1", "ZAC-ARM.ARMENIA-CP1", "ZAC-BAR.CUMBRE-CP1", "ZAC-BAR.ROBLES-CP1",
         "ZAC-BAR.SENA-CP1", "ZAC-BOG.MASTER100-CP1", "ZAC-BOG.NTOBERIN-CP1", "ZAC-BOG.TRIARA-CP2",
         "ZAC-BUC.PARAUCO-CP1", "ZAC-BUC.RICAURTE-CP1", "ZAC-CAR.INEM-CP1", "ZAC-CHI.CHIA-CP1",
         "ZAC-CTG.EL_BOSQUE-CP1", "ZAC-CUC.CUCUTA-CP1", "ZAC-MAN.MANIZALES-CP1", "ZAC-MED.RIONEGRO-CP1",
         "ZAC-MON.MONTERIA-CP1", "ZAC-STA.SANTAMARTA-CP1", "ZAC-VILL.VILLAVICENCIO-CP1" };

      Random random = new Random();
      String[] prioritys = { "alert", "crit", "warning" };

      String insert = "INSERT INTO syslog_test.logs (`host`,`priority`,`date`,`time`,`msg`)"
         + "VALUES ("
         + "'172.31.239.33',"
         + "'<tag_priority>',"
         + "now(),"
         + "'13:53:09',"
         + "'TFM0775 - httpd: HUAWEI 172.30.4.68 | <name_device> | El puerto FE 0/10/24 se ha caido - ARTURO CALLE P002562 -CMCCO02 |  | .1.3.6.1.4.1.2011.5.14.10.2.0.18 | <tag_event_name>'"
         + ");";

      for (int p = 0; p < 50; p++) {
         int numberFile = random.nextInt(100000);
         String nameFile = "C:/Temp/logs_test_" + numberFile + ".sql";
         File fileSQL = new File(nameFile);
         if (!fileSQL.exists()) {
            fileSQL.createNewFile();
         }

         FileWriter fw = new FileWriter(fileSQL.getAbsoluteFile());
         BufferedWriter bw = new BufferedWriter(fw);

         for (int k = 0; k < 0; k++) {
            int numberEvent = random.nextInt(149);
            String eventName = eventNames[numberEvent];
            int numberPriority = random.nextInt(3);
            String priority = prioritys[numberPriority];
            int numberNameDevice = random.nextInt(62);
            String nameDevice = nameDevices[numberNameDevice];
            bw.write(insert.replaceFirst("<tag_event_name>", eventName).replaceAll("<tag_priority>", priority)
               .replaceAll("<name_device>", nameDevice));
            bw.write("\n");
         }
         bw.close();
      }
      System.out.println("Fertig");

      String messageTrunk = "";
      int endIndex = messageTrunk.length() - 1;
      int beginIndex = endIndex - 50000;
      messageTrunk = messageTrunk.substring(beginIndex, endIndex);
      System.out.println(messageTrunk.length());
      System.out.println(messageTrunk);

      String name = "ZAC-ARM._MASTER100-CP1";
      expresion = "[.][A-Z_0-9]+[-]";
      String nodo = "";

      pattern = Pattern.compile(expresion);
      matcher = pattern.matcher(name);

      if (matcher.find()) {
         nodo = matcher.group();
         nodo = nodo.replace("-", "");
         nodo = nodo.replace(".", "");
      }
      System.out.println(nodo);

      LogDTO log = new LogDTO();
      System.out.println(log.isRelevant());

      message = "anuncio.pl[21532]: ALC_PYMES hFontiboni Problemas de Alcanzabilidad - 10.174.40.31";
      spliter = message.split(":");
      messageTrunk = spliter[1];
      tokenizer = new StringTokenizer(messageTrunk);
      tokenizer.nextToken();
      String nameDevice = tokenizer.nextToken();
      StringBuffer description = new StringBuffer();
      while (tokenizer.hasMoreElements()) {
         description.append(tokenizer.nextToken());
         description.append(" ");
      }
      System.out.println("Name Device: " + nameDevice);
      System.out.println("Description: " + description);

      LogEntity logEntity = new LogEntity();
      logEntity
         .setMsg("TFM0775 - httpd: HUAWEI 172.30.14.183 | HAC-BOG.TRIARA-CP1 | Se generaron cambios en la ONT 0 -100653824 |  | .1.3.6.1.4.1.2011.6.128.1.1.12.3.0.51 | hwXponProfOntPortStatusTrap");

      HashMap<String, LogDTO> hashMap = new HashMap<String, LogDTO>();
      LogDTO lol = new LogDTO();
      lol.setName("Uno");
      hashMap.put("Log1", lol);
      lol = new LogDTO();
      lol.setName("Dos");
      hashMap.put("Log2", lol);

      System.out.println(hashMap.get("Log1").getName());
      System.out.println(hashMap.containsKey("Log"));

      AlarmCatalogEntity alarmCatalogEntity = new AlarmCatalogEntity();
      alarmCatalogEntity.setTextAlarm("Opppps");
      HashMap<KeyCatalogDTO, AlarmCatalogEntity> has = new HashMap<KeyCatalogDTO, AlarmCatalogEntity>();
      KeyCatalogDTO k = new KeyCatalogDTO("1", "3");
      has.put(k, alarmCatalogEntity);

      System.out.println(has.containsKey(k));

      Date heute = new Date();
      System.out.println("Today: " + heute);
      System.out.println(Util.restarFecha(heute, 20));

   }
}
