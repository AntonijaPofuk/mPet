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

import com.raizlabs.android.dbflow.config.FlowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.SkeniranjeDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.SkeniranjeDataLoader;
import Retrofit.DataPost.ObavijestiMethod;
import Retrofit.Model.Skeniranje;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;

import static mpet.project2018.air.mpet.obavijesti.CreateNotificationChannel.CHANNEL_ID;

public class NotificationService extends Service implements SkeniranjeDataLoadedListener {

    private static List<Skeniranje> listaSkeniranja =new ArrayList<>();
    private static int delay=5000;//svakih tolko milisekundi provjerava ako postoji nova obavijesti
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

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

        loadData();//pozivam da se ucita prije handlera
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Posao koji se radi svakih 15 minuta
                    //Provjera ako postoji novo skeniranje
                    loadData();
                    if (listaSkeniranja.size() != 0) {
                        for (Skeniranje skeniranje : listaSkeniranja) {
                            if (skeniranje.procitano.contains("0")) {
                                sendNotification("Vaš ljubimac je skeniran! Pritisnite za detalje ...", "Datum i vrijeme skeniranja : " + skeniranje.datum + " | " + skeniranje.vrijeme,skeniranje.id_skeniranja);
                             /*   mpet.project2018.air.database.entities.Skeniranje lokalnaBazaSkeniranje=new mpet.project2018.air.database.entities.Skeniranje();
                                Date datum=null;
                                try {
                                    datum=format.parse(skeniranje.datum);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                lokalnaBazaSkeniranje.setDatum(datum);
                                lokalnaBazaSkeniranje.setId_skeniranja(Integer.parseInt(skeniranje.id_skeniranja));
                                lokalnaBazaSkeniranje.setKartica(skeniranje.kartica);*/

                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        listaSkeniranja.clear();
                    }

                    handler.postDelayed(this, delay);
                }
            }, delay);  //delay za obavijesti u milisekundama, promijeniti oboje, oboje moraju biti isti

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
    private void sendNotification(String title, String message,String idSkeniranja) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("idSkeniranja",idSkeniranja);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,Integer.parseInt(idSkeniranja), intent,
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
        ObavijestiMethod postNaObavijesti=new ObavijestiMethod();
        postNaObavijesti.Upload("198"); //id prijavljenog korisnika ide tu
        listaSkeniranja.addAll(listaSkeniranjaPreuzeta);
    }
}
