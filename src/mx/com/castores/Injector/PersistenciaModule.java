package mx.com.castores.Injector;

import castores.core.Persistencia;
import castores.core.PersistenciaLocal;
import com.castores.datautilsapi.log.LoggerUtils;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;


public class PersistenciaModule implements Module {

    @Override
    public void configure(Binder binder) {
        String server13;
        String serverOficina;

        binder.bind(Persistencia.class).to(PersistenciaLocal.class);
        Properties properties = new Properties();

        try {
            InputStream input = new FileInputStream("application.properties");
            
            properties.load(input);
            String env = properties.getProperty("env");
            if (env.equals("dev")) {
                server13 = properties.getProperty("devServer13");
                serverOficina = properties.getProperty("devServerOficina");
            } else {
                server13 = properties.getProperty("prodServer13");
                serverOficina = properties.getProperty("prodServerOficina");
            }
            binder.bindConstant().annotatedWith(Names.named("Server13")).to(server13 + "&usuarioWin&windows");
            binder.bindConstant().annotatedWith(Names.named("Server23")).to(serverOficina + "&usuarioWin&windows");
        } catch (Exception e) {
            LoggerUtils.printLog(this.getClass(), Level.SEVERE, e, "ERROR", Thread.currentThread().getStackTrace());
        }
    }

}