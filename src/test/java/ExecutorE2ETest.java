import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import module.Processing;
import testEntities.TestModule;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ExecutorE2ETest {
    @Test
    void e2eBatchProcessingTest(){
        Vertx vertx = Vertx.vertx();
        Injector injector = Guice.createInjector(new TestModule(vertx));
        Processing worker = injector.getInstance(Processing.class);
        UUID jobId = UUID.randomUUID();
        worker.start(jobId);
    }
}
