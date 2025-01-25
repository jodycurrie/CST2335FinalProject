package com.example.lab8;
/**
 * Represents a news item with details like title, description, link, publication date, and its favorite status.
 */
public class NewsItem {

    private String title;
    private String description;
    private String link;

    private String pubDate;
    private boolean favorite = false;
    private int id;

    /**
     * Constructs a new NewsItem object with the provided details.
     *
     * @param title The title of the news item
     * @param description The description of the news item
     * @param link The link to the full news article
     * @param pubDate The publication date of the news item
     * @param favorite The favorite status of the news item
     * @param id The unique identifier of the news item
     */
    public NewsItem(String title, String description, String link, String pubDate, boolean favorite,int id) {

        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
        this.favorite = favorite;
        this.id = id;

    }

    /**
     * Returns the unique identifier of the news item.
     *
     * @return The ID of the news item
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the news item.
     *
     * @return The title of the news item
     */
    public String getTitle() {

        return title;
    }

    /**
     * Returns the description of the news item.
     *
     * @return The description of the news item
     */
    public String getDescription() {

        return description;
    }

    /**
     * Returns the link to the full news article.
     *
     * @return The link to the full news article
     */
    public String getLink() {

        return link;
    }

    /**
     * Returns the publication date of the news item.
     *
     * @return The publication date of the news item
     */
    public String getPubDate() {

        return pubDate;

    }

    /**
     * Returns whether the news item is marked as a favorite.
     *
     * @return The favorite status of the news item
     */
    public boolean getFavorite() {

        return favorite;

    }

    /**
     * Marks the news item as a favorite.
     */
    public void setFavorite() {

        this.favorite = true;

    }

    /**
     * Returns a string representation of the NewsItem, which is the title of the news item.
     *
     * @return A string representation of the NewsItem
     */
    @Override
    public String toString() {

        return title;

    }


}
