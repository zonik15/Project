package com.app.heyphil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HCNatividad on 6/23/2016.
 */
public class HttpStuff extends AsyncTask<String, Integer, String> {

    final String baseURL ="https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/";

    //add data specific to your instance here
    final String classifierID = "34175ex88-nlc-6834";
    final String username="91b3aef7-5145-4fc8-ad3b-e6786e0f898c";
    final String password = "uWr2Psl1oKX3";
    private ProgressDialog pdia;

    final String INTENT_RESPOND_GREETINGS = "respond-greetings";
    final String INTENT_RESPONSE_ABSOLUTE = "response-absolute";
    final String INTENT_CONVO_START = "convo-start";
    final String INTENT_CONVO_FEELINGS = "convo-feelings";
    final String INTENT_WHAT_PHILCARE = "what-philcare";
    final String INTENT_HISTORY_PHILCARE = "history-philcare";
    final String INTENT_HISTORY_ESTABLISHED = "history-established";
    final String INTENT_MISSION_PHILCARE = "mission-philcare";
    final String INTENT_VISION_PHILCARE = "vision-philcare";
    final String INTENT_CHAIRMAN_PHILCARE = "chairman-philcare";
    final String INTENT_DIRECTOR_PHILCARE = "director-philcare";
    final String INTENT_INDIVIDUAL_PACKAGE = "individual-package";
    final String INTENT_ER_SHIELD = "er-shield";
    final String INTENT_ER_VANTAGE = "er-vantage";
    final String INTENT_HEALTH_VANTAGE = "health-vantage";
    final String INTENT_BUSINESS_PACKAGE = "business-package";
    final String INTENT_AFFILIATED_PHILCARE = "affiliated-philcare";
    final String INTENT_NON_COVERED = "non-covered";
    final String INTENT_REPLACE_PHILCARE = "replace-philcare";
    final String INTENT_GET_PHILCARE = "get-philcare";
    final String INTENT_APPOINTMENT_PHILCARE = "appointment-philcare";
    final String INTENT_UTILIZATION_PHILCARE = "utilization-philcare";
    final String INTENT_FORMS_PHILCARE = "forms-philcare";
    final String INTENT_CONTINUE_PHILCARE = "continue-philcare";
    final String INTENT_GIFT_PHILCARE = "gift-philcare";
    final String INTENT_CERTIFICATE_NUMBER = "certificate-number";
    final String INTENT_PROVINCE = "philcare-province";
    final String INTENT_LIST_PRODUCTS = "list-products";
    final String INTENT_RESPOND_OFF_TOPIC_PHILOSOPHY = "respond-off-topic-philosophy";
    final String INTENT_RESPOND_OFF_TOPIC_USER_FOCUS = "respond-off-topic-user-focus";
    final String INTENT_RESPOND_OFF_TOPIC_WATSON_FOCUS = "respond-off-topic-watson-focus";
    final String INTENT_JOKE = "respond-off-topic-joke-or-riddle";
    final String INTENT_MENU = "respond-menu";
    final String INTENT_SUPPORT = "respond-support";
    final String INTENT_HUGOT = "respond-hugot";
    final String INTENT_PASSWORD = "forgot-password";
    final String INTENT_RANDOM_NAMES = "respond-random-names";
    final String INTENT_RANDOM_NEWS = "respond-news";
    final String INTENT_OFF_TOPIC_NONSENSE = "respond-off-topic-nonsense-input";
    final String INTENT_RESPOND_START = "respond-start";
    final String INTENT_RESPOND_LOA = "respond-loa";
    final String INTENT_CURRENT_LOCATION = "respond-location";
    final String INTENT_RESET = "action-reset";
    final String INTENT_DOCTOR = "respond-doctor";

    public String questions = "";
    public String url;
    public String providerUrl;
    String replyText;

    Intent intent;
    JSONArray user = null;
    JSONParser jsonparser = new JSONParser();
    TextView tv;
    public String ab;
    JSONObject jobj = null;


    public String prov = null;

    public String[] myArr;
    ChatBubbleActivity chatBubbleActivity = null;
    ChatArrayAdapter chatArrayAdapter = null;
    public String certificate;
    private Activity activity;
    public HttpStuff(ChatBubbleActivity act)
    {
        chatBubbleActivity = act;
        certificate = Data.cert;
    }

    public HttpStuff(Activity activity) {
        this.activity = activity;
    }



