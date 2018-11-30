package mpet.project2018.air.mpet.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import mpet.project2018.air.mpet.R;
import mpet.project2018.air.nfc.NFCManager;

public class PrikazPodatakaOSkeniranomeLjubimcu extends Fragment implements View.OnClickListener {

    private ImageView petPic;
    private TextView petDescp;
    private TextView petName;
    private TextView owner;
    private TextView ownerAdress;
    private TextView ownerEmail;
    private TextView ownerPhone;
    private TextView ownerCell;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.prikaz_podataka_skeniranog_ljubimca,container,false);

        petDescp=view.findViewById(R.id.PetOpisValue);
        petName=view.findViewById(R.id.PetNameValue);
        owner=view.findViewById(R.id.KontaktVlasnikValue);
        ownerAdress=view.findViewById(R.id.KontaktAdresaValue);
        ownerEmail=view.findViewById(R.id.KontaktEmailValue);
        ownerPhone=view.findViewById(R.id.KontaktTelefonValue);
        ownerCell=view.findViewById(R.id.KontaktMobitelValue);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
    }
}
