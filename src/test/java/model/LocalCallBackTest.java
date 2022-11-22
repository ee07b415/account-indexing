package model;

import account.protos.AccountOuterClass;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageProducer;
import io.vertx.junit5.RunTestOnContext;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import testEntities.TestEntity;
import testEntities.TestModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@ExtendWith(VertxExtension.class)
public class LocalCallBackTest {
    @RegisterExtension
    RunTestOnContext rtoc = new RunTestOnContext();

    Vertx vertx;


    @BeforeEach
    void prepare(VertxTestContext testContext) {
        vertx = rtoc.vertx();
        // Prepare something on a Vert.x event-loop thread
        // The thread changes with each test instance
        testContext.completeNow();
    }

    @Test
    public void fetchTest(Vertx vertx, VertxTestContext testContext) {
        Injector injector = Guice.createInjector(new TestModule(vertx));
        Callback callback = injector.getInstance(Callback.class);
        EventBus eb = vertx.eventBus();
        MessageProducer<AccountOuterClass.Account> producer = eb.publisher("topic");
        callback.initial();

        List<Future> fetchProcess = new ArrayList<>();
        fetchProcess.add(producer.write(TestEntity.test1version1));
        fetchProcess.add(producer.write(TestEntity.test1version2));

        CompositeFuture.all(fetchProcess)
                .onComplete(future -> {
                    vertx.setPeriodic(1000, periodId -> {
                        if(callback.isEmpty()) {
                            SimplePriorityQueueDataBase simpleDb = (SimplePriorityQueueDataBase) injector.getInstance(Database.class);
                            Queue<AccountOuterClass.Account> result = simpleDb.get(TestEntity.test1version1.getParentProgramSubType());
                            assert result.size() == 1;
                            assert result.poll().getVersion() == 2;
                            testContext.completeNow();
                        }
                    });
                });
    }

    @Test
    public void fetchIgnoreOldVersion(Vertx vertx, VertxTestContext testContext) {
        Injector injector = Guice.createInjector(new TestModule(vertx));
        Callback callback = injector.getInstance(Callback.class);
        EventBus eb = vertx.eventBus();
        MessageProducer<AccountOuterClass.Account> producer = eb.publisher("topic");
        callback.initial();

        List<Future> fetchProcess = new ArrayList<>();
        fetchProcess.add(producer.write(TestEntity.test2version2));
        fetchProcess.add(producer.write(TestEntity.test2version1));

        CompositeFuture.all(fetchProcess)
                .onComplete(future -> {
                    vertx.setPeriodic(1000, periodId -> {
                        if(callback.isEmpty()) {
                            SimplePriorityQueueDataBase simpleDb = (SimplePriorityQueueDataBase) injector.getInstance(Database.class);
                            Queue<AccountOuterClass.Account> result = simpleDb.get(TestEntity.test2version2.getParentProgramSubType());
                            assert result.size() == 1;
                            assert result.poll().getVersion() == 2;
                            testContext.completeNow();
                        }
                    });
                });
    }
}
