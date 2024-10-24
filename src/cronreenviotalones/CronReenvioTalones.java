/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cronreenviotalones;

import castores.model.castores.Endpoints_sap;
import castores.model.talones.Sap_logs;
import castores.model.talones.Servicios_sap;
import java.util.List;
import mx.com.castores.service.SapService;
import mx.com.castores.service.TalonSapService;

/**
 *
 * @author desarrolloti32
 */
public class CronReenvioTalones {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        TalonSapService talonService = new TalonSapService();
        SapService sapService = new SapService();
        List<Sap_logs> lstServiciosSap = talonService.findTalonesPreRegistro();
        List<Endpoints_sap> lstEndpointsSap = talonService.getAllEnpointsSap();
        sapService.sendTalonSap(lstServiciosSap, lstEndpointsSap);
        
    }
    
}
