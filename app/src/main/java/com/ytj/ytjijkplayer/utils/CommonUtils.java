package com.ytj.ytjijkplayer.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/11.
 */

public class CommonUtils {
    public static void setHeight(Context context, View view, int n, boolean dip) {
        if (view != null) {
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            if (n > 0 && dip) {
                n = dip2pixel(context, n);
            }
            lp.height = n;
            view.setLayoutParams(lp);
        }
    }

    public static int dip2pixel(Context context, float n) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, n,
                context.getResources().getDisplayMetrics());
    }
}
