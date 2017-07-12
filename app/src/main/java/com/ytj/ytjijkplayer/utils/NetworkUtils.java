package com.ytj.ytjijkplayer.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    /**
     * 判断当前网络类型
     */
    public static int getNetworkType(Context context) {
        ConnectivityManager connectMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            /* 没有任何网络 */
            return 0;
        }
        if (!networkInfo.isConnected()) {
            /* 网络断开或关闭 */
            return 1;
        }
        if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
            /* 以太网网络 */
            return 2;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            /* wifi网络，当激活时，默认情况下，所有的数据流量将使用此连接 */
            return 3;
        } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return 4;
        }
        /* 未知网络 */
        return -1;
    }
}
