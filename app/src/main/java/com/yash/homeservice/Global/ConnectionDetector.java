package com.yash.homeservice.Global;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import static android.content.Context.CONNECTIVITY_SERVICE;


public abstract class ConnectionDetector {

        public static boolean isNetworkConnected(Context context)
    {
        boolean haveWifi=false;
        boolean haveData=false;

        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfo=connectivityManager.getAllNetworkInfo();

        for (NetworkInfo info: networkInfo)
        {
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    haveWifi=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    haveData=true;
        }

        return haveWifi || haveData;
    }
}
