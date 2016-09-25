package com.app.heyphil;

import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

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

import java.io.IOException;
import java.util.List;


/**
 * Created by HCNatividad on 8/25/2016.
 */
public class WebAPI {
    ChatBubbleActivity chatBubbleActivity = null;
    public static final String DIALOG_NAME = "phil-dialog";
    private DialogService DialService;
    private List<Dialog> dialogs;
    private String Tag = "WATSON";

    private Conversation conversation;
    public Integer clientId;

    public WebAPI(ChatBubbleActivity act)
    {
        chatBubbleActivity = act;
    }

    public void setDialog(String url){
        new loadAPI().execute(url);
    }

    private class loadAPI extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {}

        protected String doInBackground(String... params)
        {
            ServiceHandler sh = new ServiceHandler();
            //String Producturl="http://philcare.com.ph/api/heyphil/api.php/credentials";
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(params[0], ServiceHandler.GET);
            Log.d("Response: ", ">>>FROM WebAPI " + jsonStr);
            String check = "false";
            if (jsonStr != null) {
                JSONObject jobjectness;
                try {
                    ////whole response
                    JSONObject jobbb = new JSONObject(jsonStr);
                    //wp_post content
                    jobjectness = jobbb.getJSONObject("dialogs");
                    //records content
                    JSONArray jarr2 = jobjectness.getJSONArray("records");
                    if(jarr2.length()>0){
                        System.out.println("WEB API ==== "+jarr2.length());
                        for (int i = 0; i < jarr2.length(); i++) {
                            ///reco
                            JSONArray jarr3 = new JSONArray(jarr2.get(i).toString());
                            Data.conversationID = jarr3.getInt(2);
                            Data.clientID = jarr3.getInt(3);
                        }
                        check = "true";
                    }else{
                        //SAVE DIALOG data to API

                        check = "false";
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
            if(stream.contains("true")){
                System.out.println("POST EXECUTE WEB API === "+stream);
                String url = "http://philcare.com.ph/api/heyphil/api.php/credentials";
                new loadDialog().execute(url);
            }else{
                System.out.println("POST EXECUTE WEB API === "+stream);
                initDialog();
            }


        }
    }

    private class loadDialog extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {}

        protected String doInBackground(String... params)
        {
            if(!Data.start) {
                Data.start=true;
                chatBubbleActivity.updateResult1(Data.heyphilOpening);
            }
            else {
                if(Data.logstat) {
                    Data.logstat=false;
                    chatBubbleActivity.updateResult1(Data.heyphilOpening);
                    //chatBubbleActivity.updateResult("Welcome back "+Data.first_name+" ! How can I help you today?");
                }
                else{
                    chatBubbleActivity.updateResult1(Data.heyphilOpening);
                    //chatBubbleActivity.updateResult(Data.heyphil);
                }
            }
            /* This is to remove DIALOG SERVICE
            System.out.println("POST EXECUTE LOAD DIALOG ");
            DialService = new DialogService();
            DialService.setUsernameAndPassword(Data.dialogUsername, Data.dialogPassword);
            System.out.println("GET DIALOG");
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
                        conversation = DialService.createConversation(Data.dialogID);
                        chatBubbleActivity.updateResult( conversation.getResponse().get(0));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
            */
            return null;
        }


        protected void onPostExecute(String stream)
        {

        }
    }


    public void initDialog(){
        DialService = new DialogService();
        DialService.setUsernameAndPassword(Data.dialogUsername, Data.dialogPassword);
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    //Get Dialogs via DialogService
                    dialogs = DialService.getDialogs().execute();
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

                        conversation = DialService.createConversation(Data.dialogID).execute();
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

    public void postData() throws JSONException{
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        //HttpPost httpPost = new HttpPost("http://philcare.com.ph/heyphil-api/api/dialogs/add.json?&token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOjQsImV4cCI6MTQ2OTAwNTYxNn0.VtkJYrgQHqp7WSQaY23F0gW6V75W1IUxOfp96Gun6qw");
        HttpPost httpPost = new HttpPost("http://philcare.com.ph/api/api.php/dialogs");
        String json = "";
        System.out.println("CONVERSATION ID IS  =================== "+ Data.conversationID);
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
            System.out.println("RESULT FROM INSERTING CONVO =============== " + "Hello, I'm Phil. How can I help you today?");
            if(!Data.start) {
                Data.start = true;
                chatBubbleActivity.updateResult("Hello, I'm Phil. How can I help you today?");
            }
            else {
                if(Data.logstat) {
                    Data.logstat=false;
                    chatBubbleActivity.updateResult("Welcome back "+Data.first_name+" ! How can I help you today?");
                }
                else{
                    chatBubbleActivity.updateResult(Data.heyphil);
                }
            }

        }catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }
}