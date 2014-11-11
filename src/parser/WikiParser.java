package parser;

import infoobject.InfoObject;
import infoobject.WikiArticle;
import infoprovider.HttpProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lars on 11.11.2014.
 */
public class WikiParser extends Parser {
    private HttpProvider provider = new HttpProvider("http://en.wikipedia.org/w/api.php");

    @Override
    public void analyse(InfoObject objectToAnalyse) {
        WikiArticle currentArticle = (WikiArticle) objectToAnalyse;
        String infoQuery = "?continue=&action=query&format=json&prop=info&titles=" + currentArticle.getTitle();
        currentArticle = parseInfoOfWikiArticle(provider.requestResource(infoQuery), currentArticle);

        String linkQuery = "?continue=&action=query&format=json&prop=links&pllimit=max&titles=" + currentArticle.getTitle();
        currentArticle = parseLinksOfWikiArticle(provider.requestResource(linkQuery), currentArticle);

        String categoryQuery = "?continue=&action=query&format=json&prop=categories&cllimit=max&titles=" + currentArticle.getTitle();
        currentArticle = parseCategoriesOfWikiArticle(provider.requestResource(categoryQuery), currentArticle);

        System.out.println(currentArticle.getLinkedCategories().size());
    }

    private WikiArticle parseInfoOfWikiArticle(String wikiResponse, WikiArticle objectToAnalyse) {
        JSONObject obj = null;
        try {
            obj = new JSONObject(wikiResponse);
            obj = obj.getJSONObject("query").getJSONObject("pages");
            obj = obj.getJSONObject((String) obj.keys().next());

            objectToAnalyse.setPageId(obj.getInt("pageid"));
            objectToAnalyse.setRevisionId(obj.getInt("lastrevid"));
            objectToAnalyse.setLanguage(Language.EN); //TODO: Do correct language conversion
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return objectToAnalyse;
    }

    private WikiArticle parseLinksOfWikiArticle(String wikiResponse, WikiArticle objectToAnalyse) {
        JSONObject obj = null;
        JSONArray arr = null;
        String req = "";

        try {

            obj = new JSONObject(wikiResponse);

            obj = obj.getJSONObject("query").getJSONObject("pages");
            obj = obj.getJSONObject(Long.toString(objectToAnalyse.getPageId()));
            arr = obj.getJSONArray("links");
            for (int i = 0; i < arr.length(); i++) {
                objectToAnalyse.addLinkedArticles(new WikiArticle(arr.getJSONObject(i).getString("title")));
            }
            obj = new JSONObject(wikiResponse);
            try {
                req = obj.getJSONObject("continue").getString("plcontinue");
            } catch (JSONException e) {
                return objectToAnalyse;
            }
            if (req.length() > 0) {
                String linkQuery = "?continue=&action=query&format=json&prop=links&pllimit=max&plcontinue="
                        + req + "&titles=" + objectToAnalyse.getTitle();
                objectToAnalyse = parseLinksOfWikiArticle(provider.requestResource(linkQuery), objectToAnalyse);
            }


        } catch (JSONException e) {
            return objectToAnalyse;
        }

        return objectToAnalyse;
    }

    private WikiArticle parseCategoriesOfWikiArticle(String wikiResponse, WikiArticle objectToAnalyse) {
        JSONObject obj = null;
        JSONArray arr = null;
        String req = "";

        try {

            obj = new JSONObject(wikiResponse);

            obj = obj.getJSONObject("query").getJSONObject("pages");
            obj = obj.getJSONObject(Long.toString(objectToAnalyse.getPageId()));
            arr = obj.getJSONArray("categories");

            for (int i = 0; i < arr.length(); i++) {
                objectToAnalyse.addLinkedCategories(arr.getJSONObject(i).getString("title"));
            }
            obj = new JSONObject(wikiResponse);
            try {
                req = obj.getJSONObject("continue").getString("clcontinue");
            } catch (JSONException e) {
                return objectToAnalyse;
            }
            if (req.length() > 0) {
                String categoriesQuery = "?continue=&action=query&format=json&prop=categories&cllimit=max&clcontinue="
                        + req + "&titles=" + objectToAnalyse.getTitle();
                objectToAnalyse = parseLinksOfWikiArticle(provider.requestResource(categoriesQuery), objectToAnalyse);
            }


        } catch (JSONException e) {
            return objectToAnalyse;
        }

        return objectToAnalyse;
    }
}
