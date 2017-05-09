package com.orthopg.snaphy.orthopg;

/**
 * Created by Ravi-Gupta on 10/4/2016.
 */
public class Constants {
    public static String TAG = "OrthoPG";
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    public static final String APP_MAIL = "orthopg@mail.com";
    public static final String APP_PHONE = "tel:+91-9643476096";
    public static String APP_PLAY_STORE = "com.orthopg.snaphy.orthopg";
    public static String APP_SHARE_TEXT = "Orthopedic Doctors Largest Network \n\n";
    public static String CONTAINER = "orthopg";
    public static String SELECTED_TAB = "Trending";

    private static String SHA_KEY = "D6:E7:88:AB:D1:8E:4F:88:A8:0D:34:ED:6E:57:FC:9F:EB:8C:7B:CC";
    private static String SHA_KEY_NM = "A0:92:53:11:40:E1:6D:D2:C4:85:B0:FF:D8:77:2F:52:03:CA:8A:C1";
    private static String SHA_KEY_RELEASE = "E9:CD:A7:95:01:F2:A8:84:0C:01:7D:B4:DB:4B:23:C6:7F:21:5F:42";
    public static String CLIENT_ID = "1045211377196-k556pl54qlqrmnin81fm7e7dseud686r.apps.googleusercontent.com";
    private static String CLIENT_SECRET_KEY = "ORFoabt0p6XEOFLmKBsYQGZh";

    public static String MCI_VERIFICATION_TAG = "Complete your MCI verification";
    public static String ERROR_DOWNLOADING_BOOK = "Error Downloading Book";

    public static String BRANCH_IO_INSTANCE = "branchIoInstance";
    public static String BRANCH_IO_URL_PROPERTY = "url";


    //public static String baseUrl = "http://192.168.0.6:3000";
    //public static String baseUrl = "http://192.168.43.21:3000";
   // public static String baseUrl = "http://192.168.0.9:3000";
//    public static String baseUrl = "http://192.168.0.120:3000";
    public static String baseUrl = "http://admin.orthopg.com";
    /*public static String baseUrl = "http://ec2-54-209-33-191.compute-1.amazonaws.com:3000";*/
    public static String apiUrl = baseUrl+"/api";
    public static final String AMAZON_CLOUD_FRONT_URL = "http://d3j3ux0h7dntsg.cloudfront.net";

    public static String OLD_DB_FIELD_FLAG = "_DATA_UPDATED";
    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    //TODO CHANGE IT UNTIL THIS
    //public static String SENDER_ID = "673699478279";
    public static String SENDER_ID = "1045211377196";
    public static String LOOPBACK_APP_ID = "orthopg-snaphy-push-application";

    //SharedPreference
    public static String BOOK_SHARED_PREFERENCE = "bookSharedPreference";

    //Payu Credentials
    public final static String PAYU_MERCHANT_ID = "5076054";
    public final static String PAYU_KEY = "ADq70R";
    public final static String PAYU_SALT = "znBn6Zf1";

    /*DATA LIST IDs*/
    public static String LIST_CASE_FRAGMENT = "ListCaseFragment";
    public static String SAVED_CASE_LIST = "SavedCaseList";
    public static String BOOK_LIST_BOOKS_FRAGMENT = "BookListBooksFragment";
    public static String VIEW_ALL_BOOKS_LIST = "ViewAllBooksListFragment";
    public static String SAVED_BOOKS_LIST = "SavedBooksList";
    public static String NEWS_LIST_NEWS_FRAGMENT = "NewsListNewsFragment";
    public static String EXCEPTED_NEW_ANSWER_LIST = "ExceptedAnswerNewAnswerList";
    public static String SPECIALITY_LIST = "SpecialitiesListSpecialityFragment";
    public static String QUALIFICATION_LIST = "QualificationsListQualificationFragment";
    public static String CUSTOMER_SPECIALITY_LIST = "CustomerSpecialityList";
    public static String UPDATED_CUSTOMER_SPECIALITY_LIST = "UpdatedCustomerSpecialityList";
    public static String CUSTOMER_QUALIFICATION_LIST = "CustomerQualificationList";
    public static String ORDER_HISTORY_LIST = "OrderHistoryList";
    public static String HOME_ANSWER_LIST = "HomeAnswerList";
    public static String VIEW_ALL_BOOKS_IDS_LIST = "ViewAllBooksIdsList";
    /*END OF DATA LIST IDs*/

