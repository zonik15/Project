package com.app.heyphil;

/**
 * Created by HCNatividad on 6/23/2016.
 */
public class JokesResponse {
    public int index = 0;
    String arrayJokes[] = {
            "How many programmers does it take to change a light bulb?\n\nAnswer: None. It’s a hardware problem.",
            "What did the spider do on the computer?\n\nAnswer: A website!",
            "Why did the computer keep sneezing?\n\nAnswer: It had a virus!",
            "What does a baby computer call his father?\n\nAnswer: Data!",
            "Why did the computer squeak?\n\nAnswer: Because someone stepped on it's mouse! ",
            "No more jokes for now",
            "Why is it that programmers always confuse Halloween with Christmas?\n\nAnswer: Because 31 OCT = 25 DEC."
    };


    public JokesResponse(){
        super();
    }

    public String searchJokes(){
        String joke = arrayJokes[index];
        index++;
        if (index == arrayJokes.length){
            index = 0;
        }
        return joke;
    }
}