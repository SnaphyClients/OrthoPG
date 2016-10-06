package com.orthopg.snaphy.orthopg;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class Constants {
    public static String TAG = "OrthoPG";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;


    public static String baseUrl = "http://192.168.1.106:3000";
    //public static String baseUrl = "http://ec2-54-209-33-191.compute-1.amazonaws.com:3000";
    public static String apiUrl = baseUrl+"/api";
    //TODO CHANGE IT LATER
    public static final String AMAZON_CLOUD_FRONT_URL = "http://d3j3ux0h7dntsg.cloudfront.net";
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    public static String SENDER_ID = "673699478279";
    public static String LOOPBACK_APP_ID = "mapstrack-snaphy-push-application";
    //TODO CHANGE IT UNTIL THIS

    /*DATA LIST IDs*/
    public static String POST_DETAIL_LIST_CASE_FRAGMENT = "PostDetailsListCaseFragment";
    public static String BOOK_LIST_BOOKS_FRAGMENT = "BookListBooksFragment";
    public static String NEWS_LIST_NEWS_FRAGMENT = "NewsListNewsFragment";
    /*END OF DATA LIST IDs*/

    public static String TRENDING = "trending";
    public static String LATEST = "latest";
    public static String UNSOLVED = "unsolved";
    public static String PUBLISH = "publish";
    public static String CASE = "case";
    public static String BOOK_REVIEW = "book review";
    public static String INTERVIEW = "interview";
    public static String Doctor = "Dr. ";

    public static String PRIMARY = "#428bca";
    public static String INFO = "#5bc0de";
    public static String WARNING = "#f0ad4e";
    public static String SUCCESS = "#5cb85c";


}
