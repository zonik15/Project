package com.app.heyphil;

/**
 * Created by HCNatividad on 6/23/2016.
 */
class GreetingsResponse {

    public int index = 0;
    String arrayGreeting[] = {"I hope you are having a nice day.",
            "Have a great day!",
            "I am having a great day!",
            "Be happy!",
            "Thank you!",
            "Good day!",
            ":)"};
    String arrayConvo[] = {
            "I am doing okay.",
            "I am okay.",
            "Today is a good day. Hopefully yours too.",
            "I am a little bit busy but I always have time for you."
    };

    String arrayFeelings[] = {
            "Everything's gonna be alright",
            "Dont think too much",
            "Be happy.",
            "Smile. Dont be sad."
    };

    String arrayPhilosophy[] = {
            "My wisdom is different with yours.",
            "Let's all take a moment to review our decision in our life.",
            "Don't be serious.",
            "Yes.",
            "I dont't know have all the answers to your question."
    };

    String arrayUser[] = {
            "Are you even human?",
            "Please be serious.",
            "I think you're drunk"
    };

    String arrayWatson[] = {
            "If you think I am human, then I am human.",
            "If you think I am robot, then I am robot.",
            "Are you trying to check if I am robot?"
    };

    public GreetingsResponse(){
        super();
    }

    public String searchGreetings(){
        String greet = arrayGreeting[index];
        index++;
        if (index == arrayGreeting.length){
            index = 0;
        }
        return greet;
    }

    public String searchConvo(){
        String convo = arrayConvo[index];
        index++;
        if (index == arrayConvo.length){
            index = 0;
        }
        return convo;
    }

    public String searchFeelings(){
        String feelings = arrayFeelings[index];
        index++;
        if (index == arrayFeelings.length){
            index = 0;
        }
        return feelings;
    }

    public String searchPhilosophy(){
        String philo = arrayPhilosophy[index];
        index++;
        if (index == arrayPhilosophy.length){
            index = 0;
        }
        return philo;
    }

    public String searchUser(){
        String user = arrayUser[index];
        index++;
        if (index == arrayUser.length){
            index = 0;
        }
        return user;
    }

    public String searchWatson(){
        String watson = arrayWatson[index];
        index++;
        if (index == arrayWatson.length){
            index = 0;
        }
        return watson;
    }

}
