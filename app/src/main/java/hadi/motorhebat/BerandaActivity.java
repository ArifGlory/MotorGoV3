package hadi.motorhebat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Kelas.BaseDrawerActivity;
import Kelas.SharedVariable;
import Kelas.UserPreference;
import Module.DirectionKobal;
import Module.DirectionKobalListener;
import Module.Route;

public class BerandaActivity extends BaseDrawerActivity implements LocationListener, DirectionKobalListener{

    public static ProgressBar progressBar;
    Intent i;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static String activeDevice = "";
    TextView txtNamaMotor,txtPlatNomor,txtNotif,txtStarter;
    CardView crd_mesin,crd_timer,crd_search,crd_lokasi,crd_notif,crd_starter;
    ImageView img_engine,img_search,img_lokasi,img_timer,img_notif;
    String Smesin,Ssecure,Slokasi,Sping,Snama,Splat,Swarning,Sstarter;
    private static final long time = 2;
    private CountDownTimer mCountDownTimer;
    private long mTimeRemaining;
    Double lat;
    Double lon,userLon,userLat;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    double latitude;
    double longitude;

    private LocationManager locationManager;
    private String provider;
    //private LocationRequest mlocationrequest;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 6000;
    public static String jarakMotor;
    int jrkMotor,a,b,c;

    final static int RQS1 = 1;
    private static final long delayAlarm = 1 *60 * 1000L;
    Date calNow;
    Calendar calSet,calBanding;
    UserPreference mUserPref;
    DialogInterface.OnClickListener listener;
    int jarakAman;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(BerandaActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();

        i = getIntent();
        activeDevice = i.getStringExtra("activeDevice");


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtNamaMotor = (TextView) findViewById(R.id.txtNamaMotor);
        txtPlatNomor = (TextView) findViewById(R.id.txtPlatNomor);
        crd_mesin = (CardView) findViewById(R.id.crd_mesin);
        crd_timer = (CardView) findViewById(R.id.crd_timer);
        crd_lokasi = (CardView) findViewById(R.id.crd_lokasi);
        crd_search = (CardView) findViewById(R.id.crd_search);
        crd_starter = (CardView) findViewById(R.id.cardStarter);
        img_engine = (ImageView) findViewById(R.id.engine);
        img_search = (ImageView) findViewById(R.id.search);
        img_lokasi = (ImageView) findViewById(R.id.lokasi);
        img_timer = (ImageView) findViewById(R.id.timer);
        crd_notif = (CardView) findViewById(R.id.cardNotif);
        img_notif = (ImageView) findViewById(R.id.logoNotif);
        txtNotif = (TextView) findViewById(R.id.txtNotif);
        txtStarter = (TextView) findViewById(R.id.txtStarter);
        mUserPref = new UserPreference(this);
        jarakAman = mUserPref.getJarakAman();


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            System.out.println("Location not avilable");
        }

        getLocation();
        Toast.makeText(getApplicationContext(),"UserLat : "+userLat+" , UserLon : "+userLon , Toast.LENGTH_LONG).show();

