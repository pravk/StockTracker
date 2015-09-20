package mobile.pk.com.stocktracker.event;

import mobile.pk.com.stocktracker.dao.BlogPost;

/**
 * Created by praveen on 20/09/15.
 */
public class ViewBlogPostEvent {
    private final BlogPost blogPost;

    public ViewBlogPostEvent(BlogPost blogPost) {
        this.blogPost = blogPost;
    }

    public BlogPost getBlogPost() {
        return blogPost;
    }
}
