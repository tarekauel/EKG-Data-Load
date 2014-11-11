package infoprovider;

/**
 * Created by Lars on 11.11.2014.
 */
public abstract class InfoProvider {
    String baseUri = "";

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
        return baseUri;
    }
}
