package io.github.nosequel.storage.storage;

import io.github.nosequel.storage.StorageHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class StorageProvider<K, V> {

    /**
     * Get the storage handler of the {@link StorageProvider}
     *
     * @return the storage handler
     */
    public abstract StorageHandler getStorageHandler();

    /**
     * Set an entry within the storage object.
     *
     * @param key   the key of the entry
     * @param value the entry itself
     */
    public abstract void setEntry(K key, V value);

    /**
     * Remove an entry within the storage object.
     * <p>
     * This method will remove the entry by the key itself,
     * consult to {@link StorageProvider#removeEntryValue(Object)}
     * if you want to remove by the value instead.
     * </p>
     *
     * @param key the key to remove from the entry
     */
    public abstract void removeEntry(K key);

    /**
     * Remove an entry within the storage object.
     * <p>
     * This method will remove the entry by the value itself,
     * consult to {@link StorageProvider#removeEntry(Object)}
     * if you want to remove by the key instead.
     * </p>
     *
     * @param value the value to remove from the storage object
     */
    public abstract void removeEntryValue(V value);

    /**
     * Fetch an entry from the storage by a {@link K}.
     *
     * @param key the key to get the entry from
     * @return the entry, or null
     */
    public abstract CompletableFuture<V> fetchEntry(K key);

    /**
     * Fetch all entries from the storage.
     *
     * @return all of the found entries by the same key within the database
     */
    public abstract CompletableFuture<Map<K, V>> fetchAllEntries();

}