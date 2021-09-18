import io.github.nosequel.storage.sql.SqlStorageHandler;
import io.github.nosequel.storage.sql.provider.SqlStorageProvider;
import io.github.nosequel.storage.sql.settings.impl.JdbcPasswordSqlSettings;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class BaseSqlUnitTest {

    @Test
    @SneakyThrows
    public void simpleSaveTest() {
        final SqlStorageHandler storageHandler = new SqlStorageHandler(new JdbcPasswordSqlSettings(
                "jdbc:mysql://us-dal-mysql-01.plox.host:3306/server_14885",
                "server_14885",
                "12cdc1cf95"
        ));

        final SqlStorageProvider<Example> provider = new SqlStorageProvider<>(storageHandler, "hello", Example.class);

        provider.setEntry("staud is fat", new Example(
                "hey",
                "staud is fat"
        ));

        Thread.sleep(2000);

        System.out.println(provider.fetchEntry("staud is fat").get(2, TimeUnit.SECONDS).format());
    }

    @RequiredArgsConstructor
    class Example {

        public final String hello;
        public final String bye;

        public String format() {
            return hello + " " + bye;
        }
    }
}