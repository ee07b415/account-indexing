package model;

import account.protos.AccountOuterClass;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import testEntities.TestModule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class FileSystemIOTest {
    @Test
    public void fetchTest(Vertx vertx, VertxTestContext testContext){
        Injector injector = Guice.createInjector(new TestModule(vertx));
        IO io = injector.getInstance(IO.class);

        Checkpoint dataPublished = testContext.checkpoint();
        Checkpoint test1version1DataReceived = testContext.checkpoint();
        Checkpoint test1version2DataReceived = testContext.checkpoint();
        Checkpoint test2version1DataReceived = testContext.checkpoint();
        Checkpoint test2version2DataReceived = testContext.checkpoint();

        EventBus eb = vertx.eventBus();
        MessageConsumer<AccountOuterClass.Account> consumer = eb.consumer("topic");

        consumer.handler(message -> {
            AccountOuterClass.Account acc = message.body();
            System.out.println(acc.getId()+ " " + acc.getVersion() + " received");
            if (acc.getId().endsWith("1") && acc.getVersion() == 1){
                test1version1DataReceived.flag();
            } else if (acc.getId().endsWith("1") && acc.getVersion() == 2){
                test1version2DataReceived.flag();
            } else if (acc.getId().endsWith("2") && acc.getVersion() == 1){
                test2version1DataReceived.flag();
            } else if (acc.getId().endsWith("2") && acc.getVersion() == 2) {
                test2version2DataReceived.flag();
            }
        });

        io.fetch().onComplete(testContext.succeeding(v -> dataPublished.flag()));
    }
}
