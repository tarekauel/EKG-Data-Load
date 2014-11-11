package parser;

import infoobject.InfoObject;

/**
 * Created by Lars on 11.11.2014.
 */
public abstract class Parser {
    public static final WikiParser WIKI_PARSER = new WikiParser();


    public abstract void analyse(InfoObject objectToAnalyse);
}
