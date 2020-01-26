 package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

 public class MainActivity extends AppCompatActivity {


     private NotificationManager mNotifyManager;
     private NotificationCompat.Builder mBuilder;

     private static final int PICK_FROM_CAMERA = 1;
     private static final int PICK_FROM_GALLARY = 2;
     private static final int PICK_VIDEO_CAMERA=3;
     private static String[] PERMISSIONS_STORAGE = {
             Manifest.permission.READ_EXTERNAL_STORAGE,
             Manifest.permission.WRITE_EXTERNAL_STORAGE
     };
     private static final int PERMISSION_REQUEST_CODE = 100;

    NotificationCompat.Builder notif;
    private static final int UNIQUE_ID=2011;
    Context con;
    Button clik;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        final int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            } else {
                // Toast.makeText(DisplayImageActivity.this, "Please give your permission.", Toast.LENGTH_LONG).show();
            }
        }

        if(checkPermissionwrite()){
            //  Toast.makeText(this,"Write on",Toast.LENGTH_LONG).show();
        }
        else requestPermission();

        con=this;
        notif=new NotificationCompat.Builder(this);
        notif.setAutoCancel(true);
        clik=findViewById(R.id.not);
        clik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // pendingintent();
                notificationshow();
                /*
                notif.setContentTitle("Fuck you");
                notif.setContentText("oh yaa");
                notif.setWhen(System.currentTimeMillis());
                Intent intd=new Intent(con,MainActivity.class);
                PendingIntent ped=PendingIntent.getActivity(con,0,intd,PendingIntent.FLAG_UPDATE_CURRENT);
                notif.setContentIntent(ped);
                NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                nm.notify(UNIQUE_ID,notif.build());

                 */
            }
        });
    }

    private void pendingintent(){
        Notification noti;
        int nid = 1;
        String path;
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        File file = new File("/storage/emulated/0/DCIM/Camera/PNG_20200125_225503_8146128494111396969.png"); //
        //Intent intent = new Intent();
        //intent.setAction(Intent.ACTION_VIEW);

        //intent.setDataAndType(Uri.fromFile(file), "*/*"); //


        Uri sharedFileUri = FileProvider.getUriForFile(this, "com.example.notification.provider", file);
        ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(this).addStream(sharedFileUri);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intentBuilder.createChooserIntent(), 0);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pIntent);
        mNotifyManager.notify(nid, mBuilder.build());
    }

     private void notificationshow() {
        String filepath="/storage/emulated/0/DCIM/Camera/PNG_20200125_225503_8146128494111396969.png";
         // Create the NotificationChannel, but only on API 26+ because
         // the NotificationChannel class is new and not in the support library
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             CharSequence name = "diskspace";
             String description = "View downloaded video";
             String channel_id = "diskspace";
             int importance = NotificationManager.IMPORTANCE_HIGH;
             NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
             channel.setDescription(description);

             NotificationManager notificationManager = getSystemService(NotificationManager.class);
             notificationManager.createNotificationChannel(channel);
         }

         // show notification after saving file
         NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
         NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "diskspace")
                 .setAutoCancel(true)
                 .setSmallIcon(R.drawable.ic_launcher_background)
                 .setContentTitle(filepath)
                 .setContentText("Tap to view the image.")
                 .setLargeIcon(BitmapFactory.decodeFile(filepath))
                 .setPriority(NotificationCompat.PRIORITY_HIGH)
                 .setContentIntent(PendingIntent.getActivity(this, 0,
                         new Intent(Intent.ACTION_VIEW)
                                 .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                                 .setDataAndType(FileProvider.getUriForFile(this, "com.example.notification.fileprovider", new File(filepath)),
                                         "*/*"), 0));

         notificationManagerCompat.notify(2, builder.build());
     }


     private boolean checkPermissionwrite() {
         int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
         if (result == PackageManager.PERMISSION_GRANTED) {
             return true;
         } else {
             return false;
         }
     }

     private boolean checkIfAlreadyhavePermission() {
         int result = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
         return result == PackageManager.PERMISSION_GRANTED;
     }

     private void requestPermission() {
         if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
             Toast.makeText(MainActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
         } else {
             ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
         }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         switch (requestCode) {
             case 1: {
                 if (grantResults.length > 0
                         && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                 } else {
                     Toast.makeText(MainActivity.this, "Please give your permission.", Toast.LENGTH_LONG).show();
                 }
                 break;
             }
             case 100:
                 if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                     Toast.makeText(MainActivity.this, "Write Permission granted.", Toast.LENGTH_LONG).show();
                 } else {
                     Toast.makeText(MainActivity.this, "Write Please give your permission.", Toast.LENGTH_LONG).show();
                 }
                 break;

         }
     }


 }
