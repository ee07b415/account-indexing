package module;

import account.protos.AccountOuterClass;
import com.google.inject.Inject;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Vertx;
import model.Callback;
import model.Database;
import model.IO;
import model.LocalCallback;

import java.util.UUID;
import java.util.logging.Logger;

public class BatchProcessing implements Processing {
    private static final Logger logger = Logger.getLogger(LocalCallback.class.getName());
    IO io;
    Database<AccountOuterClass.Account> database;
    Vertx vertx;

    Callback callback;

    @Inject
    public BatchProcessing(Vertx vertx, IO io, Callback callback, Database database) {
        this.io = io;
        this.vertx = vertx;
        this.callback = callback;
        this.database = database;
    }

    @Override
    public void start(UUID jobId) {
        // initial callback
        logger.info("Initial callback");
        callback.initial();

        // Read file and send to eb
        logger.info("fetching data");
        CompositeFuture fetchProcess = io.fetch();

        fetchProcess.onComplete(res -> {
            if (res.succeeded()) {
                logger.info("Finish read all data item");
            } else {
                logger.info("Some item failed in IO");
            }
        }).compose(v -> {
            // Because the current processing model is streaming only so
            // we need periodically check if we processed all callbacks to
            // determine the job is done
            vertx.setPeriodic(1000, periodId -> {
                if(callback.isEmpty()) {
                    shutdown();
                }
            });
            return v;
        });
    }

    @Override
    public void shutdown() {
        // print the highest token-value accounts by parent_program_subtype (taking into account write version)
        database.getAllPeek().forEach((key, value) ->
                logger.info("Type: " + key + "'s highest token account:" + value.getId()));
        logger.info("process shutdown");
        vertx.close();
    }
}
