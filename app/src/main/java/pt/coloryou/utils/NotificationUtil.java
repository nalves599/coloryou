package pt.coloryou.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationUtil {

    public static final String CHANNEL_COLOR_PICKER= "colorPickerChannel";

    public static void createNotificationChannels(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel colorPickerChannel = new NotificationChannel(
                    CHANNEL_COLOR_PICKER,
                    "colorPicker",
                    NotificationManager.IMPORTANCE_HIGH
            );
            colorPickerChannel.setDescription("This is Color Picker Channel");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(colorPickerChannel);
        }
    }
}
