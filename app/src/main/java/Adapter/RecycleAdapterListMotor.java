package Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import Kelas.SharedVariable;
import hadi.motorhebat.BerandaActivity;
import hadi.motorhebat.ListMotorActivity;
import hadi.motorhebat.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterListMotor extends RecyclerView.Adapter<RecycleViewHolderListMotor> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static List<String> list_nama = new ArrayList();
    public static List<String> list_plat = new ArrayList();
    public static List<String> list_deviceID = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterListMotor(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("device").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list_nama.clear();
                list_plat.clear();
                list_deviceID.clear();
                String nama = "";
                String plat = "";
                String deviceId = "";

                int c = 0;

                ListMotorActivity.progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot child : dataSnapshot.getChildren()){

                    int a  = (int) child.getChildrenCount();
                    deviceId = child.child("deviceId").getValue().toString();
                    nama = child.child("nama_motor").getValue().toString();
                    plat = child.child("plat_nomor").getValue().toString();

                    if (!SharedVariable.list_motor.isEmpty()) {

                        if (SharedVariable.list_motor.get(c).toString().equals(deviceId)) {

                            list_deviceID.add(deviceId);
                            list_nama.add(nama);
                            list_plat.add(plat);
                        }
                    }
                    c++;
                }
                ListMotorActivity.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    @Override
    public RecycleViewHolderListMotor onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_listmotor, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderListMotor viewHolderChat = new RecycleViewHolderListMotor(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolderListMotor holder, int position) {

        Resources res = context.getResources();

      // holder.txtNamaMotor.setText(nama[position].toString());
        //holder.txtPlatNomor.setText(plat[position].toString());
        //holder.contentWithBackground.setGravity(Gravity.LEFT);
       holder.txtNamaMotor.setText(list_nama.get(position).toString());
       holder.txtPlatNomor.setText(list_plat.get(position).toString());


        holder.txtPlatNomor.setOnClickListener(clicklistener);
        holder.cardlist_item.setOnClickListener(clicklistener);
        holder.txtPlatNomor.setOnClickListener(clicklistener);
        holder.relaList.setOnClickListener(clicklistener);
        holder.img_iconlistMotor.setOnClickListener(clicklistener);


        holder.txtNamaMotor.setTag(holder);
        holder.txtPlatNomor.setTag(holder);
        holder.img_iconlistMotor.setTag(holder);
        holder.relaList.setTag(holder);


    }

    View.OnClickListener clicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RecycleViewHolderListMotor vHolder = (RecycleViewHolderListMotor) v.getTag();
            int position = vHolder.getPosition();
           // Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();

           String activeDevice = list_deviceID.get(position).toString();
            i = new Intent(v.getContext(), BerandaActivity.class);
            i.putExtra("activeDevice",activeDevice);
            context.startActivity(i);


        }
    };


    public int getItemCount() {

        return list_nama == null ? 0 : list_nama.size();
       //return nama.length;

    }



}
