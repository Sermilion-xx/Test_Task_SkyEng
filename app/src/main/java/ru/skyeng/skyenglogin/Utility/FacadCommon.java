package ru.skyeng.skyenglogin.Utility;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import ru.skyeng.skyenglogin.LoginModule.LoginActivity;
import ru.skyeng.skyenglogin.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * ---------------------------------------------------
 * Created by Sermilion on 26/01/2017.
 * Project: SkyEngLogin
 * ---------------------------------------------------
 * <a href="http://www.ucomplex.org">ucomplex.org</a>
 * <a href="http://www.github.com/sermilion>github</a>
 * ---------------------------------------------------
 */

public class FacadCommon {

    public static void createNotification(String code, Context context, Class aClass, int NOTIFICATION_ID) {
        Intent intent = new Intent(context, aClass);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        Notification noti = new Notification.Builder(context)
                .setContentTitle("Пароль.")
                .setContentText("Ваш пароль для входа: " + code)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent).build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(NOTIFICATION_ID, noti);
        NOTIFICATION_ID++;
    }

    public static void hideKeyboard(Activity context) {
        View view = context.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
