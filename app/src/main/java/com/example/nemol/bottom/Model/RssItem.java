package com.example.nemol.bottom.Model;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by nemol on 27.08.2017.
 */

public class RssItem {

    private String title;
    private String description;
    private Date pubDate;
    private String link;

    public RssItem(String title, String description, Date pubDate, String link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.link = link;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getLink()
    {
        return this.link;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Date getPubDate()
    {
        return this.pubDate;
    }

    @Override
    public String toString() {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd - hh:mm:ss");

        String result = getTitle() + "  ( " + sdf.format(this.getPubDate()) + " )";
        return result;
    }

    public static ArrayList<RssItem> getRssItems(String feedUrl) {
        ArrayList<RssItem> rssItems = new ArrayList<>();
        try {
            return new GetRssItems().execute(feedUrl).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return rssItems;
    }

}

class GetRssItems extends AsyncTask<String, Void, ArrayList<RssItem>>{
    @Override
    protected ArrayList<RssItem> doInBackground(String... feedUrl) {
        ArrayList<RssItem> rssItems = new ArrayList<>();

        try {
            URL url = new URL(feedUrl[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();

                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(is);
                Element element = document.getDocumentElement();

                NodeList nodeList = element.getElementsByTagName("item");

                if (nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {

                        Element entry = (Element) nodeList.item(i);

                        Element titleE = (Element) entry.getElementsByTagName(
                                "title").item(0);
                        Element descriptionE = (Element) entry
                                .getElementsByTagName("description").item(0);
                        Element pubDateE = (Element) entry
                                .getElementsByTagName("pubDate").item(0);
                        Element linkE = (Element) entry.getElementsByTagName(
                                "link").item(0);

                        String title = titleE.getFirstChild().getNodeValue();
                        String description = descriptionE.getFirstChild().getNodeValue();
                        Date pubDate = new Date(pubDateE.getFirstChild().getNodeValue());
                        String link = linkE.getFirstChild().getNodeValue();
                        RssItem rssItem = new RssItem(title, description,
                                pubDate, link);

                        rssItems.add(rssItem);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssItems;
    }
}
