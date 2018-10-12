package hadi.motorhebat;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import Adapter.RecycleAdapterListMotor;
import Kelas.SharedVariable;

public class ListMotorActivity extends AppCompatActivity {

    FloatingActionButton btnCreate;
    Intent i;
    RecyclerView recycler_listMotor;
    RecycleAdapterListMotor adapter;
    public static ProgressBar progressBar;
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_motor);
        btnCreate = (FloatingActionButton) findViewById(R.id.btnCreate);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtInfo = (TextView) findViewById(R.id.txtInfo);

        recycler_listMotor = (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapter = new RecycleAdapterListMotor(this);
        recycler_listMotor.setAdapter(adapter);
        recycler_listMotor.setLayoutManager(new LinearLayoutManager(this));

        if (SharedVariable.list_motor.isEmpty()){
            txtInfo.setVisibility(View.VISIBLE);
        }

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),TambahMotor.class);
                startActivity(i);
            }
        });
    }
}
