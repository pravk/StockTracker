package mobile.pk.com.stocktracker.service;

import java.util.List;

import mobile.pk.com.stocktracker.dao.BlogPost;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by praveen on 20/09/15.
 */
public interface BlogService {


    @GET("/blog/search")
    List<BlogPost> findBlogPosts(@Query("search")String text);

    @GET("/blog/search")
    void findBlogPosts(@Query("search")String text, @Query("page")int page, @Query("size")int size, Callback<List<BlogPost>> callback);


    @GET("/blog/recent")
    List<BlogPost> getRecentBlogPosts();

    @GET("/blog/recent")
    void getRecentBlogPosts(@Query("page")int page, @Query("size")int size, Callback<List<BlogPost>> callback);
}
