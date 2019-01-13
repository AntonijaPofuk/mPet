package mpet.project2018.air.mpet.fragments;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import Retrofit.DataPost.KorisnikUredivanjeMethod;
import Retrofit.DataPost.RegistracijaMethod;
import Retrofit.RemotePost.StatusListener;
import mpet.project2018.air.database.entities.Korisnik_Table;
import mpet.project2018.air.mpet.Config;
import mpet.project2018.air.mpet.R;

import static android.app.Activity.RESULT_OK;
import static mpet.project2018.air.mpet.Config.SHARED_PREF_NAME;

public class KorisnikUredivanje extends Fragment implements StatusListener {
    private KorisnikUredivanje.OnFragmentInteractionListener mListener;
    private KorisnikUredivanjeMethod method=new KorisnikUredivanjeMethod(this);
    //private RegistracijaMethod method=new RegistracijaMethod(this);

    public KorisnikUredivanje() {}
    public String status=null;

    private static int RESULT_LOAD_IMAGE = 1;
    private final int PICK_IMAGE_REQUEST = 71;
    private ImageButton imageButton;
    private Bitmap bit=null;
    private String slika=null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.update, container, false);

        if (mListener != null) {
            mListener.onFragmentInteraction("Korisnički podaci");
        }
        imageButton= (ImageButton) view.findViewById(R.id.btnChooseImage);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i=new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
                */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        final SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
        String id=sharedPreferences1.getString(Config.ID_SHARED_PREF, "");
        Toast.makeText(getActivity(),"Vas id je"+id, Toast.LENGTH_SHORT).show();
        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, 0);
            String idPrijavljeni = sharedPreferences.getString(Config.ID_SHARED_PREF, "").toString();
            mpet.project2018.air.database.entities.Korisnik k=new SQLite().select().from(mpet.project2018.air.database.entities.Korisnik.class)
                    .where(Korisnik_Table.id_korisnika.is(Integer.parseInt(idPrijavljeni))).querySingle();
            final String ime = k.getIme();
            final String prezime = k.getPrezime();
            final String korime = k.getKorisnicko_ime();
            final String adresa = k.getAdresa();
            final String email = k.getEmail();
            final String telefon = k.getBroj_telefona();
            final String mobitel = k.getBroj_mobitela();
            final Bitmap slika = k.getSlika();
            final String lozinka=k.getLozinka();

            Toast.makeText(getActivity(),"Vase ime je" + ime, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(),"Vase ime je" + prezime, Toast.LENGTH_LONG).show();
            Toast.makeText(getActivity(),"Vase ime je" + korime, Toast.LENGTH_LONG).show();
            Button buttonSpremi=(Button) view.findViewById(R.id.btnUpdateSpremi);
            buttonSpremi.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    EditText korImeS = (EditText) view.findViewById(R.id.unosKorImeU);
                                                    korImeS.setText(korime);
                                                    EditText imeK = (EditText) view.findViewById(R.id.unosImeU);
                                                    imeK.setText(ime);
                                                    EditText prezimeK = (EditText) view.findViewById(R.id.unosPrezimeU);
                                                    prezimeK.setText(prezime);
                                                    EditText adresaK = (EditText) view.findViewById(R.id.unosAdresaU);
                                                    adresaK.setText(adresa);
                                                    final EditText emailK = (EditText) view.findViewById(R.id.unosMailU);
                                                    emailK.setText(email);
                                                    EditText telefonK = (EditText) view.findViewById(R.id.unosTelefonU);
                                                    telefonK.setText(telefon);
                                                    EditText mobitelK = (EditText) view.findViewById(R.id.unosMobitelU);
                                                    mobitelK.setText(mobitel);
                                                    ImageButton imageButton = (ImageButton) view.findViewById(R.id.button_choose_image);
                                                    imageButton.setImageBitmap(slika);


                                                    String korImeN = korImeS.getText().toString();
                                                    String imeN= imeK.getText().toString();
                                                    String prezimeN=prezimeK.getText().toString();
                                                    String adresa=adresaK.getText().toString();
                                                    String mail=emailK.getText().toString();
                                                    String telefon=telefonK.getText().toString();
                                                    if(TextUtils.isEmpty(telefon)){
                                                        telefon="DEFAULT";
                                                    }
                                                    String mobitel=mobitelK.getText().toString();
                                                    if(TextUtils.isEmpty(mobitel)){
                                                        mobitel="DEFAULT";
                                                    }

                                                    String provjera=null;

                                                    /**/
                                                /*if(bit!=null){
                                                    slika = BitmapTOString(bit);
                                                }
                                                //String slika = BitmapTOString(bit);

                                                /**/
                                                    if(TextUtils.isEmpty(ime)||TextUtils.isEmpty(prezime)||TextUtils.isEmpty(mail)||korImeN.length()<3){
                                                        alertingMessage("Niste unijeli sva potrebna polja.", R.drawable.exclamation_message);
                                                    }
                                                    else{
                                                        if (provjeraNedozvoljeniZnakovi(ime, prezime, korImeN, adresa, mobitel, telefon))
                                                            alertingMessage("Koristili ste nedozvoljene znakove!", R.drawable.exclamation_message);
                                                        else if(provjeraMail(mail)){
                                                            alertingMessage("E-mail adresa nije u propisnom obliku.", R.drawable.exclamation_message);
                                                        }
                                                        else
                                                            KorisnikUredivanjeMethod.Upload(ime, prezime, korime, adresa, mail, mobitel, telefon, lozinka);
                                                        //swapFragment();
                                                    }
                                                }
                                            }
            );
       }
        catch (Exception e){
        }

        Button buttonOdustani=(Button) view.findViewById(R.id.btnUpdateOdustani);

        buttonOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });
        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageButton.setImageBitmap(bitmap);
                bit=bitmap;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public String BitmapTOString(Bitmap bitmap) {

        Bitmap bm = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
        return imgString;
    }

    private void swapFragment(){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, new PocetnaUlogirani());
        ft.commit();
    }
    private boolean provjeraNedozvoljeniZnakovi(String ime,String prezime,String korIme,String adresa,String mobitel,String telefon){
        String pattern = "[\\'|\\!|\\?|\\#|\\*|\\$|\\%|\\&|\\/]";
        Pattern p = Pattern.compile(pattern);
        boolean found=false;
        if(p.matcher(ime).find())
            return true;
        if(p.matcher(prezime).find())
            return true;
        if(p.matcher(korIme).find())
            return true;
        if(p.matcher(ime).find())
            return true;
        if(p.matcher(adresa).find())
            return true;
        if(p.matcher(mobitel).find())
            return true;
        if(p.matcher(telefon).find())
            return true;
        return found;
    }

    private boolean provjeraMail(String mail){
        String pattern = "^[A-z0-9][A-z0-9]*\\.?[A-z0-9]*@[A-z0-9]+\\.[A-z0-9]{2,}$";
        Pattern p = Pattern.compile(pattern);
        if(p.matcher(mail).matches())
            return false;
        else return true;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof KorisnikUredivanje.OnFragmentInteractionListener) {
            mListener = (KorisnikUredivanje.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStatusChanged(String s) {

    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String title);
    }
    private class ArticleFragment {
    }
}







