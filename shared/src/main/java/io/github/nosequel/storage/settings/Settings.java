package io.github.nosequel.storage.settings;

public interface Settings<T, U> {

    /**
     * Create the database handling object.
     *
     * @return the newly created object
     */
    T createObject();

    /**
     * Authenticate for the database
     *
     * @param object the object to authenticate for
     * @return the authenticated object
     */
    U auth(U object);

}
