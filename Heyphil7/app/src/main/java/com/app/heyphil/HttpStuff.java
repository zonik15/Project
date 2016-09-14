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

//import com.squareup.okhttp.Credentials;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;*/

/**
 * Created by HCNatividad on 6/23/2016.
 */
public class HttpStuff extends AsyncTask<String, Integer, String> {

    final String baseURL ="https://gateway.watsonplatform.net/natural-language-classifier/api/v1/classifiers/";

    //add data specific to your instance here
    final String classifierID = Data.nlcClassifierId;
    final String username = Data.nlcUsername;
    final String password = Data.nlcPassword;

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
    final String INTENT_MY_ACCOUNT = "my-account";
    final String INTENT_UTILIZATION = "my-utilization";
    final String INTENT_MEDICAL_INFO = "my-medical";
    final String INTENT_CARD_INFO = "my-card";
    final String INTENT_BENEFITS = "my-benefits";
    final String INTENT_DEPENDENTS = "my-dependents";
    final String INTENT_DENTIST = "find-dentist";
    final String INTENT_BIRTHDAY = "my-birthday";
    final String INTENT_ADDRESS = "my-address";
    final String INTENT_VALIDITY = "my-validity";
    final String INTENT_MY_NAME = "my-name";
    final String INTENT_ACTION_LOGOUT = "action-logout";

    public String questions = "";
    public String url;
    public String providerUrl;
    String replyText;
    String nlcTopic = null;

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

            nlcTopic = topClass;
            //parsing faqAPI
            System.out.println("NLC RESULT INTENT IS "+topClass);
            String faqAPI = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/HeyPhilFAQ/?Topic="+topClass;
            System.out.println("STRING URL FROM HTTPSTUFF == "+faqAPI);
            new loadAPI().execute(faqAPI);
            /*
            if(topClass.contains(INTENT_PROVINCE)){
                chatBubbleActivity.byArea();
                replyText = "0";
            }else if(topClass.contains(INTENT_AFFILIATED_PHILCARE)){
                String url = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchHospitals/?CertNo="+certificate+"&Type=OP&City=&Hospital";
                //this.url = url;
                //new retrievedata().execute(url);
                chatBubbleActivity.byCities();
                replyText = "0";
            }else if(topClass.contains(INTENT_LIST_PRODUCTS)){
                chatBubbleActivity.Products();
                replyText = "0";
                result = "0";
                // get json string from url
            }else if(topClass.contains(INTENT_RESPOND_LOA)){
                System.out.println("SHOULD BE RESPONDING FOR LOA===================");
                chatBubbleActivity.updateResult("Here are the providers nearby your current location. You can choose any location or specific provider you are looking for. Tap or click the google marker to view information an to proceed to create or request LOA. Thank you!");
                Data.City=Data.currentCity;
                chatBubbleActivity.callMain();
                replyText = "0";
                result = "0";
            }else if(topClass.contains(INTENT_DOCTOR)){
                chatBubbleActivity.byLOA();
                replyText = "0";
                result = "0";
            }else if(topClass.contains(INTENT_MY_ACCOUNT)){
                chatBubbleActivity.updateResult("You can view your basic information.");
                chatBubbleActivity.MyAccount();
                replyText = "0";
            }else if(topClass.contains(INTENT_UTILIZATION)){
                chatBubbleActivity.Utilization();
                chatBubbleActivity.updateResult("You can view your utilization.");
                replyText = "0";
            }else if(topClass.contains(INTENT_MEDICAL_INFO)){
                chatBubbleActivity.MyMedicalinfo();
                chatBubbleActivity.updateResult("You can view your latest annual physical examination.");
                replyText = "0";
            }else if(topClass.contains(INTENT_CARD_INFO)){
                chatBubbleActivity.MyMedicalinfo();
                chatBubbleActivity.updateResult("You can view your card information.");
                replyText = "0";
            }else if(topClass.contains(INTENT_BENEFITS)){
                chatBubbleActivity.MyBenefits();
                chatBubbleActivity.updateResult("You can view your benefits package plan.");
                replyText = "0";
            }else if(topClass.contains(INTENT_DEPENDENTS)){
                chatBubbleActivity.Dependent();
                chatBubbleActivity.updateResult("You can view your list of dependents.");
                replyText = "0";
            }else if(topClass.contains(INTENT_DENTIST)){
                chatBubbleActivity.Dental();
                chatBubbleActivity.updateResult("Here are the list of dental providers, you may choose your prefer dental provider to view the doctor's availabilty.");
                replyText = "0";
            } else{
                System.out.println("SHOULD GET RESPONSE"+getResponse(topClass));
                replyText = getResponse(topClass);
                if(replyText.equals("0")){
                    //chatBubbleActivity.searchDialog(topClass);
                    chatBubbleActivity.searchDialog(questions);
                    replyText = "0";
                }else{
                    //chatBubbleActivity.updateResult("Please type hospital");
                }
            }*/

