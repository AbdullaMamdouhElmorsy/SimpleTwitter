
package com.morsy.simpletwitter;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class TwitterClient extends OAuthBaseClient {

    public static final BaseApi REST_API_CLASS = TwitterApi.instance();
    // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY =
            "PIF4WKYsvYpD4pfsp00bd4QEi";       // Change this
    public static final String REST_CONSUMER_SECRET =
            "4NUsE3uBeTABIpdW05Qn171eiQ9JHn42I9183dvB2uiboHCD0P"; // Change this

    public static final String REST_CALLBACK_URL = "x-oauthflow-twitter://arbitraryname.com"; // Change this (here and in manifest)
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

    //METHOD=ENDPOINT
    //HomeTimeLine - Get us the home timeline data
    // GET statuses/home_timeline.json
    //  count=25
    //  since_id = 1
    public void getHomeTimeline(long maxId,  long sinceId, boolean isScrolled, boolean isRefreshed,
                                AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();

        if (isScrolled) {
            params.put("max_id", maxId);
            params.put("count", 25);
        } else if(isRefreshed) {
            params.put("since_id", sinceId);
        } else {
            params.put("count", 25);
            params.put("since_id", 1); //get latest tweets
        }
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void sendTweet(String text, boolean isReplyTo,
                          String inReplyToStatusId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");
        //Specify the params
        RequestParams params = new RequestParams();

        if (isReplyTo) {
            params.put("in_reply_to_status_id", inReplyToStatusId);
        }

        params.put("status", text);

        //Execute the request
        getClient().post(apiUrl, params, handler);
    }

    public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("screen_name", screenName);
        //params.put("since_id", 1); //get latest tweets
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void getMyInfo(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");
        //Execute the request
        getClient().get(apiUrl, null, handler);
    }

    public void getMentionsTimeline(long l, long l1, boolean isScrolled,
                                    boolean isRefreshed,
                                    TextHttpResponseHandler textHttpResponseHandler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("count", 25);

        //Execute the request
        getClient().get(apiUrl, textHttpResponseHandler);
    }

    public void getUserInfo(String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("users/show.json");
        //Execute the request
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        getClient().get(apiUrl, params, handler);
    }

    public void getFollowings(String screenName, long cursor, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("friends/list.json");
        //Execute the request
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("cursor", cursor);
        getClient().get(apiUrl, params, handler);
    }

    public void getFollowers(long cursor, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("followers/list.json");
        //Execute the request
        RequestParams params = new RequestParams();
        //params.put("screen_name", screenName);
        params.put("cursor", cursor);
        getClient().get(apiUrl, params, handler);
    }

    public void searchTweets(String query, long maxId,  long sinceId, boolean isScrolled,
                             boolean isRefreshed,
                             AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("search/tweets.json");
        //Execute the request
        RequestParams params = new RequestParams();
        params.put("q", query);

        if (isScrolled) {
            params.put("max_id", maxId);
            params.put("count", 25);
        } else if(isRefreshed) {
            params.put("since_id", sinceId);
        } else {
            params.put("count", 25);
            params.put("since_id", 1); //get latest tweets
        }

        getClient().get(apiUrl, params, handler);
    }

    public void getMessages(long maxId,  long sinceId, boolean isScrolled, boolean isRefreshed,
                            AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("direct_messages.json");
        //Specify the params
        RequestParams params = new RequestParams();

        if (isScrolled) {
            params.put("max_id", maxId);
            params.put("count", 25);
        } else if(isRefreshed) {
            params.put("since_id", sinceId);
        } else {
            params.put("count", 25);
            params.put("since_id", 1); //get latest tweets
        }
        //Execute the request
        getClient().get(apiUrl, params, handler);
    }

    //direct_messages/new.json
    public void sendMessage(String text, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("direct_messages/new.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("text", text);
        params.put("screen_name", screenName);

        //Execute the request
        getClient().post(apiUrl, params, handler);
    }

    //statuses/retweet/:id.json
    public void statusRetweet(String statusId,AsyncHttpResponseHandler handler) {
        String uri = "statuses/retweet/" + statusId + ".json";
        String apiUrl = getApiUrl(uri);
        //Execute the request
        getClient().post(apiUrl, null, handler);
    }

    //favorites/create.json
    public void statusLike(String statusId,AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");
        //Specify the params
        RequestParams params = new RequestParams();
        params.put("id", statusId);

        //Execute the request
        getClient().post(apiUrl, params, handler);
    }


    //https://gist.github.com/shrikant0013/0ab543ce0b2f645eb3bb


    //COMPOSE TWEET

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}