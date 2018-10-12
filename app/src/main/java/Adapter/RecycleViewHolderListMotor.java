package Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import hadi.motorhebat.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHolderListMotor extends RecyclerView.ViewHolder {

    public TextView txtNamaMotor,txtPlatNomor;
    public ImageView img_iconlistMotor;
    public CardView cardlist_item;
    public RelativeLayout relaList;

    public RecycleViewHolderListMotor(View itemView) {
        super(itemView);

        txtNamaMotor = (TextView) itemView.findViewById(R.id.txt_namaMotor);
        txtPlatNomor = (TextView) itemView.findViewById(R.id.txt_platNomor);
        img_iconlistMotor = (ImageView) itemView.findViewById(R.id.img_iconlistMotor);
        cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);
        relaList = (RelativeLayout) itemView.findViewById(R.id.relaList);

    }
}
