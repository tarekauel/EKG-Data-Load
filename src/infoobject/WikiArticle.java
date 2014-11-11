package infoobject;

import parser.Language;
import parser.Parser;

import java.util.ArrayList;

/**
 * Created by Lars on 11.11.2014.
 */
public class WikiArticle extends InfoObject {
    private long pageId = 0;
    private long revisionId = 0;
    private Language language = null;
    private String url = "";
    private String title = "";
    private ArrayList<WikiArticle> linkedArticles = new ArrayList<WikiArticle>();
    private ArrayList<String> linkedCategories = new ArrayList<String>();

    public WikiArticle(String title) {
        super(Parser.WIKI_PARSER);
        this.title = title;
    }

    public boolean setPageId(long pageId) {
        if (this.pageId == 0 && pageId > 0) {
            this.pageId = pageId;
            return true;
        }
        return false;
    }

    public boolean setRevisionId(long revisionId) {
        if (this.revisionId == 0 && revisionId > 0) {
            this.revisionId = revisionId;
            return true;
        }
        return false;
    }

    public boolean setLanguage(Language language) {
        if (this.language == null && language != null) {
            this.language = language;
            return true;
        }
        return false;
    }

    public boolean setUrl(String url) {
        if (this.url.equals("") && !url.equals("")) {
            this.url = url;
            return true;
        }
        return false;
    }

    public boolean addLinkedArticles(WikiArticle linkedArticle) {
        if (this.linkedArticles.contains(linkedArticle)) {
            return false;
        }
        this.linkedArticles.add(linkedArticle);
        return true;
    }

    public boolean addLinkedCategories(String linkedCategory) {
        for (String s : linkedCategories) {
            if (s.equals(linkedCategory)) {
                return false;
            }
        }
        this.linkedCategories.add(linkedCategory);
        return true;
    }

    public long getPageId() {
        return pageId;
    }

    public long getRevisionId() {
        return revisionId;
    }

    public Language getLanguage() {
        return language;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<WikiArticle> getLinkedArticles() {
        return linkedArticles;
    }

    public ArrayList<String> getLinkedCategories() {
        return linkedCategories;
    }

    @Override
    public String toString() {
        return title;
    }

}
