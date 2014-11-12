package infoobject;

import parser.Parser;

/**
 * An InfoObject is supposed to store all relevant data from a Resource
 */
public abstract class InfoObject {
    private final Parser ASSOCIATED_PARSER;

    /**
     * Constructor
     * It will associate a Parser to the InfoObject which will be called on analyse.
     *
     * @param associatedParser the instance of a Parser to use with this InfoObject.
     */
    public InfoObject(Parser associatedParser) {
        ASSOCIATED_PARSER = associatedParser;
    }

    /**
     * This constructor is to prevent anyone of doing useless things with the InfoObject
     */
    private InfoObject() {
        ASSOCIATED_PARSER = null;
    }

    /**
     * This method will cause the InfoObject to be analysed regarding to its associated parser
     */
    public void analyse() {
        ASSOCIATED_PARSER.analyse(this);
    }

    @Override
    public String toString() {
        return "InfoObject with AssociatedParser " + ASSOCIATED_PARSER;
    }
}
