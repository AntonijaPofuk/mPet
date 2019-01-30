package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;


import mpet.project2018.air.core.OnFragmentInteractionListener;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.notifications.NotificationsRecycleViewAdapter;

public class NotificationAll extends Fragment {


    OnFragmentInteractionListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.notification_all, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction(getString(R.string.prikaz_svih_obavijesti_naslov));
        }

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notificationAllRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<mpet.project2018.air.database.entities.Skeniranje> scanListLocal = new SQLite().select().from(mpet.project2018.air.database.entities.Skeniranje.class).queryList();

        if(scanListLocal.isEmpty() || scanListLocal.size()==0) {
            Toast.makeText(getContext(), getString(R.string.nema_obavijesti), Toast.LENGTH_SHORT).show();
        }

        NotificationsRecycleViewAdapter notificationsAdapter = new NotificationsRecycleViewAdapter(scanListLocal);

        recyclerView.setAdapter(notificationsAdapter);


    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
