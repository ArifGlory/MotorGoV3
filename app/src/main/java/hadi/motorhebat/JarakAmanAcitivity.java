package hadi.motorhebat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import Kelas.MotorList;
import Kelas.UserPreference;

public class JarakAmanAcitivity extends AppCompatActivity {


    EditText etJarakAman;
    Button btnSimpan;
    Intent i;
    ProgressBar progressBar;
    UserPreference mUserPref;
    int jarakAman;
    TextView txtJarakNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jarak_aman_acitivity);

        etJarakAman = (EditText) findViewById(R.id.userEmailId);
        btnSimpan = (Button) findViewById(R.id.signUpBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtJarakNow = (TextView) findViewById(R.id.txtJarakNow);
        mUserPref = new UserPreference(this);

        jarakAman = mUserPref.getJarakAman();
        txtJarakNow.setText("Jarak aman sekarang : "+String.valueOf(jarakAman)+" m");

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                String getJarak = etJarakAman.getText().toString();
                int inputJarak = Integer.parseInt(etJarakAman.getText().toString());

                if (getJarak.equals("") || getJarak.length() == 0){
                    customToast("Field Harus diisi !");
                    progressBar.setVisibility(View.GONE);
                }else {
                    mUserPref.setJarakAman(inputJarak);
                    progressBar.setVisibility(View.GONE);
                    customToast("Jarak aman ditetapkan");
                    etJarakAman.setText("");
                    jarakAman = mUserPref.getJarakAman();
                    txtJarakNow.setText("Jarak aman sekarang : "+String.valueOf(jarakAman)+" m");
                }

            }
        });
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
}
