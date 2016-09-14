package com.app.heyphil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;

import com.ibm.watson.developer_cloud.dialog.v1.DialogService;
import com.ibm.watson.developer_cloud.dialog.v1.model.Conversation;
import com.ibm.watson.developer_cloud.dialog.v1.model.Dialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by HCNatividad on 7/14/2016.
 */
public class HeyPhilApi extends AsyncTask<String,String,String>{
    //IBM WATSON
    public static final String DIALOG_NAME = "phil-dialog";
    private DialogService DialService;
    private Conversation conversation;
    private String conversationResult="";
    private boolean watsonInited = false;
    private String dialogId= "";
    private List<Dialog> dialogs;
    private String Tag = "WATSON";
    public Integer clientId;
    public List dialogList;
    private Activity activity;
    ChatBubbleActivity chatBubbleActivity = null;

    public HeyPhilApi(ChatBubbleActivity act)
    {
        chatBubbleActivity = act;

    }
    public HeyPhilApi(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {
            // check your log for json response
            String stream = null;
            String urlString = strings[0];
            System.out.println("URL FROM CHATBUBBLEACTIVITY START DIALOG =====  "+strings[0]);
            JSONParser hh = new JSONParser();
            stream = hh.GetHTTPData(urlString);
            System.out.println("AFTER PARSING");
            // Return the data from specified url
            return stream;
        }
        protected void onPostExecute(String stream) {

            if (stream != null) {
                try {
                    System.out.println("STREAMINGGGGGGG");
                    JSONObject object = new JSONObject(stream);
                    System.out.println("AFTER   STREAMINGGGGGGG" + object);
                    String getSuccess = object.getString("dialog");
                    System.out.println("TRY ================== "+ getSuccess);
                    if(getSuccess.contains("fail")){
                        System.out.println("NO CERTIFICATE NUMBER IN THE DATABASE  ======================");
                        initDialog();


                    }else{
                        System.out.println("else ================== "+ getSuccess);
                        JSONArray Jarray = object.getJSONArray("dialog");
                        String data = "";
                        //getDialog();
                        System.out.println("START PARSING FROM PHILCARE ===== "+Jarray.length());
                        System.out.println("CONTENT OF HEYPHIL API ======== "+ Jarray);
                        if(Jarray.length()>0){
                            for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject Jasonobject = null;
                                Jasonobject = Jarray.getJSONObject(i);
                                Data.conversationID = Jasonobject.getInt("dialog_profile");
                                Data.clientID = Jasonobject.getInt("client_profile");

                            }
                            chatBubbleActivity.updateResult("Hello, I'm Phil. How can I help you today?");
                        }
                    }

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
        }

    protected void onPreExecute()
    {

        //mainActivity.analyzingResult("Analyzing your question...");
    }



    public void initDialog(){
        DialService = new DialogService();
        DialService.setUsernameAndPassword("e8a2c738-2a5f-41be-8cc3-af55ac6bbfab", "Zp2AVhgESBn0");
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //Get Dialogs via DialogService
                    dialogs = DialService.getDialogs();
                    if (dialogs.size() > 0) {
                        for (int i=0; i< dialogs.size(); i++) {
                            Log.d(Tag, "Dialog[" + i + "].Name=" + dialogs.get(i).getName());
                            Log.d(Tag, "Dialog[" + i + "].ID=" + dialogs.get(i).getId());
                            if (dialogs.get(i).getName().equals(DIALOG_NAME)) {
                                Data.dialogID = dialogs.get(i).getId();

                            }
                        }
                    }else {
                        Log.d(Tag, "There is no dialog!!!");
                    }
                    //Dialog[0]=pizza, Dialog[1]=liza
                    //conversation = DialService.createConversation(dialogs.get(1).getId());
                    if (!Data.dialogID.equals("")) {

                        conversation = DialService.createConversation(Data.dialogID);
                        clientId = conversation.getId();
                        Data.conversationID = conversation.getId();
                        Data.clientID = conversation.getClientId();
                        System.out.println("CLIENT ID ==================== "+clientId);
                        postData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }).start();
    }

    public void getDialog(){
        DialService = new DialogService();
        DialService.setUsernameAndPassword("e8a2c738-2a5f-41be-8cc3-af55ac6bbfab", "Zp2AVhgESBn0");
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    dialogs = DialService.getDialogs();
                    if (dialogs.size() > 0) {
                        for (int i=0; i< dialogs.size(); i++) {
                            if (dialogs.get(i).getName().equals(DIALOG_NAME)) {
                                Data.dialogID = dialogs.get(i).getId();
                            }
                        }
                    }else {
                        Log.d(Tag, "There is no dialog!!!");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }).start();
    }
    public void postData() throws JSONException{
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://philcare.com.ph/heyphil-api/api/dialogs/add.json?&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjQsImV4cCI6MTQ2OTAwNTYxNn0.VtkJYrgQHqp7WSQaY23F0gW6V75W1IUxOfp96Gun6qw");
        String json = "";
        System.out.println("CONVERSATION ID IS  =================== "+Data.conversationID);
        try {
            // JSON data:
            //json.put("certificate_no", Data.cert);
            //json.put("dialog_profile", Data.conversationID);

            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("dialog_profile", Data.conversationID);
            jsonObject.accumulate("certificate_no", Data.cert);
            jsonObject.accumulate("client_profile", Data.clientID);

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            // 9. receive response as inputStream
            System.out.println("RESULT FROM INSERTING CONVO =============== " + httpResponse.getEntity().getContent());
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            //ChatBubbleActivity chat = new ChatBubbleActivity();
            //chat.initWatsonService();
            chatBubbleActivity.updateResult("Hello, I'm Phil. How can I help you today?");

        }catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }


}
