package com.tunavis.establishment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.getcapacitor.JSObject;
import com.getcapacitor.PluginCall;
import com.getcapacitor.plugin.PushNotifications;

public class PushNotificationService {
    private static final String CHANNEL_ID = "tunavis_notifications";
    private static final String CHANNEL_NAME = "TunAvis Notifications";
    private static final String TAG = "PushNotificationService";

    private Context context;
    private NotificationManager notificationManager;

    public PushNotificationService(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    /**
     * Crée le canal de notification (requis pour Android 8.0+)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            );
            
            // Activer les sons et vibrations
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(true);
            
            // Sonnerie par défaut du système
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            channel.setSound(defaultSoundUri, null);
            
            // Sonnerie personnalisée (optionnel - ajoutez votre fichier son dans res/raw/)
            // Uri customSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
            // channel.setSound(customSoundUri, null);
            
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Affiche une notification avec sonnerie
     */
    public void showNotification(String title, String message, String type, int notificationId) {
        try {
            // Intent pour ouvrir l'application
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("type", type); // "avis" ou "reclamation"
            intent.putExtra("notificationId", notificationId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
            );

            // Sonnerie par défaut
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            
            // Pour une sonnerie personnalisée, décommentez cette ligne:
            // Uri customSoundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.notification_sound);
            // defaultSoundUri = customSoundUri;

            // Construire la notification
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Remplacez par votre icône
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri) // Sonnerie
                .setVibrate(new long[]{0, 500, 250, 500}) // Vibration
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentIntent(pendingIntent)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

            // Afficher la notification
            notificationManager.notify(notificationId, notificationBuilder.build());

            Log.d(TAG, "Notification affichée: " + title);

        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de l'affichage de la notification", e);
        }
    }

    /**
     * Joue une sonnerie personnalisée
     */
    public void playNotificationSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            RingtoneManager.getRingtone(context, notification).play();
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la lecture de la sonnerie", e);
        }
    }
}

