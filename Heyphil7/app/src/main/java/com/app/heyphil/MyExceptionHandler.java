package com.app.heyphil;

import android.content.Context;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by ABDalisay on 9/16/2016.
 */
public class MyExceptionHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;
    public MyExceptionHandler(Context context) {

        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {

        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        System.err.println(stackTrace);// You can use LogCat too
       /* Intent intent = new Intent(myContext, myActivityClass);
        String s = stackTrace.toString();
        //you can use this String to know what caused the exception and in which Activity
        intent.putExtra("uncaughtException",
                "Exception is: " + stackTrace.toString());
        intent.putExtra("stacktrace", s);
        myContext.startActivity(intent);
        //for restarting the Activity
        Process.killProcess(Process.myPid());
        System.exit(0);*/
    }
}
