package mx.com.castores.Injector;
import castores.core.InjectorContainer;
import com.google.inject.Guice;
import com.google.inject.Injector;
public class AppInjector {
    private static Injector inject;
    private AppInjector() {
    }
    public static Injector getInjector() {
        if (inject == null) {
            synchronized (AppInjector.class) {
                if (inject == null) {
                    inject = Guice.createInjector(new PersistenciaModule());
                    InjectorContainer ic = InjectorContainer.getInstance(inject);
                }
            }
        } 
        return inject;
       
    }
}