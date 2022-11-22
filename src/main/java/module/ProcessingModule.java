package module;

import com.google.inject.AbstractModule;
import entity.Config;
import entity.ProcessType;
import io.vertx.core.Vertx;
import model.*;

public class ProcessingModule extends AbstractModule {
    Vertx vertx;
    Config config;

    public ProcessingModule(Vertx vertx, Config config){
        this.vertx = vertx;
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Callback.class).to(LocalCallback.class);
        bind(IO.class).to(FileSystemIO.class);
        bind(Vertx.class).toInstance(vertx);
        bind(Config.class).toInstance(config);
        if (config.getProcessType().equals(ProcessType.BATCH)) {
            bind(Processing.class).to(BatchProcessing.class);
        } else {
            // We don't have other processing type yet but can be changed later
            bind(Processing.class).to(BatchProcessing.class);
        }
        bind(Database.class).toInstance(new SimplePriorityQueueDataBase());
    }
}
