package com.app.heyphil;

/**
 * Created by HCNatividad on 6/22/2016.
 */
public class ConvoResponse {
    public int index = 0;
    String convoArray[] =   {
            "Are you sad? Please say something",
            "I think you forgot to type your question.",
            "Do you need time to formulate your question?",
            "Sometimes I also need a moment to be alone.",
            "Could you type your question again.",
            "Please ask your question again.",
            "I'm getting tired of your silence.",
            "...",
            "?",
            "I think you're losing your connection."
    };

    String dialogArray[] = {
            "Do you still need to ask more question?",
            "Does my answer satisfy you? If not try asking again and be more specific",
            "Please be more specific in your question for me to be able to answer correctly.",
            "Just ask your question about PhilCare",
            "You're asking too many question.",
            "Please don't take advantage of my wisdom."
    };


    String loaArray[] = {
            "Unfortunately I can't process questions under LOA.",
            "I am currently learning about LOA process.",
            "I am sorry to bear the bad news. I can't answer any questions regarding LOA."
    };

    String providerArray[] = {
            "Search for providers is under construction.",
            "I am still studying the search for providers process.",
            "Oh no! I am not done studying the search for providers."
    };

    String medArray[] = {
            "Medical Information in not available at this moment.",
            "Coming soon."
    };

    String hugotArray[] = {
            "Sa LOVE, walang bingi; walang bulag; walang pipi…pero TANGA marami",
            "Kapag sinabihan ka nang: 'Ang ganda mo!', mag-thank you ka na lang. Minsan ka na  nga lang sabihan, mag-iinarte ka pa",
            "Aanhin mo ang palasyo kung walang internet connection dito. Mabuti pang mag-stay sa bahay kubo, sa Wi-Fi zone nakapuwesto",
            "Aanhin mo ang pera’t mansiyon, kung nagmumura ang iyong bilbil at puson",
            "Labs, para kang damit na suot ko ngayon. Simple lang pero bagay sa akin",
            "Kapag mahal mo, ipaglaban mo. Pero kung pinagmumukha ka nang tanga, iwan mo na",
            "Mamatay-matay ka sa selos, hindi naman pala kayo. Ano ba 'yun !",
            "Lagi na lang ninyong sinisisi ang mga taong 'paasa'. Hindi kaya, kasalanan mo dahil 'assuming' ka lang?",
            "Kapag sumama ka sa matatalino, tatalino ka rin. Kapag sumama ka sa adik, magiging adik ka rin. Try mong sumama sa akin, baka sakaling maging akin ka rin",
            "Ang mabuting lalaki, 'stick to one', hindi 3 in 1."
    };

    String namesArray[] = {
            "Maybe they are the developer of this app?",
            "Maybe my neighbor",
            "My boss.",
            "My name in the past."
    };

    String newsArray[] = {
            "You can check this link for current events <a href='http://cnnphilippines.com/' target = '_blank'>CNN</a>",
            "I might not know any news right now so you can check this out, <a href='http://www.gmanetwork.com/news/'>GMA News</a>, <a href='http://news.abs-cbn.com/'>ABS CBN News</a>"
    };

    public ConvoResponse(){
        super();
    }

    public String searchNextQ() {
        String convo = convoArray[index];
        index++;
        if (index == convoArray.length) index = 0;
        return convo;
    }

    public String searchNextDialog(){
        String nextDialog = dialogArray[index];
        index++;
        if(index == dialogArray.length) index = 0;
        return nextDialog;
    }

    public String searchMenu(){
        String menu = "<a href='#' onClick='menuLoa();'>LOA</a><a href='#' onClick='menuProviders();'>Search Providers</a>";
        return menu;
    }

    public String searchLoa(){
        String loa = loaArray[index];
        index++;
        if (index == loaArray.length) index = 0;
        return loa;
    }

    public String searchProviders(){
        String provider = providerArray[index];
        index++;
        if (index == providerArray.length) index = 0;
        return provider;
    }

    public String searchMed(){
        String med = medArray[index];
        index++;
        if (index == medArray.length) index = 0;
        return med;
    }

    public String searchHugot(){
        String hugot = hugotArray[index];
        index++;
        if (index == hugotArray.length) index = 0;
        return hugot;
    }

    public String searchNames(){
        String names = namesArray[index];
        index++;
        if (index == namesArray.length) index = 0;
        return names;
    }

    public String searchNews(){
        String news = newsArray[index];
        index++;
        if (index == newsArray.length) index = 0;
        return news;
    }
}
