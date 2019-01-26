package mpet.project2018.air.manualinput;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import Retrofit.DataGetListenersAndLoaders.DataLoadedListeners.LjubimacDataLoadedListener;
import Retrofit.DataGetListenersAndLoaders.DataLoaders.LjubimacDataLoader;
import Retrofit.Model.Ljubimac;
import mpet.project2018.air.core.CodeValidation;
import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.core.PetDataInterface;

@SuppressLint("ValidFragment")
public class ManualInputFragment extends Fragment implements LjubimacDataLoadedListener, View.OnClickListener {


    private PetDataInterface listenerActivity;
    private Ljubimac loadedPet;
    private Button potvrdiUnos;
    private EditText unosKoda;
    private ProgressBar manualProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.manual_input,container,false);

        potvrdiUnos=view.findViewById(R.id.potvrdiKodButton);
        potvrdiUnos.setOnClickListener(this);

        unosKoda=view.findViewById(R.id.unesiKodEditText);
        manualProgress=view.findViewById(R.id.progressBarManual);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    private void validateCode(String code) {

        if(CodeValidation.validateCodeFormat(code))
        {
            LjubimacDataLoader petLoader=new LjubimacDataLoader(this);
            petLoader.loadDataByTag(code);
        }
        else
        {
            outputValidationStatus(false);
        }

    }


    private void outputValidationStatus(boolean validationStatus) {

        if(validationStatus)alertingMessage( getResources().getString(R.string.codeStatusOK),R.drawable.success_message,validationStatus);
        else alertingMessage(getResources().getString(R.string.codeStatusNotOK),R.drawable.fail_message,validationStatus);
    }

    @Override
    public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {

        if(listaLjubimaca.isEmpty()) outputValidationStatus(false);
        else
        {
            loadedPet=listaLjubimaca.get(0);
            outputValidationStatus(true);
        }

    }

    private void alertingMessage(String message, int imageIcon, final boolean status)
    {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(getActivity());
        }
        builder.setTitle("Rezultat Provjere koda")
                .setMessage(message)
                .setPositiveButton("U redu", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(!status)
                        {
                            potvrdiUnos.setVisibility(View.VISIBLE);
                            manualProgress.setVisibility(View.INVISIBLE);
                        }
                        if(status) listenerActivity.petCodeLoaded(loadedPet);
                    }
                })
                .setIcon(imageIcon)
                .show();
    }

    @Override
    public void onClick(View v) {
        if(InternetConnectionHandler.isOnline(getActivity())) {
            String uneseniKod = unosKoda.getText().toString();
            validateCode(uneseniKod);
            potvrdiUnos.setVisibility(View.INVISIBLE);
            manualProgress.setVisibility(View.VISIBLE);
        }
        else Toast.makeText(getContext(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PetDataInterface) {
            listenerActivity = (PetDataInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PetDataInterface");
        }
    }
}
