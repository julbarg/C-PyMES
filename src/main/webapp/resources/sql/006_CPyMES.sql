-- =============================================
-- Author:  	Julian Barragan Verano
-- Create date: 07-MAY-2015
-- Description: CREATE TABLES Project CPyMES - IVR
-- Name SQL: 006_CPyMES.sql
-- =============================================

-- TRUNCATE TABLE ALARMA_PYMES_SERVICIO_NIT
CREATE OR REPLACE PROCEDURE KOU_ADM.TRUNCAR_ALARMA_SERVICIO_NIT IS
BEGIN
   EXECUTE IMMEDIATE ('TRUNCATE TABLE KOU_ADM.ALARMA_PYMES_SERVICIO_NIT;');
  EXCEPTION 
      WHEN OTHERS THEN
        BEGIN
          raise_application_error(-20101,'  Fallo Truncate Alarma Pymes ' );
        END;
END TRUNCAR_ALARMA_SERVICIO_NIT;
