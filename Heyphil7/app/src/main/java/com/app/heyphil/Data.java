package com.app.heyphil;

import android.graphics.Bitmap;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Data {
	public static List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
	public static URL					 link;
	public static String 				cert;
	public static String				Fname;
	public static String 				Lname;
	public static String                last_name;
	public static String                first_name;
	public static String                mi;
	public static String                sex;
	public static String                civil_status;
	public static String                birthday;
	public static String                dep_cert_no;
	public static String                dep_name;
	public static String                dep_type;
	public static String                home_address;
	public static String                home_number;
	public static String                mobile_number;
	public static String                certificate_number;
	public static String                member_type;
	public static String                agreement_name;
	public static String                effectivity_date;
	public static String                expiration_date;
	public static String                bldg_no;
	public static String                street;
	public static String                brgy;
	public static String                city;
	public static String                province;
	public static String                email;
	public static String                agreement_no;
	public static String                policyno;
	public static String                moed;
	public static String                dental;
	public static String                ape;

	public static String 				patient_code;
	public static String 				pef_value;
	public static String 				urinalysis;
	public static String 				ecg;
	public static String 				xray;
	public static String 				ultrasound;
	public static String 				doa;
	public static String 				pos;

	public static String 				ben_pack;
	public static String 				plan_type;
	public static String 				room_desc;
	public static String 				ben_limit;
	public static String 				hospitals;
	public static String				pre_ex;
	public static String 				philhealth;
	public static String 				riders;
	public static String 				room_rate;


	public static String 				depcert;
	public static String 				depname;
	public static String 				detype;
	public static String 				depnames;
	public static String 				dpnames;
	public static List<String> dependent = new ArrayList<String>();

	public static String				case_no;
	public static String				date_availed;
	public static String				date_created;
	public static String				illness;
	public static String				nature;
	public static String				provider;
	public static String				status;
	public static String				utilization;

	public static String				specialization;
	public static String				currentCity;
	public static String				City;
	public static String				sprovider;
	public static String				sproviderad;
	public static Double				lat;
	public static Double				lon;
	public static List<String> lat1 = new ArrayList<String>(); // declare your list outside the listener, or make it as class variable. 
	public static  List<String> lon1 = new ArrayList<String>();
	public static  List<String> name1 = new ArrayList<String>();// declare your list outside the listener, or make it as class variable. 
	public static  List<String> pcode = new ArrayList<String>();
	public static  List<String> tel = new ArrayList<String>();
	public static  List<String> address = new ArrayList<String>();
	public static ArrayList<String> mylist = new ArrayList<String>();
	public static ArrayList<String> slist = new ArrayList<String>();
	public static ArrayList<String> specializationList = new ArrayList<String>();
	public static String loc;
	//provider list(Search Box)
	public static ArrayList<HashMap<String, String>> providerList= new ArrayList<HashMap<String, String>>();
	//for WATSON
	public static Boolean haveConversation;
	public static Integer conversationID;
	public static Integer clientID;
	public static String dialogID;
	public static Double				doctorLat;
	public static Double				doctorLon;
	public static String doctorName;
	public static Boolean isDoctor;
	public static String doctorProvider;
	public static String doctorAddress;
	public static String doctorSpecialization;
	public static String doctorPcode;
	public static String doctorCode;
	public static String Complaint;
	public static boolean cloa;
	public static List<String> productList = new ArrayList<String>();
	public static List<String> productID = new ArrayList<String>();
	public static List<String> productContent = new ArrayList<String>();
	public static Map<String,String> PriceList = new HashMap<String,String>();
	public static ArrayList<HashMap<String, String>> Loahistory= new ArrayList<HashMap<String, String>>();
	public static String caseno;
	public static String LOA;
	public static String ProviderName;
	public static String DoctorName;
	public static String Date;
	public static String age;
	public static String attending;
	public static String from;
	public static String to;
	public static String complaints;
	public static String coordinator;
	public static String initial;


	//hanna 08/01/2016
	public static String nlcUsername;
	public static String nlcPassword;
	public static String nlcClassifierId;

	public static String dialogUsername;
	public static String dialogPassword;
	public static String dialogDialogId;

	public static String ttsUsername;
	public static String ttsPassword;
	public static boolean getCity;
	public static boolean getPro;
	public static Bitmap bitmap;
	public static boolean Bitmap;
	public static boolean logout;
	public static String avatar;
	public static boolean start;
	public static boolean logstat;
	public static String heyphil = "What else can I help you?";
	public static String heyphilOpening = "Hi, I'm Phil. How may I help you today?";
	public static String heyphilHasNoAnswer = "I'm sorry. I can't answer your question at this moment";
	public static String code;
	public static Double Weight;
	public static Double Height;
	public static int spin_gender_value;
	public static int spin_ex_level_value;
	public static String fbprofile;
	public static ArrayList<HashMap<String, String>> favorite;
	public static boolean tts;




}