-- =============================================
-- Author:  	Julian Barragan Verano
-- Create date: 13-MAY-2015
-- Description: ROLLBACK TABLES Project CPyMES - IVR Seguridad
-- Name SQL: 008_CPyMES.sql
-- =============================================

DROP PROCEDURE KOU_ADM.TRUNCAR_ALARMA_SERVICIO_NIT;
DROP PROCEDURE KOU_ADM.TRUNCAR_ALARMA_PYMES;

DROP SEQUENCE KOU_ADM.ALARMA_PYMES_SERVICIO_NIT_SEQ;
DROP SEQUENCE KOU_ADM.ALARMA_PYMES_SEQ;

DROP PUBLIC SYNONYM ALARMA_PYMES_VIEW;
DROP PUBLIC SYNONYM ALARMA_PYMES_SERVICIO_NIT;
DROP PUBLIC SYNONYM ALARMA_PYMES;

DROP VIEW KOU_ADM.ALARMA_PYMES_VIEW;

DROP INDEX KOU_ADM.CODIGO_SERVICIO_INDEX;
DROP INDEX KOU_ADM.NIT_INDEX;

ALTER TABLE KOU_ADM.ALARMA_PYMES_SERVICIO_NIT DROP CONSTRAINT ALARMA_PYMES_SERVICIO_NIT_FK1;

DROP TABLE KOU_ADM.ALARMA_PYMES_SERVICIO_NIT;
DROP TABLE KOU_ADM.ALARMA_PYMES;