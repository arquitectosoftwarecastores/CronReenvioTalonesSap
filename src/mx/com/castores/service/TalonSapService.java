/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.castores.service;

import castores.core.PersistenciaLocal;
import castores.dao.castores.Endpoints_sapDao;
import castores.dao.cfdinomina.Cfdi23Dao;
import castores.dao.talones.DocumentoscfdiDao;
import castores.dao.talones.Log_reeenvio_talones_sapDao;
import castores.dao.talones.Sap_logsDao;
import castores.dao.talones.Servicios_sapDao;
import castores.dao.talones.TalonesDao;
import castores.model.castores.Endpoints_sap;
import castores.model.cfdinomina.Cfdi23;
import castores.model.talones.Documentoscfdi;
import castores.model.talones.Log_reeenvio_talones_sap;
import castores.model.talones.Sap_logs;
import castores.model.talones.Servicios_sap;
import castores.model.talones.Talones;
import com.castores.criteriaapi.core.CriteriaBuilder;
import com.castores.datautilsapi.db.DBUtils;
import com.google.inject.Injector;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import mx.com.castores.Injector.AppInjector;


/**
 *
 * @author desarrolloti32
 */
public class TalonSapService {


    public List<Sap_logs> findTalonesPreRegistro() {
        Injector inj = AppInjector.getInjector();
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
        Sap_logsDao talonesLogSapDao = inj.getInstance(Sap_logsDao.class);
        Calendar calendario = Calendar.getInstance();

        calendario.setTime( new Date());
        // Restar 24 horas
        calendario.add(Calendar.MINUTE, -2);
        Date fechaPasada = calendario.getTime();
        
        criteriaBuilder.between("CONCAT(fecha,' ',hora)", fechaPasada, "NOW()");
        criteriaBuilder.eq("peticion", "PRE-REGISTRO");
        criteriaBuilder.in("respuesta_servicio", new String[]{"Registro Antes de enviar a SAP","Error: No se pudo obtener el token de autenticación."});
        List<Sap_logs> lstTalones = talonesLogSapDao.findBy(criteriaBuilder);
        criteriaBuilder.clear();
        return lstTalones;
    }
    
     public List<Endpoints_sap> getAllEnpointsSap() {
        Injector inj = AppInjector.getInjector();
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
        Endpoints_sapDao endpointsSapDao = inj.getInstance(Endpoints_sapDao.class); 
        List<Endpoints_sap> lstTalones = endpointsSapDao.findAll();
        criteriaBuilder.clear();
        return lstTalones;
    }
    
    
    public void insertLogSap(String claTalon, String estatus, String mensaje, String idCliente, String nombreCliente) {
        try {
            Injector inj = AppInjector.getInjector();
            CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
            Log_reeenvio_talones_sapDao logReenvioTalonesDao = inj.getInstance(Log_reeenvio_talones_sapDao.class);
            criteriaBuilder.eq("cla_talon", claTalon);
            Log_reeenvio_talones_sap logReeenvioTalonesSap = new Log_reeenvio_talones_sap();
            logReeenvioTalonesSap.setCla_talon(claTalon);
            logReeenvioTalonesSap.setEstatus(estatus);
            logReeenvioTalonesSap.setMensaje(mensaje);
            logReeenvioTalonesSap.setFecha_envio(logReenvioTalonesDao.getCurrentDate());
            logReeenvioTalonesSap.setIdCliente(idCliente);
            logReeenvioTalonesSap.setNombreCliente(nombreCliente);
            logReenvioTalonesDao.create(logReeenvioTalonesSap);
        } catch (Exception ex) {
            Logger.getLogger(SapService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
