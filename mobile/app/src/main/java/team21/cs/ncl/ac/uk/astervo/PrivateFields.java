package team21.cs.ncl.ac.uk.astervo;

public class PrivateFields {

    //BASE URL
    public static final String BASE_URL = "https://astervo.herokuapp.com";

    //LOGIN POST
    public static final String TAG_USER = "user";
    public static final String TAG_EMAIL = "email";
    public static final String TAG_PASS = "password";
    public static final String TAG_PASS_CONF = "password_confirmation";

    //LOGIN RESPONSE
    public static final String TAG_DATA = "data";
    public static final String TAG_AUTH = "auth_token";
    public static final String TAG_SUCCESS = "success";
    public static final String TAG_INFO = "info";
    public static final String TAG_ERROR = "error";
    public static final String TAG_ACC = "accounts";

    //ACCOUNTS
    public static final String TAG_TYPE = "type";
    public static final String TAG_ACC_ID = "id";
    public static final String TAG_BAL = "balance";
    public static final String TAG_INTEREST = "interest";

    //TRANSFER POST
    public static final String TAG_TRANS = "transfer";
    public static final String TAG_TRANS_TO = "to";
    public static final String TAG_TRANS_FROM = "from";
    public static final String TAG_TRANS_AMOUNT = "amount";

    //TRANSFER RESPONSE
    public static final String TAG_TRANS_RESULT = "result";
    public static final String TAG_MESSAGE = "message";

    //TRANSFER LIST RESPONSE
    public static final String TAG_TRANS_ARRAY = "transfers";
    public static final String TAG_TRANS_DATE = "date";
    public static final String TAG_TRANS_SENDER = "sender";
    public static final String TAG_TRANS_RECEIVER = "receiver";

    //QUESTIONS RESPONSE
    public static final String TAG_QUESTIONS = "questions";
    public static final String TAG_QUESTION = "question";
    public static final String TAG_ANSWERS = "answers";
    public static final String TAG_ANSWER = "answer";
    public static final String TAG_Q_ID = "id";

    //QUIZ RESPONSE
    public static final String TAG_SCORE = "score";
}