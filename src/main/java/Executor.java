import com.google.inject.Guice;
import com.google.inject.Injector;
import entity.Config;
import io.vertx.core.Vertx;
import model.ConfigLoader;
import module.Processing;
import module.ProcessingModule;

import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * Main executor, execute job based on the configuration file under the resources folder
 */
public class Executor {
    public static void main(String[] args) throws FileNotFoundException {
        Config config = new ConfigLoader().load();
        Vertx vertx = Vertx.vertx();
        Injector injector = Guice.createInjector(new ProcessingModule(vertx, config));
        Processing worker = injector.getInstance(Processing.class);
        UUID jobId = UUID.randomUUID();
        worker.start(jobId);
    }
}