            result = replyText;

        }
        catch (JSONException ex)
        {
            //probably not a JSON
            result = "Hmm..."+retVal;
        }
        /*
        pdia.dismiss();
        System.out.println(" RESULT  ====== " + result);
        if(result.equals("0")){
        }else{
            chatBubbleActivity.updateResult(result);
        }*/
    }

    private class loadAPI extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {}

        protected String doInBackground(String... params)
        {
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(params[0], ServiceHandler.GET);
            Log.d("Response: ", ">>>FROM HTTPSTUFF" + jsonStr);
            String check = "false";
            if (jsonStr != null) {
                JSONArray jobjectness;
                try {

                    JSONObject jobj = new JSONObject(jsonStr);

                    jobjectness = jobj.getJSONArray("HeyPhilFAQResult");

                    JSONObject jarr3 = null;

                    if(jobjectness.length()>0){

                        for (int i = 0; i < jobjectness .length(); i++) {

                            jarr3 = jobjectness.getJSONObject(i);
                            System.out.println("GETTING DATA FROM FAQ ===== "+jarr3.getString("Answer"));
                            check = jarr3.getString("Answer");
                        }

                    }else{

                        check = "I forgot my answer!!";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return check;
        }


        protected void onPostExecute(String stream)
        {
            String result = "none";
            System.out.println("STREAM VALUE IS "+stream);
            if(nlcTopic.contains(INTENT_PROVINCE)){
                chatBubbleActivity.byArea();
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_AFFILIATED_PHILCARE)){
                String url = "https://apps.philcare.com.ph/PhilcareWatsonTest/Search.svc/SearchHospitals/?CertNo="+certificate+"&Type=OP&City=&Hospital";

                try{
                    Data.getCity=true;
                    Data.City=Data.currentCity;
                    chatBubbleActivity.byCities();
                    replyText = "0";
                }catch(NullPointerException e){
                    System.out.println("onActivityResult consume crashed");
                }finally {

                }

            }else if(nlcTopic.contains(INTENT_LIST_PRODUCTS)){
                Data.avatar="Salesman";
                chatBubbleActivity.Products();
                replyText = "0";
                result = "0";

            }else if(nlcTopic.contains(INTENT_RESPOND_LOA)){
                System.out.println("SHOULD BE RESPONDING FOR LOA===================");
                //chatBubbleActivity.updateResult("Here are the providers nearby your current location. You can choose any location or specific provider you are looking for. Tap or click the google marker to view information an to proceed to create or request LOA. Thank you!");
                Data.avatar="Agent";
                Data.getCity=true;
                Data.City=Data.currentCity;
                chatBubbleActivity.callMain();
                replyText = "0";
                result = "0";
            }else if(nlcTopic.contains(INTENT_DOCTOR)){
                Data.avatar="Doctor";
                chatBubbleActivity.byLOA();
                replyText = "0";
                result = "0";
            }else if(nlcTopic.contains(INTENT_MY_ACCOUNT)){
                chatBubbleActivity.updateResult("You can view your basic information.");
                chatBubbleActivity.MyAccount();
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_UTILIZATION)){
                chatBubbleActivity.Utilization();
                chatBubbleActivity.updateResult("You can view your utilization.");
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_MEDICAL_INFO)){
                chatBubbleActivity.MyMedicalinfo();
                chatBubbleActivity.updateResult("You can view your latest annual physical examination.");
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_CARD_INFO)){
                chatBubbleActivity.MyCardInformation();
                chatBubbleActivity.updateResult("You can view your card information.");
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_BENEFITS)){
                chatBubbleActivity.MyBenefits();
                chatBubbleActivity.updateResult("You can view your benefits package plan.");
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_DEPENDENTS)){
                chatBubbleActivity.Dependent();
                chatBubbleActivity.updateResult("You can view your list of dependents.");
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_DENTIST)){
                chatBubbleActivity.Dental();
                chatBubbleActivity.updateResult("Here are the list of dental providers, you may choose your prefer dental provider to view the doctor's availabilty.");
                replyText = "0";
            }else if(nlcTopic.contains(INTENT_CURRENT_LOCATION)){
                replyText = Data.currentCity;
            }else if(nlcTopic.contains(INTENT_BIRTHDAY)){
                replyText = Data.birthday;
            }else if(nlcTopic.contains(INTENT_VALIDITY)){
                replyText = Data.expiration_date;
            }else if(nlcTopic.contains(INTENT_ADDRESS)){
                replyText = Data.home_address;
            }else if(nlcTopic.contains(INTENT_MY_NAME)){
                replyText = "Your name is "+Data.first_name;
            }else if(nlcTopic.contains(INTENT_ACTION_LOGOUT)){
                chatBubbleActivity.logout();
                replyText = "0";
            }
            else{
                System.out.println("SHOULD GET RESPONSE"+getResponse(nlcTopic));
                //replyText = getResponse(nlcTopic);
                //answer from parse of FAQ API
                //replyText = getResponse(stream);
                replyText = stream;
                if(replyText.equals("0")){
                    //chatBubbleActivity.searchDialog(topClass);
                    chatBubbleActivity.searchDialog(questions);
                    replyText = "0";
                }else{

                    //chatBubbleActivity.updateResult("Please type hospital");
                }
            }

            pdia.dismiss();
            System.out.println(" LOAD API RESULT ====== " + result);
            if(replyText.equals("0")){

            }else{
                chatBubbleActivity.updateResult(replyText);
            }
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

                    //chatBubbleActivity.callMain();

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