package parser;

import infoobject.InfoObject;
import infoprovider.InfoProvider;

import java.util.logging.Logger;

/**
 * The Parser class will offer Constants for each known Parser and Offer for the Core a method to call.
 */
public abstract class Parser {
    public static final WikiParser WIKI_PARSER = new WikiParser();
    final InfoProvider provider;
    Logger log = Logger.getGlobal();

    /**
     * will set an InfoProvider which is used to get the infos for parsing.
     *
     * @param provider
     */
    Parser(InfoProvider provider) {
        this.provider = provider;
    }

    /**
     * This method is supposed to analyse an object regarding the used Parser
     *
     * @param objectToAnalyse which should be analysed
     */
    public abstract void analyse(InfoObject objectToAnalyse);

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [" + provider.toString() + "]";
    }
}
