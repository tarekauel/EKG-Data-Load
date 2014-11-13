package parser;

import infoobject.InfoObject;
import infoobject.WikiArticle;
import infoprovider.HttpProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Parser can parse WikiArticles
 * It will connect to the english wikipedia version (This is hardcoded in the constructor)
 */
public class WikiParser extends Parser {

    /**
     * Creates a new WikiParser.
     * Will create a Provider for the parser which will connect to the api of the english wiki
     */
    public WikiParser() {
        super(new HttpProvider("http://en.wikipedia.org/w/api.php"));
    }

    @Override
    public void analyse(InfoObject objectToAnalyse) {
        log.finest(this.toString() + ": analysing [" + objectToAnalyse.toString() + "]");
        // Parse the InfoObject to an WikiArticle to make it possible to parse it correct.
        WikiArticle currentArticle = (WikiArticle) objectToAnalyse;

        // gets the general infos and parses them
        String infoQuery = "?continue=&action=query&format=json&prop=info&inprop=url&titles=" + currentArticle.getTitle();
        currentArticle = parseInfoOfWikiArticle(provider.requestResource(infoQuery), currentArticle);

        // gets the links of this article and parses them. If you can not achieve all articles with one query
        // it will be called recursive to get all links.
        String linkQuery = "?continue=&action=query&format=json&prop=links&pllimit=max&titles=" + currentArticle.getTitle();
        currentArticle = parseLinksOfWikiArticle(provider.requestResource(linkQuery), currentArticle);
        // gets the categories of this article and parses them. If you can not achieve all categories with one query
        // it will be called recursive to get all links.
        String categoryQuery = "?continue=&action=query&format=json&prop=categories&cllimit=max&titles=" + currentArticle.getTitle();
        currentArticle = parseCategoriesOfWikiArticle(provider.requestResource(categoryQuery), currentArticle);
    }

    /**
     * parses the answer of a wikiResponse with the infoobject returned as json
     * This will fill the objects attributes:
     * - pageId
     * - revisionId
     * - url
     * - language
     *
     * @param wikiResponse    the json with all the infos in it
     * @param objectToAnalyse object, which should be filled with more information
     * @return giving back the updated object.
     */
    private WikiArticle parseInfoOfWikiArticle(String wikiResponse, WikiArticle objectToAnalyse) {
        log.finer("[" + objectToAnalyse.getTitle() + "] Response from Wikipedia Source to Info Request:" + wikiResponse);
        JSONObject obj = null;
        try {
            //reduce the object to the needed node.
            obj = new JSONObject(wikiResponse);
            obj = obj.getJSONObject("query").getJSONObject("pages");
            obj = obj.getJSONObject((String) obj.keys().next());
            //get only the needed properties and add it to the object.
            objectToAnalyse.setPageId(obj.getInt("pageid"));
            objectToAnalyse.setRevisionId(obj.getInt("lastrevid"));
            objectToAnalyse.setUrl(obj.getString("fullurl"));
            objectToAnalyse.setLanguage(Language.EN); //TODO: Do correct language conversion
        } catch (JSONException e) {
            // There was a wrong answer and/ or the api changed
            log.severe("[" + objectToAnalyse.getTitle() + "] Could not parse the response: " + wikiResponse);
        }
        return objectToAnalyse;
    }

