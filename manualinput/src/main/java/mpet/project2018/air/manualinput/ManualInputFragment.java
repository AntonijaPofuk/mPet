package mpet.project2018.air.manualinput;


import android.annotation.SuppressLint;
import android.content.Context;

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

import mpet.project2018.air.core.InternetConnectionHandler;
import mpet.project2018.air.core.LocationAvailabilityHandler;
import mpet.project2018.air.core.ModuleInterface;
import mpet.project2018.air.core.OnFragmentInteractionListener;


/**
 * Klasa za provedbu ručnog unosa koda NFC taga
 */
@SuppressLint("ValidFragment")
public class ManualInputFragment extends Fragment implements View.OnClickListener, ModuleInterface {

    // Trenutna aktivnost
    private OnFragmentInteractionListener listenerActivity;
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
     * Pokreće validaciju koda na klik
     *
     * @param v gumb referenca
     */
    @Override
    public void onClick(View v) {
        if (InternetConnectionHandler.isOnline(getActivity())) {
            String uneseniKod = unosKoda.getText().toString();
            listenerActivity.onCodeArrived(uneseniKod);
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

    @Override
    public int getModuleName() {
        return R.string.module_namex;
    }

    @Override
    public String returnCode() {
        return null;
    }

}
