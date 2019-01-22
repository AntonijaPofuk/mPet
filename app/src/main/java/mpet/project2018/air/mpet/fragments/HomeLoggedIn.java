package mpet.project2018.air.mpet.fragments;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.Delete;

import mpet.project2018.air.database.entities.Kartica;
import mpet.project2018.air.database.entities.Korisnik;
import mpet.project2018.air.database.entities.Ljubimac;
import mpet.project2018.air.database.entities.Skeniranje;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;


public class HomeLoggedIn extends Fragment {

    private OnFragmentInteractionListener mListener;
    public HomeLoggedIn() {}
    private String idPrijavljeni;
    private ProgressDialog progress;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_logged_in, container, false);
        // NOTE : We are calling the onFragmentInteraction() declared in the MainActivity
        // we are sending "Fragment 1" as title parameter when fragment1 is activated
        if (mListener != null) {
            mListener.onFragmentInteraction("Početna");
        }

        // Listeneri za btn
        // Button btn1= (Button) view.findViewById(R.id.frag1_btn1); btn1.setOnclickListener(...





        Button btn1=(Button) view.findViewById(R.id.btnOdjava);
        btn1.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        logout();
                                    }
                                }
        );

        Button btn2=(Button) view.findViewById(R.id.btnScanUlogirani);
        btn2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Toast.makeText(getActivity(), "Skeniranje....",
                                                Toast.LENGTH_LONG).show();
                                        swapFragment2();
                                    }
                                }
        );






        //Fetching email from shared preferences
        /*SharedPreferences sp = this.getActivity().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = sp.getString(Config.ID_SHARED_PREF,"");
        //Showing the current logged in email to textview
        */



     /*    Korisnik k = new Korisnik();
        k = new SQLite().select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.valueOf(idPrijavljeni))).querySingle();
        idKorisnik = k.getId_korisnika();
        ime = k.getIme();
        Toast.makeText(getActivity(),"Vase ime je" + ime, Toast.LENGTH_LONG).show();
        //ne radi kad se odjavis */
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
        String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
        //Toast.makeText(getActivity(),"Vas id je"+idPrijavljeni, Toast.LENGTH_SHORT).show();
        //Korisnik k=new Korisnik();
        //Korisnik korisnik = new SQLite().select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt((idPrijavljeni)))).querySingle();
        //k = new SQLite().select().from(Korisnik.class).where(Korisnik_Table.id_korisnika.is(Integer.parseInt(idPrijavljeni))).querySingle();



        //Skeniranje skeniranje = new SQLite().select().from(Skeniranje.class).where(Skeniranje_Table.id_skeniranja.is(Integer.parseInt(idSkeniranja))).querySingle();



        return view;
    }


    private void logout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Sigurno se želite odjaviti?");
        alertDialogBuilder.setPositiveButton("Da",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences preferences = getActivity().getSharedPreferences
                                (Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();

                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);
                        editor.remove("ulogiraniKorisnikId");

                        editor.commit();

                        /*zamjena izbornika*/
                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.activity_main_drawer_logged_out);

                        deleteDatabase();

                        clearBackStack();
                        swapFragment();
                    }
                });

        alertDialogBuilder.setNegativeButton("Ne",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void clearBackStack() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    private void deleteDatabase(){
        Delete.table(Korisnik.class);
        Delete.table(Skeniranje.class);
        Delete.table(Ljubimac.class);
        Delete.table(Kartica.class);
    }

    private void swapFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new HomeLoggedOut());
        //ft.addToBackStack(null);

        ft.commit();
    }

    private void swapFragment2(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new ModulNavigationFragment());
        ft.addToBackStack(null);
        ft.commit();

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
        // Uri -> String
        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }

    /***download slika*********/
    /*
    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getActivity().getApplicationContext(), result, "my_image.png");
        }
    }
    */
    /*********************************/



}
