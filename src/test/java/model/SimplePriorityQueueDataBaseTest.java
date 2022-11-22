package model;

import account.protos.AccountOuterClass;
import testEntities.TestEntity;
import org.junit.jupiter.api.Test;

public class SimplePriorityQueueDataBaseTest {
    @Test
    public void getAllPeekTest(){
        SimplePriorityQueueDataBase simpleDb = new SimplePriorityQueueDataBase();

        simpleDb.insert(TestEntity.test1version1);
        simpleDb.insert(TestEntity.test1version2);

        AccountOuterClass.Account result = simpleDb.getAllPeek().get(TestEntity.test1version1.getParentProgramSubType());

        assert result.getTokens() == TestEntity.test1version2.getTokens();
    }
}
