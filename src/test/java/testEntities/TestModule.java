package testEntities;

import com.google.inject.AbstractModule;
import entity.Config;
import entity.ProcessType;
import io.vertx.core.Vertx;
import model.*;
import module.BatchProcessing;
import module.Processing;

public class TestModule extends AbstractModule {
    Vertx vertx;
    Config config;

    public TestModule(Vertx vertx) {
        this.vertx = vertx;
        config = new Config(ProcessType.BATCH, "src/test/resources/example.json");
    }

    @Override
    protected void configure() {
        bind(Callback.class).to(LocalCallback.class);
        bind(IO.class).to(FileSystemIO.class);
        bind(Vertx.class).toInstance(vertx);
        bind(Config.class).toInstance(config);
        bind(Processing.class).to(BatchProcessing.class);
        bind(Database.class).toInstance(new SimplePriorityQueueDataBase());
    }
}