package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mpet.project2018.air.mpet.R;

public class AboutApp extends Fragment{
    private OnFragmentInteractionListener mListener;

    public AboutApp() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about_app, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("O nama");
        }
        TextView textView = (TextView) view.findViewById(R.id.textView10);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText("Aplikacije je izrađena u svrhu predmeta Analiza i razvoj programa koji se izvodi na diplomskom studiju informatike te kao takva još nije predviđena za širu upotrebu. Ukratko aplikacija predstavlja način vođenja evidencije o kućnim ljubimcima. Osnovna funkcionalnost koju nudi je praćenje ljubimca putem NFC kartice. Korisniku je na raspolaganju također i skreniranje tuđih ljubimaca te slanje poruka vlasniku. Na ovaj način se može olakšati pronalazak ljubimca u slučaju da je nestao ili pobjegao."
        );
        return view;

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


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }

}

