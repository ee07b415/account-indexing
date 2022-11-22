package model;

import account.protos.AccountOuterClass;
import account.protos.AccountOuterClass.Accounts;
import com.google.inject.Inject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import entity.Config;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.core.file.FileSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * File system IO read in data from the json file which contains array of json object of data
 * from the filepath defined in the configuration file then write to the build in eventBus
 * managed by vertx
 */
public class FileSystemIO implements IO{
    private static final Logger logger = Logger.getLogger(FileSystemIO.class.getName());
    Vertx vertx;
    Config config;

    @Inject
    FileSystemIO(Vertx vertx, Config config){
        this.vertx = vertx;
        this.config = config;
    }
    @Override
    public CompositeFuture fetch(){
        EventBus eb = vertx.eventBus();
        MessageProducer<AccountOuterClass.Account> producer = eb.publisher("topic");

        FileSystem fs = vertx.fileSystem();

        List<Future> fetchProcess = new ArrayList<>();

        fs.readFile(config.getFilePath(), res -> {
            try {
                Accounts.Builder as = Accounts.newBuilder();
                // The JSONArray not follow protobuf format, need add the key of the array logic
                JsonFormat.parser().merge("{\"accounts\":" + res.result().toString() + "}", as);
                for(AccountOuterClass.Account acc : as.getAccountsList()){
                    fetchProcess.add(producer.write(acc));
                }
            } catch (InvalidProtocolBufferException e) {
                logger.warning(e.getMessage());
            }
        });

        return CompositeFuture.all(fetchProcess);
    }
}
