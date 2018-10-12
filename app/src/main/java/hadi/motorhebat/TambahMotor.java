package hadi.motorhebat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Kelas.MotorList;
import Kelas.MotorModel;
import Kelas.Utils;

public class TambahMotor extends AppCompatActivity {

    EditText  et_plat,
            et_kodeDevice;
    Button btnSimpan;
    Intent i;
    DatabaseReference ref,refUser;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static List<String> list_kode_device = new ArrayList();
    public static List<String> list_pass_device = new ArrayList();
    ProgressBar progressBar;
    String userID;
    MotorList motorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_motor);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(TambahMotor.this);
        ref = FirebaseDatabase.getInstance().getReference();
        refUser = FirebaseDatabase.getInstance().getReference().child("users");
        fAuth = FirebaseAuth.getInstance();
        ref = ref.child("device");
        userID = fAuth.getCurrentUser().getUid();

        et_plat = (EditText) findViewById(R.id.userEmailId);
        et_kodeDevice = (EditText) findViewById(R.id.password);
        btnSimpan = (Button) findViewById(R.id.signUpBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        getDataDevice();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });


    }

    private void matikanKomponen(){
        progressBar.setVisibility(View.VISIBLE);
        et_plat.setEnabled(false);
        et_kodeDevice.setEnabled(false);
    }

    private void hidupkanKomponen(){
        progressBar.setVisibility(View.GONE);
        et_plat.setEnabled(true);
        et_kodeDevice.setEnabled(true);
    }

    private void checkValidation() {

        matikanKomponen();

        // Get all edittext texts
        String getEmailId = et_plat.getText().toString();
        String getPassword = et_kodeDevice.getText().toString();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        // Check if all strings are null or not
        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0
                ) {

            customToast("Semua field harus diiisi");
            hidupkanKomponen();
        }
        // Else do signup or do your stuff
        else
            addMotor(getEmailId,getPassword);

    }

    private void addMotor(final String deviceID,String passwordDevice){
        matikanKomponen();
        int jmlDevice = list_kode_device.size();

        if (list_kode_device.contains(deviceID)){


            for (int c=0;c<jmlDevice;c++){



                if (list_kode_device.get(c).toString().equals(deviceID)) {

                    if (list_pass_device.get(c).toString().equals(passwordDevice)){
                        customToast("Device Berhasil ditambahkan !");
                        String status = "off0";
                        //tambahkan device ke list motor nya user tsb

                        motorList = new MotorList(list_kode_device.get(c).toString(),status);

                        String key = ref.child(userID).child("motorList").push().getKey();
                        refUser.child(userID).child("motorList").child(key).setValue(motorList);

                        i = new Intent(getApplicationContext(),SplashActivity.class);
                        startActivity(i);

                    }
                    else {
                        customToast("Password Device Salah");
                    }

                }
            }
            hidupkanKomponen();

        }else {
            customToast("Device tidak ditemukan !");
            hidupkanKomponen();
        }
    }

        public  void customToast(String s){
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast,
                    (ViewGroup) findViewById(R.id.toast_root));

            TextView text = (TextView) layout.findViewById(R.id.toast_error);
            text.setText(s);
            Toast toast = new Toast(getApplicationContext());// Get Toast Context
            toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
            toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
            toast.setView(layout); // Set Custom View over toast
            toast.show();// Finally show toast
        }

    private void getDataDevice(){
        matikanKomponen();

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_kode_device.clear();
                list_pass_device.clear();

                String deviceId = "";
                String passDevice = "";
                for (DataSnapshot child : dataSnapshot.getChildren()){

                    deviceId = child.child("deviceId").getValue().toString();
                    passDevice = child.child("password_device").getValue().toString();

                    list_kode_device.add(deviceId);
                    list_pass_device.add(passDevice);
                }

                hidupkanKomponen();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