        if (activeDevice.equals("zero")){
            txtNamaMotor.setText("Belum Mendaftarkan Motor");
            txtPlatNomor.setText("-- ---- --");
            txtNotif.setText("----------");
            matikanKomponenSementara();
        }else {
            matikanKomponen();

            ref.child("device").child(activeDevice).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String namaMotor = dataSnapshot.child("nama_motor").getValue().toString();
                    String platNomor = dataSnapshot.child("plat_nomor").getValue().toString();
                    String ping  = dataSnapshot.child("ping").getValue().toString();
                    String mesin  = dataSnapshot.child("mesin").getValue().toString();
                    String secure = dataSnapshot.child("secureMode").getValue().toString();
                    String latlon = dataSnapshot.child("latlon").getValue().toString();
                    String sublon = latlon.substring(latlon.indexOf(",")+1);
                    String warning = dataSnapshot.child("warning").getValue().toString();
                    String starter = dataSnapshot.child("starter").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    int index = latlon.indexOf(",");
                    String sublat = latlon.substring(0,index);
                    //Toast.makeText(getApplicationContext(),"substing lat : "+sublat+" | substing lon : "+sublon, Toast.LENGTH_SHORT).show();

                    txtPlatNomor.setText(platNomor);
                    txtNamaMotor.setText(namaMotor);

                    lat = Double.valueOf(sublat);
                    lon = Double.valueOf(sublon);

                    Sping = ping;
                    Smesin = secure;
                    Ssecure = status;
                    Swarning = warning;
                    SharedVariable.latitudeMotor = lat;
                    SharedVariable.longitudeMotor = lon;
                    Sstarter = mesin;


                    if (Sstarter.equals("Off")){
                        txtStarter.setText("Starter Off");
                        crd_starter.setBackgroundColor(getResources().getColor(R.color.white));
                    }else {
                        txtStarter.setText("Starter On");
                        crd_starter.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    }
                    if (Sping.equals("Off")){
                        img_search.setImageResource(R.drawable.search_off);
                    }else {
                        img_search.setImageResource(R.drawable.search_on);
                    }

                    if (Smesin.equals("Off")){
                        img_engine.setImageResource(R.drawable.mesin_off);
                    }else {
                        img_engine.setImageResource(R.drawable.mesin_on);
                    }

                    if (Ssecure.equals("Off")){
                        img_timer.setImageResource(R.drawable.padlock);
                    }else {
                        img_timer.setImageResource(R.drawable.locked_padlock);
                    }

                    if (Swarning.equals("aman")){
                        img_notif.setImageResource(R.drawable.warning_off);
                        txtNotif.setText("Kunci Aman");
                    }else {
                        if (Ssecure.equals("On")){
                            img_notif.setImageResource(R.drawable.warning_on);
                            txtNotif.setText("Terindikasi Pembobolan !");

                            //tampilkam Motif
                            calNow = Calendar.getInstance().getTime();
                            calSet = Calendar.getInstance();
                            calBanding = Calendar.getInstance();
                            a = calNow.getHours();
                            b = calNow.getMinutes() + 1;
                            calSet.set(Calendar.HOUR_OF_DAY, a);
                            calSet.set(Calendar.MINUTE, b);
                            calSet.set(Calendar.SECOND, 0);
                            calSet.set(Calendar.MILLISECOND, 0);

                            if (calSet.compareTo(calBanding) <= 0) {
                                //today set time passed,count to tomorow

                                calSet.add(Calendar.DATE, 1);
                                Log.i("Hasil = ", "<=0");
                            } else if (calSet.compareTo(calBanding) > 0) {

                                Log.i("Hasil = ", " > 0");
                            } else {

                                Log.i("Hasil = ", "else");
                            }

                            setNotif(calSet);
                        }

                    }

                 //   Toast.makeText(getApplicationContext(),"lat : "+lat+" , Lon : "+lon , Toast.LENGTH_LONG).show();
                    hidupkanKomponen();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            ref.child("device").child(activeDevice).child("latlon").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   sendRequest();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        //menyalakan motor
        crd_mesin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Smesin.equals("Off")){
                    ref.child("device").child(activeDevice).child("secureMode").setValue("On");
                  //  Toast.makeText(getApplicationContext(),"Hour : "+a+", B : "+b, Toast.LENGTH_SHORT).show();


                }else {
                    ref.child("device").child(activeDevice).child("secureMode").setValue("Off");
                }

            }
        });

        //// MEnghidupkan fitur cari Motor (klakson nyala)
        crd_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Sping.equals("Off")){

                    ref.child("device").child(activeDevice).child("ping").setValue("On");

                }else {
                    ref.child("device").child(activeDevice).child("ping").setValue("Off");
                }

            }
        });

        //menyalakan fitur secure mode
        crd_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Ssecure.equals("Off")){
                    ref.child("device").child(activeDevice).child("status").setValue("On");
                    SharedVariable.notifChance = 0;
                }else {
                    ref.child("device").child(activeDevice).child("status").setValue("Off");
                }
            }
        });

        //Pindah Ke Maps Activity
        crd_lokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(i);
            }
        });

        crd_notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Swarning.equals("aman")){

                }else {
                    if (Ssecure.equals("On")){
                        showDialogNotif();
                    }

                }
            }
        });

        crd_starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Smesin.equals("On")){
                   // txtStarter.setText("Starter On");
                   // crd_starter.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    ref.child("device").child(activeDevice).child("mesin").setValue("On");

                }else {
                    Toast.makeText(getApplicationContext(),"Kontak belum dinyalakan",Toast.LENGTH_SHORT).show();
                }
            }
        });
        

    }

    private void matikanKomponen(){
        progressBar.setVisibility(View.VISIBLE);
        crd_lokasi.setEnabled(false);
        crd_mesin.setEnabled(false);
        crd_search.setEnabled(false);
        crd_timer.setEnabled(false);
        crd_notif.setEnabled(false);
    }

    private void matikanKomponenSementara(){
        crd_lokasi.setEnabled(false);
        crd_mesin.setEnabled(false);
        crd_search.setEnabled(false);
        crd_timer.setEnabled(false);
        crd_notif.setEnabled(false);
    }

    private void hidupkanKomponen(){
        progressBar.setVisibility(View.GONE);
        crd_lokasi.setEnabled(true);
        crd_mesin.setEnabled(true);
        crd_search.setEnabled(true);
        crd_timer.setEnabled(true);
        crd_notif.setEnabled(true);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());

       // Toast.makeText(getApplicationContext(),"Userlat : "+lat+"& UserLon :"+lng , Toast.LENGTH_SHORT).show();

        userLat = lat;
        userLon = lng;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private void showDialogNotif(){
        LayoutInflater minlfater = LayoutInflater.from(this);
        View v = minlfater.inflate(R.layout.custom_dialog_goto_login, null);
        final android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this).create();
        dialog.setView(v);

        final Button btnDialogYes = (Button) v.findViewById(R.id.btnDialogKeLogin);
        final Button btnDialogNo = (Button) v.findViewById(R.id.btnDialogNo);
        final ImageView headerImage = (ImageView) v.findViewById(R.id.header_cover_image);


        btnDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.child("device").child(activeDevice).child("warning").setValue("aman");
                dialog.dismiss();
            }
        });
        btnDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            userLat = latitude;
                            userLon = longitude;
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                    }
                    if (locationManager != null) {
                        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            userLat = latitude;
                            userLon = longitude;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, (LocationListener) this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates((LocationListener) this);
    }

    @Override
    public void onDirectionKobalStart() {

    }

    @Override
    public void onDirectionKobalSuccess(List<Route> route) {
        int no = 0;

        for (Route routes : route) {

            jarakMotor = routes.distance.text;
            jrkMotor = routes.distance.value;

            no++;
        }
        SharedVariable.jarakKeMotor = jarakMotor;
       // Log.d("SV.jarak BA = ",SharedVariable.jarakKeMotor);
       // Toast.makeText(getApplicationContext(),"Jarak Ke motor Str: "+jarakMotor, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(getApplicationContext(),"Jarak Ke motor Int: "+jrkMotor, Toast.LENGTH_SHORT).show();


       // if (SharedVariable.notifChance %3 == 0) {

        if (Ssecure.equals("On")) {
            jarakAman = mUserPref.getJarakAman();
            if (jrkMotor > jarakAman) {
              //  Toast.makeText(getApplicationContext(), "Motor tidak berada dalam jarak aman, segera periksa Motor anda. Jarak = : " + jarakMotor, Toast.LENGTH_SHORT).show();

                calNow = Calendar.getInstance().getTime();
                calSet = Calendar.getInstance();
                calBanding = Calendar.getInstance();
                a = calNow.getHours();
                b = calNow.getMinutes();
                c = calNow.getSeconds() + 1;

                calSet.set(Calendar.HOUR_OF_DAY, a);
                calSet.set(Calendar.MINUTE, b);
                calSet.set(Calendar.SECOND, c);
                calSet.set(Calendar.MILLISECOND, 0);

                if (calSet.compareTo(calBanding) <= 0) {
                    //today set time passed,count to tomorow

                    calSet.add(Calendar.DATE, 1);
                    Log.i("Hasil = ", "<=0");
                } else if (calSet.compareTo(calBanding) > 0) {

                    Log.i("Hasil = ", " > 0");
                } else {

                    Log.i("Hasil = ", "else");
                }
                setAlarm(calSet);
                SharedVariable.notifChance++;
            }

        }
       // }

    }

    private void sendRequest(){

        String origin = String.valueOf(userLat) + ","+ String.valueOf(userLon);
        String destination = String.valueOf(lat) + ","+ String.valueOf(lon);

        if (origin.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Tolong masukkan origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Tolong masukkan destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

//            Toast.makeText(getActivity(),"origin : "+origin+"& destination : "+destination,Toast.LENGTH_SHORT).show();

        try {
            new DirectionKobal(this, origin, destination).execute();
            Log.d("origin:",origin);
            Log.d("destination:",destination);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Eror Direction : "+e.toString(),Toast.LENGTH_SHORT).show();
        }



    }

    private void setAlarm(Calendar targetCal){

        Intent i = new Intent(getBaseContext(), AlarmReceiver.class);
        i.putExtra("jarak",jarakMotor);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),RQS1,i,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);

        if (Build.VERSION.SDK_INT < 23){

            if (Build.VERSION.SDK_INT >= 19){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
            }else {
                alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
            }

        }else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
        }
    }

    private void setNotif(Calendar targetCal){

        Intent i = new Intent(getBaseContext(), WarningReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(),RQS1,i,0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);

        if (Build.VERSION.SDK_INT < 23){

            if (Build.VERSION.SDK_INT >= 19){
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
            }else {
                alarmManager.set(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
            }

        }else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,targetCal.getTimeInMillis(),pendingIntent);
        }
    }


}
