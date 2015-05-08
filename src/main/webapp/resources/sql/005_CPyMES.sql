-- =============================================
-- Author:  	Julian Barragan Verano
-- Create date: 07-MAY-2015
-- Description: CREATE TABLES Project CPyMES - IVR
-- Name SQL: 005_CPyMES.sql
-- =============================================

-- TRUNCATE TABLE ALARMA_PYMES
CREATE OR REPLACE PROCEDURE KOU_ADM.TRUNCAR_ALARMA_PYMES IS
BEGIN
   EXECUTE IMMEDIATE ('TRUNCATE TABLE KOU_ADM.ALARMA_PYMES;');
  EXCEPTION 
      WHEN OTHERS THEN
        BEGIN
          raise_application_error(-20101,'  Fallo Truncate Alarma Pymes ' );
        END;
END TRUNCAR_ALARMA_PYMES;
