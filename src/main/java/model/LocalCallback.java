package model;

import account.protos.AccountOuterClass;
import com.google.inject.Inject;
import entity.VersionTimerId;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * A local callback system managed by vertx timer
 *
 * When receive a message of account information, we will try to check if the
 * id-parentid-subtype key exist in current callBackMap.
 * If not: generate a callback to indexing the data and add id key as key, versionid and callback id as value to the map
 * If exist: check the version ids, if the new data has newer version, cancel the old callback and update map
 * with new callback, if new data has older version, ignore the update
 */
public class LocalCallback implements Callback{
    private static final Logger logger = Logger.getLogger(LocalCallback.class.getName());
    Vertx vertx;
    Database<AccountOuterClass.Account> database;
    Map<String, VersionTimerId> callBackMap = new ConcurrentHashMap<>();

    @Inject
    public LocalCallback(Vertx vertx, Database database){
        this.vertx = vertx;
        this.database = database;
    }
    @Override
    public void initial(){
        EventBus eb = vertx.eventBus();
        MessageConsumer<AccountOuterClass.Account> consumer = eb.consumer("topic");

        consumer.handler(message -> {
            AccountOuterClass.Account acc = message.body();
            String key = acc.getId() + acc.getParentProgram() + acc.getParentProgramSubType();

            // Do nothing if new message with older version
            // add to callbackmap or replace with new version
            // skip the item without version for now
            if (!acc.hasVersion()) {
                return;
            }

            if (!callBackMap.containsKey(key) || (callBackMap.get(key).getVersion() < acc.getVersion())){
                // Only new item need to cancel old call back
                if (callBackMap.containsKey(key)){
                    VersionTimerId versionTimerId = callBackMap.get(key);

                    // when successfully cancelled(false for now exist or already executed),
                    // will cancel despite return value
                    if(vertx.cancelTimer(versionTimerId.getTimerId())) {
                        // Display an old callback is canceled in favor of a new one.
                        logger.info("old version " + versionTimerId.getVersion() +
                                " cancelled with new version " + acc.getVersion());
                    }
                }

                // generate new delayed callback
                if (acc.getCallbackTimeMs() < 1){
                    // directly executing
                    indexing(acc);
                } else {
                    long timerId = vertx.setTimer(acc.getCallbackTimeMs(), id -> {
                        indexing(acc);
                        callBackMap.remove(key);
                    });
                    callBackMap.put(key, new VersionTimerId(acc.getVersion(), timerId));
                }
            }
        });
    }

    void indexing(AccountOuterClass.Account acc) {
        //Display a short message log message to console when each (accountId +
        //parent_program_subtype + version) tuple has been indexed.
        logger.info(acc.getId() + ":" + acc.getParentProgramSubType() + ":" + acc.getVersion() + " indexed");
        database.insert(acc);
    }

    @Override
    public boolean isEmpty(){
        return callBackMap.isEmpty();
    }
}
