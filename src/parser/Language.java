package parser;

/**
 * Created by Lars on 11.11.2014.
 */
public enum Language {
    DE("Deutsch", "DE"),
    EN("Englisch", "EN"),
    FR("Franzoesisch", "FR");

    private final String shortLang;
    private final String longLang;

    Language(String longLang, String shortLang) {
        this.longLang = longLang;
        this.shortLang = shortLang;
    }

}
