package mpet.project2018.air.mpet.obavijesti;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.SkeniranjeDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.SkeniranjeDataLoader;
import Retrofit.Model.Skeniranje;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;

import static mpet.project2018.air.mpet.obavijesti.CreateNotificationChannel.CHANNEL_ID;

public class NotificationService extends Service implements SkeniranjeDataLoadedListener {

    private static boolean firstLoad=false;
    private static List<Skeniranje> listaSkeniranja =new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Example Service")
                //.setSmallIcon(R.drawable.ic_launcher_background) -->ikona
                .setContentText(input)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Posao koji se radi svakih 15 minuta
                //Provjera ako postoji novo skeniranje
                loadData();

                if(listaSkeniranja.size()!=0){
                    for (Skeniranje skeniranje: listaSkeniranja) {
                        if(skeniranje.procitano.contains("0")){
                            sendNotification("Vaš ljubimac je skeniran! Pritisnite za detalje ...","Datum i vrijeme skeniranja : "+skeniranje.datum+" | "+skeniranje.vrijeme);
                        }
                    }
                    listaSkeniranja.clear();
                }

                handler.postDelayed(this, 60000*15);
            }
        }, 60000*15);  //delay za obavijesti u milisekundama, promijeniti oboje, oboje moraju biti isti

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //funkcija za slanje obavijesti na desktop
    private void sendNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int icon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? R.drawable.ic_launcher_background : R.mipmap.ic_launcher;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default";
            NotificationChannel channel = new NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(message);
            channel.enableLights(true);
            channel.setLightColor(Color.BLUE);
            channel.setSound(defaultSoundUri, null);
            channel.setShowBadge(true);
            notificationManager.createNotificationChannel(channel);
            Notification notification = new Notification.Builder(this, channelId)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(icon)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build();
            notificationManager.notify(m, notification);
        } else {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent)
                    .setLights(Color.BLUE, 3000, 3000);
            notificationManager.notify(m, notificationBuilder.build());
        }
    }

    public void loadData(){
        SkeniranjeDataLoader skeniranjeDataLoader=new SkeniranjeDataLoader(this);
        skeniranjeDataLoader.loadDataByUserId("198");//Testni ID

    }


    @Override
    public void SkeniranjeOnDataLoaded(List<Skeniranje> listaSkeniranjaPreuzeta) {
        listaSkeniranja.addAll(listaSkeniranjaPreuzeta);
    }
}
