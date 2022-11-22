package model;

import account.protos.AccountOuterClass;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A simple priority queue database read in account data and stored in key(subType) value(Queue(data))
 * The queue is a priority based on the tokens account
 */
public class SimplePriorityQueueDataBase implements Database<AccountOuterClass.Account> {
    private final Map<String, Queue<AccountOuterClass.Account>> inMemoryTable = new ConcurrentHashMap<>();

    public SimplePriorityQueueDataBase(){}

    @Override
    public void insert(AccountOuterClass.Account acc){
        if (!inMemoryTable.containsKey(acc.getParentProgramSubType())){
            inMemoryTable.put(acc.getParentProgramSubType(),
                    new PriorityQueue<>(Comparator.comparingLong(AccountOuterClass.Account::getTokens).reversed()));
        }
        inMemoryTable.get(acc.getParentProgramSubType()).offer(acc);
    }

    /**
     * Get all peek return the highest token account information of each sub-type
     * @return a map of {subType, account} account is the one with highest token in the original queue
     */
    @Override
    public Map<String, AccountOuterClass.Account> getAllPeek(){
        return inMemoryTable.entrySet()
                .stream()
                .filter(x -> !x.getValue().isEmpty())
                .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue().peek()));
    }

    public Queue<AccountOuterClass.Account> get(String key){
        return inMemoryTable.get(key);
    }
}
