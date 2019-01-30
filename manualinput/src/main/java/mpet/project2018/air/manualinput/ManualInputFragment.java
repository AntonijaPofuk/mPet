package mpet.project2018.air.manualinput;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
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
import mpet.project2018.air.core.LocationAvailabilityHandler;
import mpet.project2018.air.core.OnFragmentInteractionListener;


/**
 * Klasa za provedbu ručnog unosa koda NFC taga
 */
@SuppressLint("ValidFragment")
public class ManualInputFragment extends Fragment implements LjubimacDataLoadedListener, View.OnClickListener {

    // Trenutna aktivnost
    private OnFragmentInteractionListener listenerActivity;
    // skenirani ljubimac
    private Ljubimac loadedPet;
    // view elementi
    private Button potvrdiUnos;
    private EditText unosKoda;
    private ProgressBar manualProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        listenerActivity.onFragmentInteraction("Unos koda");

        View view = inflater.inflate(R.layout.manual_input, container, false);

        potvrdiUnos = view.findViewById(R.id.potvrdiKodButton);
        potvrdiUnos.setOnClickListener(this);

        unosKoda = view.findViewById(R.id.unesiKodEditText);
        manualProgress = view.findViewById(R.id.progressBarManual);

        LocationAvailabilityHandler.locationCheck(getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    /**
     * Validacija formata koda
     *
     * @param code kod za validiranje, upisani kod
     */
    private void validateCode(String code) {

        if (CodeValidation.validateCodeFormat(code)) {
            LjubimacDataLoader petLoader = new LjubimacDataLoader(this);
            petLoader.loadDataByTag(code);
        } else {
            outputValidationStatus(false);
        }
    }

    /**
     * Međukorak u ispisu stanja validacije
     *
     * @param validationStatus status validacije
     */
    private void outputValidationStatus(boolean validationStatus) {
        if (validationStatus) listenerActivity.petCodeLoaded(loadedPet);
        else
            alertingMessage(getResources().getString(R.string.codeStatusNotOK), R.drawable.fail_message);
    }

    /**
     * Okida se kada je ljubimac dohvaćen pomoću web servisa
     *
     * @param listaLjubimaca lista ljubimaca u kojoj je prvi element traženi ljubimac
     */
    @Override
    public void LjubimacOnDataLoaded(List<Ljubimac> listaLjubimaca) {
        if (listaLjubimaca.isEmpty()) outputValidationStatus(false);
        else {
            loadedPet = listaLjubimaca.get(0);
            outputValidationStatus(true);
        }
    }

    /**
     * Metoda za prikaz dialog boxa
     *
     * @param message   poruka za ispisati
     * @param imageIcon ikona koja će se prikazati
     */
    private void alertingMessage(String message, int imageIcon) {
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
                        potvrdiUnos.setVisibility(View.VISIBLE);
                        manualProgress.setVisibility(View.INVISIBLE);
                    }
                })
                .setIcon(imageIcon)
                .show();
    }

    /**
     * Pokreće validaciju koda na klik
     *
     * @param v gumb referenca
     */
    @Override
    public void onClick(View v) {
        if (InternetConnectionHandler.isOnline(getActivity())) {
            String uneseniKod = unosKoda.getText().toString();
            validateCode(uneseniKod);
            potvrdiUnos.setVisibility(View.INVISIBLE);
            manualProgress.setVisibility(View.VISIBLE);
        } else
            Toast.makeText(getContext(), mpet.project2018.air.core.R.string.internetNotAvailable, Toast.LENGTH_SHORT).show();
    }

    /**
     * Metoda kojom se dobiva instanca aktivnosti
     *
     * @param context kontekst aplikacije
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listenerActivity = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PetDataInterface");
        }
    }
}
