The account indexing system provide the processing 
logic for incoming account data.

## How to start run in local environment:
1. Get the data with .json format ready, you could put under the resources file
2. Update the config.json in the resources file, make sure the processType is 'BATCH' and update the filePath to point to the data file
3. Run the Executor.java's main method

## System pattern
The code and project implement with java + gradle. This is one of most
popular code build tools for java. 

We use Guice for dependent management and the object injection. Using
injection can help decouple where the dependent construct from where to
be called. We can easily config the type of the components we want
in Guice Module, and only need to consider the interface design. Using
injection also help in unit test, where we use local/inMemory/Mock
component by only changing the bind class in module file.

The system itself contains 3 parts:
IO, Callback, DataBase.

IO response for reading data.  
Callback response for processing data before insert DataBase.  
DataBase is where the data final landed.

Consider we will handle the streaming format data in production env 
and do IO and callback in async way, we introduced an eventBus as
a cache where the data IO will publish to and callback will read from.
Here we use the vertx.io framework which is a java framework provide
variety of api help run java in non-blocking and async way:  
IO -> EventBus -> CallBack -> DB

## Observability
When the system get online for production serving purpose, we will
need to add some metrics to help monitor the system:
1. JVM/Compute entity metrics like CPU/Memory/GC/Uptime/Versions of code on runner
2. Traffic metrics like rps/network throughput/P90,95,99,999/latency
3. Service metrics like success rate/rate of data ingested/dead letter ratio
4. Per upstream/client metrics/usage on service
5. Rate limit data if available
