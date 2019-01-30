package mpet.project2018.air.mpet.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import Retrofit.DataPost.LjubimacPodaciMethod;
import Retrofit.RemotePost.StatusListener;
import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Ljubimac_Table;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.database.entities.Skeniranje_Table;
import mpet.project2018.air.mpet.R;

public class RemoveTag extends Fragment implements StatusListener{

    private String ID_LJUBIMCA;

    private String status;
    private LjubimacPodaciMethod method=new LjubimacPodaciMethod(this);

    private Ljubimac uredivaniLjubimac;

    public static RemoveTag newInstance(String idLjub) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_LJUBIMCA", idLjub);

        RemoveTag fragment = new RemoveTag();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            ID_LJUBIMCA = bundle.getString("ID_LJUBIMCA");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        readBundle(bundle);

        final View view = inflater.inflate(R.layout.brisanje_kartice, container, false);

        Button buttonUkloni=(Button) view.findViewById(R.id.btnBrisanjeKarticeSpremi);
        Button buttonOdustani=(Button) view.findViewById(R.id.btnBrisanjeKarticeOdustani);

        buttonUkloni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            method.RemoveKartica(ID_LJUBIMCA,uredivaniLjubimac.getKartica().getId_kartice());
            }
        });

        buttonOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });

        /*popunjavanje početnih podataka o ljubimcu*/
        uredivaniLjubimac=new SQLite().select().from(Ljubimac.class).where(Ljubimac_Table.id_ljubimca.is(Integer.parseInt(ID_LJUBIMCA))).querySingle();
        TextView imeText = (TextView) view.findViewById(R.id.txtPetName);
        imeText.setText(uredivaniLjubimac.getIme());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return view;
    }

    private void swapFragment(){
        FragmentManager fm= getActivity().getSupportFragmentManager();
        fm.popBackStack();
    }

    private void alertingMessage(String message, int imageIcon)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Upozorenje")
                .setMessage(message)
                .setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(imageIcon)
                .show();
    }

    @Override
    public void onStatusChanged(String s) {
        status=s;
        if(s.equals("greska")){
            alertingMessage("Ups, greška...",R.drawable.fail_message);
        }
        else if(!s.equals("greska")&&!s.equals("uspjesno")){
            Toast.makeText(getActivity(), "Ažurirali ste podatke :)",
                    Toast.LENGTH_LONG).show();
            /*upis u lokalnu bazu*/
            List<Skeniranje> skeniranja =new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.kartica_id_kartice.is(uredivaniLjubimac.getKartica().getId_kartice())).queryList();
            for(Skeniranje scan:skeniranja){
                scan.delete();
            }

            Kartica k=new Kartica();
            uredivaniLjubimac.setKartica(k);
            uredivaniLjubimac.update();
            /**/
            swapFragment();
        }

    }

}