    /**
     * parses the answer of a wikiResponse with the linksobject returned as json
     * This will fill the objects attributes:
     * - linkedArticles
     *
     * @param wikiResponse    the json with all the links in it
     * @param objectToAnalyse object, which should be filled with more information
     * @return giving back the updated object.
     */
    private WikiArticle parseLinksOfWikiArticle(String wikiResponse, WikiArticle objectToAnalyse) {
        log.finer("[" + objectToAnalyse.getTitle() + "] Response from Wikipedia Source to Links Request:" + wikiResponse);
        JSONObject obj = null;
        JSONArray arr = null;
        String req = "";

        try {
            //reduce the object to the needed node.
            obj = new JSONObject(wikiResponse);
            obj = obj.getJSONObject("query").getJSONObject("pages");
            obj = obj.getJSONObject(Long.toString(objectToAnalyse.getPageId()));
            arr = obj.getJSONArray("links");
            //loop over the array full of links
            for (int i = 0; i < arr.length(); i++) {
                objectToAnalyse.addLinkedArticles(new WikiArticle(arr.getJSONObject(i).getString("title")));
            }
            //reparse the answer to check if we need to continue
            obj = new JSONObject(wikiResponse);
            try {
                req = obj.getJSONObject("continue").getString("plcontinue");
            } catch (JSONException e) {
                //you will get here, if the continue does not exists. in this case we are ready with this parsing.
                return objectToAnalyse;
            }
            //seems like continue was set and we are going to continue with this, cause there are more linked things..
            if (req.length() > 0) {
                log.finest("[" + objectToAnalyse.getTitle() + "] needs some more Information from wiki and requests it." +
                        "Information belonging to section Links");
                //query with the new startpoint (plcontinue)
                String linkQuery = "?continue=&action=query&format=json&prop=links&pllimit=max&plcontinue="
                        + req + "&titles=" + objectToAnalyse.getTitle();
                //recursive!
                objectToAnalyse = parseLinksOfWikiArticle(provider.requestResource(linkQuery), objectToAnalyse);
            }



        } catch (JSONException e) {
            // There was a wrong answer and/ or the api changed
            log.severe("[" + objectToAnalyse.getTitle() + "] Could not parse the response: " + wikiResponse);
        }
        return objectToAnalyse;

    }

    /**
     * parses the answer of a wikiResponse with the categoriesobject returned as json
     * This will fill the objects attributes:
     * - linkedCategories
     *
     * @param wikiResponse    the json with all the categories in it
     * @param objectToAnalyse object, which should be filled with more information
     * @return giving back the updated object.
     */
    private WikiArticle parseCategoriesOfWikiArticle(String wikiResponse, WikiArticle objectToAnalyse) {
        log.finer("[" + objectToAnalyse.getTitle() + "] Response from Wikipedia Source to Category Request:"
                + wikiResponse);
        JSONObject obj = null;
        JSONArray arr = null;
        String req = "";

        try {
            //reduce the object to the needed node.
            obj = new JSONObject(wikiResponse);
            obj = obj.getJSONObject("query").getJSONObject("pages");
            obj = obj.getJSONObject(Long.toString(objectToAnalyse.getPageId()));
            arr = obj.getJSONArray("categories");
            //loop over the array full of categories
            for (int i = 0; i < arr.length(); i++) {
                objectToAnalyse.addLinkedCategories(arr.getJSONObject(i).getString("title"));
            }
            //reparse the answer to check if we need to continue
            obj = new JSONObject(wikiResponse);
            try {
                req = obj.getJSONObject("continue").getString("clcontinue");
            } catch (JSONException e) {
                //you will get here, if the continue does not exists. in this case we are ready with this parsing.
                return objectToAnalyse;
            }
            //seems like continue was set and we are going to continue with this, cause there are more linked things..
            if (req.length() > 0) {
                log.finest("[" + objectToAnalyse.getTitle() + "] needs some more Information from wiki and requests it." +
                        "Information belonging to section Categories");
                String categoriesQuery = "?continue=&action=query&format=json&prop=categories&cllimit=max&clcontinue="
                        + req + "&titles=" + objectToAnalyse.getTitle();
                //recursiv!
                objectToAnalyse = parseLinksOfWikiArticle(provider.requestResource(categoriesQuery), objectToAnalyse);
            }


        } catch (JSONException e) {
            log.severe("[" + objectToAnalyse.getTitle() + "] Could not parse the response: " + wikiResponse);
        }
        return objectToAnalyse;

    }
}
