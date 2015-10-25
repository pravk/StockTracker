package mobile.pk.com.stocktracker.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by praveen on 20/09/15.
 */
public class BlogPost implements Serializable{

    private String id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String title;
    private String content;
    private long lastModified;
    private String summary20;
    private String summary50;
    private String summary100;
    private String summary200;
    private String summary300;
    private String author;
    private String blog;
    private String url;
    private List<String> imageUrlList;

    private List<BlogComment> comments;
    private int commentCount;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSummary() {
        return summary20;
    }

    public void setSummary(String summary) {
        this.summary20 = summary;
    }

    public List<BlogComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getSummary50() {
        return summary50;
    }

    public void setSummary50(String summary50) {
        this.summary50 = summary50;
    }

    public String getSummary100() {
        return summary100;
    }

    public void setSummary100(String summary100) {
        this.summary100 = summary100;
    }

    public String getSummary200() {
        return summary200;
    }

    public void setSummary200(String summary200) {
        this.summary200 = summary200;
    }

    public String getSummary300() {
        return summary300;
    }

    public void setSummary300(String summary300) {
        this.summary300 = summary300;
    }
}