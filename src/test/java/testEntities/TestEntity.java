package testEntities;

import account.protos.AccountOuterClass;

public class TestEntity {
    public static AccountOuterClass.Account test1version1 = AccountOuterClass.Account.newBuilder()
            .setId("test1")
            .setParentProgram("parent1")
            .setParentProgramSubType("mint")
            .setTokens(500L)
            .setCallbackTimeMs(1000L)
            .putData("mintId", "6BhkGCMVMyrjEEkrASJcLxfAvoW43g6BubxjpeUyZFoz")
            .setVersion(1)
            .build();

    public static AccountOuterClass.Account test1version2 = AccountOuterClass.Account.newBuilder()
            .setId("test1")
            .setParentProgram("parent1")
            .setParentProgramSubType("mint")
            .setTokens(600L)
            .setCallbackTimeMs(1L)
            .putData("mintId", "6BhkGCMVMyrjEEkrASJcLxfAvoW43g6BubxjpeUyZFoz")
            .setVersion(2)
            .build();

    public static AccountOuterClass.Account test2version1 = AccountOuterClass.Account.newBuilder()
            .setId("test2")
            .setParentProgram("parent2")
            .setParentProgramSubType("metadata")
            .setTokens(500L)
            .setCallbackTimeMs(1000L)
            .putData("mintId", "6BhkGCMVMyrjEEkrASJcLxfAvoW43g6BubxjpeUyZFoz")
            .setVersion(1)
            .build();

    public static AccountOuterClass.Account test2version2 = AccountOuterClass.Account.newBuilder()
            .setId("test2")
            .setParentProgram("parent2")
            .setParentProgramSubType("metadata")
            .setTokens(600L)
            .setCallbackTimeMs(1L)
            .putData("mintId", "6BhkGCMVMyrjEEkrASJcLxfAvoW43g6BubxjpeUyZFoz")
            .setVersion(2)
            .build();
}
