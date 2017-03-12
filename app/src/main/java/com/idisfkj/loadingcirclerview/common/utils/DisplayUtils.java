package com.idisfkj.loadingcirclerview.common.utils;

import com.idisfkj.loadingcirclerview.App;

/**
 * Created by idisfkj on 17/3/12.
 * Email : idisfkj@gmail.com.
 */

public class DisplayUtils {

    public static int dip2px(float value) {
        final float scale = App.mAppContext.getResources().getDisplayMetrics().density;
        return (int) (value * scale + 0.5f);
    }
}
