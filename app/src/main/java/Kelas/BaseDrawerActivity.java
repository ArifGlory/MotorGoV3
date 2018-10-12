package Kelas;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;

import hadi.motorhebat.JarakAmanAcitivity;
import hadi.motorhebat.ListMotorActivity;
import hadi.motorhebat.MainActivity;
import hadi.motorhebat.R;

import hadi.motorhebat.SplashActivity;
import util.CircleTransformation;

/**
 * Created by Miroslaw Stanek on 15.07.15.
 */
public class BaseDrawerActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;

    @BindDimen(R.dimen.global_menu_avatar_size)
    int avatarSize;
    @BindString(R.string.user_profile_photo)
    String profilePhoto;

    //Cannot be bound via Butterknife, hosting view is initialized later (see setupHeader() method)
    private ImageView ivMenuUserProfilePhoto;
    private TextView txtHargaCoin,txtNamaProfil,txtExp;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String displayname;
    FirebaseUser fbUser;
    String check;
    Intent i;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
        bindViews();
        setupHeader();
        setupNavigation();
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();


    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (getToolbar() != null) {

            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }

    private void setupNavigation(){
        vNavigation.setNavigationItemSelectedListener(this);
    }


    private void setupHeader() {
        View headerView = vNavigation.getHeaderView(0);
        ivMenuUserProfilePhoto = (ImageView) headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        txtHargaCoin = (TextView) headerView.findViewById(R.id.txtHargaCoin);
        txtNamaProfil = (TextView) headerView.findViewById(R.id.txtNamaProfil);
        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalMenuHeaderClick(v);
            }
        });


      /*  if (txtHargaCoin.getText().equals("0")){
            Log.d("errorKoneksi:","true");
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }*/


        Picasso.with(this)
                .load(R.drawable.icon_utama)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);

       txtNamaProfil.setText(SharedVariable.nama);

        //jumlah motor
        txtHargaCoin.setText(String.valueOf(SharedVariable.list_motor.size()));
        check = SharedVariable.check;
        //Toast.makeText(getApplicationContext(),"Check : "+check,Toast.LENGTH_SHORT).show();


        if (!check.equals("1")){
            i = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(i);
        }

    }

    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.LEFT);

      /*  new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth() / 2;
                Log.d("startLocation1:",":"+startingLocation[0]);
                Log.d("startLocation2:",":"+startingLocation[1]);
                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseDrawerActivity.this);
                overridePendingTransition(0, 0);

            }
        }, 200);*/

        String uId = fAuth.getCurrentUser().getUid();
      //  Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
       // i.putExtra("uId",uId);
       // i.putExtra("nama",nama);
      //  i.putExtra("exp",exp);
      //  startActivity(i);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            // Handle the camera action
            fAuth.signOut();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.menu_list){
            Intent i = new Intent(getApplicationContext(), ListMotorActivity.class);
            startActivity(i);
        }
        if (id == R.id.menu_jarakAman){
            Intent i = new Intent(getApplicationContext(), JarakAmanAcitivity.class);
            startActivity(i);
        }

        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
    }


}
