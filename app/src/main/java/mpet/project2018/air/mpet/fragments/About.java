package mpet.project2018.air.mpet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import mpet.project2018.air.mpet.R;

public class About extends Fragment{
    private OnFragmentInteractionListener mListener;

    public About() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.about, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("O nama");
        }

        TextView textView = (TextView) view.findViewById(R.id.textView11);
        TextView textView2 = (TextView) view.findViewById(R.id.textView12);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView4);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.imageView5);


            textView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                                ft.replace(R.id.mainFrame, new AboutApp());
                                                ft.addToBackStack(null);
                                                ft.commit();

                                            }
                                        }
            );

        imageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.mainFrame, new AboutApp());
                                            ft.addToBackStack(null);
                                            ft.commit();

                                        }
                                    }
        );

        textView2.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.mainFrame, new AboutUs());
                                            ft.addToBackStack(null);
                                            ft.commit();

                                        }
                                    }
        );

        imageView2.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                             ft.replace(R.id.mainFrame, new AboutUs());
                                             ft.addToBackStack(null);
                                             ft.commit();

                                         }
                                     }
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

