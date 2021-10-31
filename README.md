<img src="https://www.code-inspector.com/project/27285/score/svg"> 

# Storage Handler
This is a library based off of my old storage handler within my [queue revamp](https://github.com/NoSequel/queue-recode). It's for easy storage handling for multiple platforms.

# Todo
* DynamicSqlStorageProvider
  * Currently not finished, and will definitely throw an error if you try to use it.
  * Find a way to dynamically fetch the entry (`DynamicSqlStorageProvider<T>#setEntry(String, T)` should work, but untested)

# Example Usages
> more examples coming soon... i'm lazy

## Redis
```java
public class ExampleClass {
    
    public static void main(String[] args) {
        final RedisStorageHandler storageHandler = new RedisStorageHandler(new NoAuthRedisSettings(
                        "127.0.0.1",
                        6379
                )
        );
        
        final RedisStorageProvider<String> exampleProvider = new RedisStorageProvider<>(
                "examples",
                storageHandler,
                String.class
        );
        
        storageHandler.setEntry("hey", "how are you");
        storageHandler.setEntry("bye", "you are bad.");
        
        Thread.sleep(1000L);
        
        for (Entry<String, String> entry : storageHandler.fetchAllEntries().join()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```