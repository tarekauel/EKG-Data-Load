package infoobject;

import core.Core;
import parser.Language;
import parser.Parser;

import java.util.ArrayList;

/**
 * This class represents an wikipedia Article
 */
public class WikiArticle extends InfoObject {
    private long pageId = 0;
    private long revisionId = 0;
    private Language language = null;
    private String url = "";
    private String title = "";
    private ArrayList<WikiArticle> linkedArticles = new ArrayList<WikiArticle>();
    private ArrayList<String> linkedCategories = new ArrayList<String>();

    /**
     * Will create a new WikipediaArticle with a title.
     *
     * @param title the Title of a WikiArticle. Be aware that this is not the shown title on the wiki page itself,
     *              this is caused by the possibility of double titles in the view, but not technically.
     */
    public WikiArticle(String title) {
        //Assign the correct parser
        super(Parser.WIKI_PARSER);
        log.finer("Title:" + title);
        this.title = title;
    }

    /**
     * sets the pageId to a value
     *
     * @param pageId long
     * @return if the value is already set, it will return false, else true.
     */
    public boolean setPageId(long pageId) {
        if (this.pageId == 0 && pageId > 0) {
            log.finer("[" + this.getTitle() + "] Page Id:" + pageId);
            this.pageId = pageId;
            return true;
        }
        log.severe("[" + this.getTitle() + "] tried to set property with value [" + pageId + "] but failed.");
        return false;
    }

    /**
     * sets the revisionId to a value
     *
     * @param revisionId long
     * @return if the value is already set, it will return false, else true.
     */
    public boolean setRevisionId(long revisionId) {
        if (this.revisionId == 0 && revisionId > 0) {
            this.revisionId = revisionId;
            log.finer("[" + this.getTitle() + "] Last Revision Id:" + revisionId);
            return true;
        }
        log.severe("[" + this.getTitle() + "] tried to set property with value [" + revisionId + "] but failed.");
        return false;
    }

    /**
     * sets the language to a value
     *
     * @param language enum Language
     * @return if the value is already set, it will return false, else true.
     */
    public boolean setLanguage(Language language) {
        if (this.language == null && language != null) {
            this.language = language;
            log.finer("[" + this.getTitle() + "] language:" + language);
            return true;
        }
        log.severe("[" + this.getTitle() + "] tried to set property with value [" + language + "] but failed.");
        return false;
    }

    /**
     * sets the url to a value
     *
     * @param url String
     * @return if the value is already set, it will return false, else true.
     */
    public boolean setUrl(String url) {
        if (this.url.equals("") && !url.equals("")) {
            this.url = url;
            log.finer("[" + this.getTitle() + "] URL:" + url);
            return true;
        }
        log.severe("[" + this.getTitle() + "] tried to set property with value [" + url + "] but failed.");
        return false;
    }

    /**
     * adds an linked Article
     *
     * @param linkedArticle WikiArticle
     * @return false, if this article is already linked
     */
    public boolean addLinkedArticles(WikiArticle linkedArticle) {
        if (this.linkedArticles.contains(linkedArticle)) {
            log.severe("[" + this.getTitle() + "] tried to set property with value [" + linkedArticle + "] but failed.");
            return false;
        }
        log.finer("[" + this.getTitle() + "] added linked article:" + linkedArticle.getTitle());
        this.linkedArticles.add(linkedArticle);
        // add it to the scheduling!
        Core.getCore().addWikiArticleToScheduleList(linkedArticle);

        return true;
    }

    /**
     * adds an linked Category
     *
     * @param linkedCategory WikiArticle
     * @return false, if this category is already linked
     */
    public boolean addLinkedCategories(String linkedCategory) {
        for (String s : linkedCategories) {
            if (s.equals(linkedCategory)) {
                log.severe("[" + this.getTitle() + "] tried to set property with value [" + linkedCategory + "] but failed.");
                return false;
            }
        }
        log.finer("[" + this.getTitle() + "] added linked category");
        this.linkedCategories.add(linkedCategory);
        return true;
    }

    /**
     * returns the unique page ID
     *
     * @return pageId
     */
    public long getPageId() {
        return pageId;
    }

    /**
     * returns the unique last revision Id
     *
     * @return revisionId
     */
    public long getRevisionId() {
        return revisionId;
    }

    /**
     * returns the language of this article
     *
     * @return language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * returns the url to the article, if you want to see the html
     *
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * returns the technical title of an article
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * returns a list of all articles which are linked by this article.
     *
     * @return
     */
    public ArrayList<WikiArticle> getLinkedArticles() {
        return linkedArticles;
    }

    /**
     * returns a list of all categories which are linked to this article
     *
     * @return
     */
    public ArrayList<String> getLinkedCategories() {
        return linkedCategories;
    }

    @Override
    public String toString() {
        return super.toString() + "." + this.getClass().getSimpleName() + "[" + this.getTitle() + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WikiArticle other = (WikiArticle) obj;
        //If this null is and the other title is set, its unequal.
        //If this is not null and the the titles are equal, its surely equal too.
        if ((this.getTitle() == null) ? (other.getTitle() != null) : !this.getTitle().equals(other.getTitle())) {
            return false;
        }
        return true;

    }
}