    @Override
    protected String doInBackground(String... question) {
        String retVal = "none";
        //build a request to talk to Watson
        OkHttpClient client = new OkHttpClient();
        String creds = Credentials.basic(username, password);
        Request request = new Request.Builder()
                .url(baseURL + classifierID + "/classify?text=" + question[0])
                .header("Authorization",creds)
                .build();

        Response response = null;
        questions = question[0];
        try {
            response = client.newCall(request).execute();
            retVal = response.body().string();
        }
        catch (IOException ex)
        {
            retVal="Uh oh..:"+ ex.getMessage();
        }
        return retVal;
    }


    protected void onProgressUpdate(Integer... progress) {
        //TODO
    }

    public void setUrl(String u){
        this.url = u;
    }
    @Override
    protected void onPostExecute(String retVal) {
        //network call came back. lets parse the resultant JSON
        String result = "none";
        String topClass = "none";
        System.out.println(retVal);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String provider = null;
        JSONArray dataJsonArr = null;
        try {
            JSONObject answer = new JSONObject(retVal);
            topClass = answer.get("top_class").toString();
            // Getting JSON from URL
            System.out.print(" what is intent  ====== "+topClass);
            System.out.println("CERTificAte is ====================== "+certificate);

            if(topClass.contains(INTENT_PROVINCE)){
                chatBubbleActivity.byCities();

                replyText = "0";
            }else if(topClass.contains(INTENT_AFFILIATED_PHILCARE)){
                String url = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchHospitals/?CertNo="+certificate+"&Type=OP&City=&Hospital";
                //this.url = url;
                //new retrievedata().execute(url);
                chatBubbleActivity.byCities();

                replyText = "0";

            }else if(topClass.contains(INTENT_LIST_PRODUCTS)){
                chatBubbleActivity.callProduct();
                replyText = "0";
                result = "0";
                // get json string from url
            }else if(topClass.contains(INTENT_RESPOND_LOA)){
                System.out.println("SHOULD BE RESPONDING FOR LOA===================");
                //chatBubbleActivity.updateResult("Please type hospital");
                //activity.startActivity(new Intent(activity, MainActivity.class));

                //Intent intent = new Intent(activity, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //activity.getApplicationContext().startActivity(intent);
                chatBubbleActivity.callMain();

                replyText = "0";
                result = "0";
            }else if(topClass.contains(INTENT_DOCTOR)){
                chatBubbleActivity.byLOA();
                replyText = "0";
                result = "0";
            }else{
                System.out.println("SHOULD GET RESPONSE"+getResponse(topClass));
                replyText = getResponse(topClass);
                if(replyText.equals("0")){
                    //chatBubbleActivity.searchDialog(topClass);
                    chatBubbleActivity.searchDialog(questions);
                    replyText = "0";
                }else{

                    //chatBubbleActivity.updateResult("Please type hospital");
                }

            }

            result = replyText;

        }
        catch (JSONException ex)
        {
            //probably not a JSON
            result = "Hmm..."+retVal;
        }
        pdia.dismiss();
        System.out.println(" RESULT  ====== " + result);
        if(result.equals("0")){

        }else{
            chatBubbleActivity.updateResult(result);
        }
    }




    public class retrievedata extends AsyncTask<String,String,String>{
        String yourJsonStringUrl = url;
        EditText chat_text;
        final String TAG = "AsyncTaskParseJson.java";

