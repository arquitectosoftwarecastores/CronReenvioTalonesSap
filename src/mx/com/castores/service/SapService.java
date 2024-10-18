package mx.com.castores.service;

import castores.dao.talones.ParametrosDao;
import castores.dao.talones.Sap_logsDao;
import castores.model.castores.Endpoints_sap;
import castores.model.talones.Parametros;
import castores.model.talones.Sap_logs;
import com.castores.criteriaapi.core.CriteriaBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.inject.Injector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.com.castores.Injector.AppInjector;
import mx.com.castores.dto.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.google.gson.Gson;
import java.util.Optional;
import org.json.JSONObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.com.castores.util.AccessToken;
import org.apache.commons.logging.LogConfigurationException;

/**
 *
 * @author desarrolloti32
 */
public class SapService {

    public void sendTalonSap(List<Sap_logs> lstTalonesSapLog, List<Endpoints_sap> lstEndpointsSap) {
        TokenDTO objToken = getTokenParams();
        String accessToken = getTokenSAP(objToken);
        if (accessToken != null) {
            lstTalonesSapLog.forEach(talon -> {
                System.err.println(talon.getId());
                Optional<Endpoints_sap> filteredEendpointsSap = lstEndpointsSap.stream()
                        .filter(endpoint -> endpoint.getId_servicio() == talon.getIdservicio())
                        .findFirst();

                if (filteredEendpointsSap.isPresent()) {
                    Endpoints_sap endpointsSap = filteredEendpointsSap.get();
                    reenviarTalon(talon.getParametros(), accessToken, endpointsSap.getEndpoint(), objToken.getUrlServidor(), talon);
                } else {

                }
            });
        }
    }

    public String getTokenSAP(TokenDTO objToken) {
        try {

            String url = objToken.getUrlToken();
            String authToken = "";
            HttpResponse<String> response = Unirest.post(url).basicAuth(
                    objToken.getClientID(),
                    objToken.getClientSecret())
                    .queryString("username", objToken.getUserName())
                    .queryString("password", objToken.getPassword())
                    .queryString("grant_type", "password").asString();
            JSONObject obj = new JSONObject((String) response.getBody());
            authToken = obj.optString("access_token", "");
            return authToken;

        }catch(LogConfigurationException | UnirestException ex){
            Logger.getLogger(SapService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public TokenDTO getTokenParams() {
        Injector inj = AppInjector.getInjector();
        CriteriaBuilder criteriaBuilder = new CriteriaBuilder();
        ParametrosDao talonesParametrosDao = inj.getInstance(ParametrosDao.class);
        TokenDTO objToken = new TokenDTO();
        List<Parametros> lstParametros;

        criteriaBuilder.eq("idparametro", 333);
        lstParametros = new ArrayList<Parametros>();
        lstParametros = talonesParametrosDao.findBy(criteriaBuilder);
        objToken.setUrlServidor(lstParametros.get(0).getValor());
        criteriaBuilder.clear();

        criteriaBuilder.eq("idparametro", 334);
        lstParametros = new ArrayList<Parametros>();
        lstParametros = talonesParametrosDao.findBy(criteriaBuilder);
        objToken.setUrlToken(lstParametros.get(0).getValor());
        criteriaBuilder.clear();

        criteriaBuilder.eq("idparametro", 335);
        lstParametros = new ArrayList<Parametros>();
        lstParametros = talonesParametrosDao.findBy(criteriaBuilder);
        objToken.setClientID(lstParametros.get(0).getValor());
        criteriaBuilder.clear();

        criteriaBuilder.eq("idparametro", 336);
        lstParametros = new ArrayList<Parametros>();
        lstParametros = talonesParametrosDao.findBy(criteriaBuilder);
        objToken.setClientSecret(lstParametros.get(0).getValor());
        criteriaBuilder.clear();

        criteriaBuilder.eq("idparametro", 337);
        lstParametros = new ArrayList<Parametros>();
        lstParametros = talonesParametrosDao.findBy(criteriaBuilder);
        objToken.setUserName(lstParametros.get(0).getValor());
        criteriaBuilder.clear();

        criteriaBuilder.eq("idparametro", 338);
        lstParametros = new ArrayList<Parametros>();
        lstParametros = talonesParametrosDao.findBy(criteriaBuilder);
        objToken.setPassword(lstParametros.get(0).getValor());
        criteriaBuilder.clear();

        return objToken;
    }

    public void reenviarTalon(String sapJson, String accessTokenInhouse, String endpoint, String serverUrl, Sap_logs sapLogs) {
        String url = serverUrl + endpoint;
        boolean responseEntity = executeRequest(url, accessTokenInhouse, sapJson);
        if (responseEntity) {
            Injector inj = AppInjector.getInjector();
            Sap_logsDao talonesLogSapDao = inj.getInstance(Sap_logsDao.class);
            sapLogs.setPeticion("ESCRITURA");
            sapLogs.setFecha(talonesLogSapDao.getCurrentDate());
            sapLogs.setHora(talonesLogSapDao.getCurrentDate());
            talonesLogSapDao.edit(sapLogs);
        }
    }

    public boolean executeRequest(String url, String tokenSAP, String body) {
        try {

            String respuestaSAP = "";
            HttpResponse<String> response = Unirest.post(url)
                    .header("Authorization", "Bearer " + tokenSAP)
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();
            JSONObject jsonResponse = new JSONObject(response.getBody());
            respuestaSAP = response.getBody();
            if (jsonResponse.has("message")) {
                return true;
            }
        } catch (UnirestException | JsonSyntaxException ex) {
            Logger.getLogger(SapService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SapService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
