package com.example.nemol.bottom;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by nemol on 30.08.2017.
 */

public class RssLink {

    private int id;
    private String link;
    private String title;

    RssLink(int id, String link, String title) {
        this.id = id;
        this.link = link;
        this.title = title;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    String getLink() {
        return this.link;
    }

    String getTitle() {
        return this.title;
    }

    public int getId() {
        return id;
    }
}