        // contacts JSONArray
        JSONArray dataJsonArr = null;
        @Override
        protected String doInBackground(String... strings) {
            // check your log for json response
            String stream = null;
            String urlString = strings[0];

            JSONParser hh = new JSONParser();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }
        protected void onPostExecute(String stream) {
            if (stream != null) {
                try {
                    JSONObject object = new JSONObject(stream);
                    JSONArray Jarray = object.getJSONArray("SearchHospitalsResult");
                    String data = "";

                    for (int i = 0; i < Jarray.length(); i++) {
                        JSONObject Jasonobject = null;

                        Jasonobject = Jarray.getJSONObject(i);
                        data = "<tr><td><a href='#'>"+Jasonobject.getString("ProviderName")+"</a></td></tr>";
                        // get an output on the screen
                        chatBubbleActivity.updateResult(Html.fromHtml(data).toString());

                        data = "";

                    }
                    String table ="<table>" + data + "</table>";
                    replyText = table;
                    setProvider(table);
                    System.out.println("WHATS HAPPENING "+data);

                    //activity.startActivity(new Intent(activity, MainActivity.class));

                    chatBubbleActivity.callMain();

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
        }
    }
    protected void onPreExecute()
    {
        pdia = new ProgressDialog(chatBubbleActivity);
        pdia.setMessage("Analyzing your question...");
        pdia.show();
        //mainActivity.analyzingResult("Analyzing your question...");
    }

    public String getResponse(String intent){
        GreetingsResponse greeting =  new GreetingsResponse();
        PhilcareResponse philcare = new PhilcareResponse();
        JokesResponse joke = new JokesResponse();
        ConvoResponse convo = new ConvoResponse();
        ProductsResponse product = new ProductsResponse();
        String reply = null;
        System.out.println("intent is "+intent);

        if(intent.equals(INTENT_RESPOND_GREETINGS)){
            System.out.println("RESPOND GREETINGs");
            replyText = greeting.searchGreetings();
            System.out.println("RESPOND GREETINGs ==== "+greeting.searchGreetings());
        }else if(intent.equals(INTENT_CONVO_START )){
            replyText = greeting.searchConvo();
        }else if(intent.equals(INTENT_CONVO_FEELINGS )){
            replyText = greeting.searchFeelings();
        }else if(intent.equals(INTENT_WHAT_PHILCARE)){
            replyText =  philcare.whatPhilcare();
        }else if(intent.equals(INTENT_HISTORY_PHILCARE)){
            replyText = philcare.historyPhilcare();
        }else if(intent.equals(INTENT_HISTORY_ESTABLISHED)){
            replyText = philcare.establishedPhilcare();
        }else if(intent.equals(INTENT_MISSION_PHILCARE)){
            replyText = philcare.missionPhilcare();
        }else if(intent.equals(INTENT_VISION_PHILCARE)){
            replyText = philcare.visionPhilcare();
        }else if(intent.equals(INTENT_CHAIRMAN_PHILCARE)){
            replyText = philcare.chairmanPhilcare();
        }else if(intent.equals(INTENT_DIRECTOR_PHILCARE)){
            replyText = philcare.directorPhilcare();
        }else if(intent.equals(INTENT_RESPOND_OFF_TOPIC_PHILOSOPHY)){
            replyText = greeting.searchPhilosophy();
        }else if(intent.equals(INTENT_RESPOND_OFF_TOPIC_USER_FOCUS)){
            replyText = greeting.searchUser();
        }else if(intent.equals(INTENT_RESPOND_OFF_TOPIC_WATSON_FOCUS)){
            replyText = greeting.searchWatson();
        }else if(intent.equals(INTENT_JOKE)){
            replyText = joke.searchJokes();
        }else if(intent.equals(INTENT_HUGOT)){
            replyText = convo.searchHugot();
        }else if(intent.equals(INTENT_RANDOM_NAMES)){
            replyText = convo.searchNames();
        }else if(intent.equals(INTENT_RANDOM_NEWS)){
            replyText = convo.searchNews();
        }else if(intent.equals(INTENT_INDIVIDUAL_PACKAGE)){
            replyText = product.searchProducts();
        }else if(intent.equals(INTENT_ER_SHIELD)){
            replyText = product.searchErShield();
        }else if(intent.equals(INTENT_ER_VANTAGE)){
            replyText = product.searchErShield();
        }else if(intent.equals(INTENT_HEALTH_VANTAGE)){
            replyText = product.searchErShield();
        }else if(intent.equals(INTENT_BUSINESS_PACKAGE)){
            replyText = product.searchBgPlus();
            replyText = product.searchHealthAdvantageProgram();
        }else if(intent.equals(INTENT_NON_COVERED)){
            replyText = product.searchNonCoveredDiseases();
        }else if(intent.equals(INTENT_REPLACE_PHILCARE)){
            replyText = product.searchReplaceCard();
        }else if(intent.equals(INTENT_GET_PHILCARE)){
            replyText = product.searchGetCardNumber();
        }else if(intent.equals(INTENT_APPOINTMENT_PHILCARE)){
            replyText = product.searchOnlineAppointment();
        }else if(intent.equals(INTENT_UTILIZATION_PHILCARE)){
            replyText = product.searchUtilization();
        }else if(intent.equals(INTENT_FORMS_PHILCARE)){
            replyText = product.searchGetForms();
        }else if(intent.equals(INTENT_CONTINUE_PHILCARE)){
            replyText = product.searchContinueExisting();
        }else if(intent.equals(INTENT_GIFT_PHILCARE)){
            replyText = product.searchGift();
        }else if(intent.equals(INTENT_CURRENT_LOCATION)){
            System.out.println("======================================>>>>CURRENT CITY");
            replyText = Data.currentCity;
        }else if(intent.equals(INTENT_RESET)){
            chatArrayAdapter.clear();

        }else if(intent.equals("respond-server-error")){
            replyText = "Sorry. One of my cognitive systems is not working at the moment.";
        }else{
            replyText = "0";
        }

        return replyText;
    }

    public void setProvider(String t){
        prov = t;
    }
    public String searchProvider(){
        return prov;
    }

    public void listProviders(){

    }



}