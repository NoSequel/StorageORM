# Storage Handler
This is a library based off of my old storage handler within my [queue revamp](https://github.com/NoSequel/queue-recode). It's for easy storage handling for multiple platforms.

# Todo / Development Progress
* Serialization currently only works for **Jedis** module. Mongo module uses GSON to serialize/deserialize.
* Perhaps add more features in the future.

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