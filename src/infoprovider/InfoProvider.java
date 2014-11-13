package infoprovider;

import java.util.logging.Logger;

/**
 * An InfoProvider can give access to any kind of resource
 */
public abstract class InfoProvider {
    String baseUri = "";
    Logger log = Logger.getGlobal();

    /**
     * will take an baseUri which can be used as root directory in case of files or as api link in case of web provider.
     *
     * @param baseUri
     */
    InfoProvider(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * will send a Query with the baseUri of the Provider and the query from this call
     *
     * @param query
     * @return
     */
    public abstract String requestResource(String query);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" + baseUri + "]";
    }
}
