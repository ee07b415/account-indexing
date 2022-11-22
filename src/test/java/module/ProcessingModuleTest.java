package module;

import com.google.inject.Guice;
import com.google.inject.Injector;
import entity.Config;
import entity.ProcessType;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class ProcessingModuleTest {
    @Test
    public void processingModuleVerifyTest(Vertx vertx){
        Config config = new Config(ProcessType.BATCH, "src/test/resources/example.json");
        Injector injector = Guice.createInjector(new ProcessingModule(vertx, config));

        assert injector.getInstance(Callback.class) instanceof LocalCallback;
        assert injector.getInstance(IO.class) instanceof FileSystemIO;
        assert injector.getInstance(Processing.class) instanceof BatchProcessing;
        assert injector.getInstance(Database.class) instanceof SimplePriorityQueueDataBase;
        assert injector.getInstance(Config.class).equals(config);
    }
}