    /*MOdel IDs*/
    public static String ZOOM_IMAGE_ID = "ZoomImageId";
    public static String VIEW_PAGER_ID = "ViewPagerId";
    public static String NOTIFICATION_ID = "notificationId";
    public static String CASE_PRESENTER_ID = "CasePresenterId";
    public static String SAVED_CASE_PRESENTER_ID = "SavedCasePresenterId";
    public static String ADD_NEW_CASE = "ADD_NEW_CASE";
    public static String EDIT_IN_PROCESS_COMMENT_POST_MODEL = "EDIT_IN_COMMENT_POST";
    public static String EDIT_IN_PROCESS_COMMENT_MODEL = "EDIT_IN_COMMENT_MODEL";
    public static String POST_PUSH_EVENT_DATA = "PostPushEventData";
    public static String GOOGLE_ACCESS_TOKEN = "GoogleAccessTokens";
    public static String MCI_NUMBER = "MciNumber";
    public static String BOOK_DESCRIPTION_ID = "BookDescriptionId";
    public static String BOOK_CATEGORY_ID = "BookCategoryId";
    public static String CASE_PROFILE_DATA = "CaseProfileData";
    public static String SAVED_BOOKS_DATA = "SavedBooksData";
    public static String BRANCH_CASE_ID = "BranchCaseId";
    public static String PAYMENT_BOOK_ID = "PaymentBookId";
    public static String DOWNLOADED_BOOK_ID = "DOWNLOADED_BOOK_ID";
    public static String BOOK_DETAIL_MODEL_VALUE = "BookDetailValue";
    public static String PAYMENT_MODEL_DATA = "PaymentModelData";
    public static String CHECK_SAVED_BOOK_DATA = "CheckSavedBookData";
    public static String PAYU_PAYMENT_ID = "PayuPaymentId";
    public static String GENERATED_TRANSACTION_ID = "GeneratedTransactionId";
    /*---------------*/

    /*IDs*/
    public static String LOGIN_CUSTOMER = "loginCustomer";
    public static String GOOGLE_API_CLIENT = "googleApiClient";
    public static String TRACK_LIKE = "trackLike";
    public static String TRACK_SAVE = "trackSave";

    public static String TRENDING = "trending";
    public static String LATEST = "latest";
    public static String UNSOLVED = "unsolved";
    public static String SAVED = "saved";
    public static String POSTED = "posted";
    public static String PUBLISH = "publish";
    public static String ALLOW = "allow";
    public static String CASE = "case";
    public static String BOOK_REVIEW = "book review";
    public static String INTERVIEW = "interview";
    public static String NEWS = "news";
    public static String ADV = "advertisement";
    public static String Doctor = "Dr. ";
    public static String ANONYMOUS = "Anonymous";

    public static String PRIMARY = "#428bca";
    public static String INFO = "#5bc0de";
    public static String WARNING = "#f0ad4e";
    public static String SUCCESS = "#5cb85c";

    public static String YEAR = "year";
    public static String MONTH = "month";
    public static String DAY = "day";
    public static String HOUR = "hour";
    public static String MINUTE = "minute";
    public static String SECOND = "second";

    public static String YEARS = "years";
    public static String MONTHS = "months";
    public static String DAYS = "days";
    public static String HOURS = "hours";
    public static String MINUTES = "minutes";
    public static String SECONDS = "seconds";


    public static String SAVED_BOOKS_CATEGORY = "Saved Books";

    public static String BOOK_TYPE = "Book Type";
    public static String EBOOK_BOOK_TYPE = "ebook";
    public static String HARDCOPY_BOOK_TYPE = "hardcopy";


    /*-------------------------------------------------------------------------------------------------------*/
    /*MESSAGES AREA*/
    public static String ERROR_MESSAGE = "Cannot login ! Try again later";
    public static String UPLOAD_ERROR = "Cannot Upload Image ! Try again";
    public static String ERROR_UPDATING_MCI = "Cannot Update MCI! Try again later";
    public static String ERROR_UPDATING_NAME = "Cannot Update Name! Try again later";
    public static String ERROR_FIRST_NAME_EMPTY = "First Name cannot be empty";
    public static String CASE_UPLOAD_ERROR = "Cannot Save! Check your network";
    public static String HEADING_REQUIRED_MESSAGE = "Heading cannot be blank";
    public static String SUCCESS_SAVED = "Case saved";
    public static String SAVING_POST = "Saving case";
    public static String DELETE_ERROR_POST = "Cannot Delete! Try again later.";
    public static String DELETE_SUCCESS_POST = "Case successfully deleted.";
    public static String ACCEPT_ANSWER_ERROR_COMMENT = "Cannot Accept Answer! Try again later";
    public static String BLANK_COMMENT_ERROR = "Comment cannot be blank";
    public static String VERIFICATION_IN_PROGRESS = "Verification is under process";
    public static String NETWORK_ERROR = "Cannot Connect! Check your network";

    public static String NEWS_RELEASE_MESSAGE = "News";
    public static String BOOKS_RELEASE_MESSAGE = "Book Release";
    public static String COMMENT_MESSAGE = "has commented on the post.";
    public static String LIKE_MESSAGE = "has liked on the post.";
    public static String SAVE_MESSAGE = "has saved on the post.";
    public static String OTP = "Enter OTP first";


    /*--------------------------------------------------------------------------------------------------------*/

}
