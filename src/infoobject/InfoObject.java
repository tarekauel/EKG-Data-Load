package infoobject;

import parser.Parser;

/**
 * Created by Lars on 11.11.2014.
 */
public abstract class InfoObject {
    private final Parser ASSOCIATED_PARSER;

    public InfoObject(Parser associatedParser) {
        ASSOCIATED_PARSER = associatedParser;
    }

    private InfoObject() {
        ASSOCIATED_PARSER = null;
    }

    public void analyse() {
        ASSOCIATED_PARSER.analyse(this);
    }

    @Override
    public String toString() {
        return "InfoObject with AssociatedParser " + ASSOCIATED_PARSER;
    }
}
