package mpet.project2018.air.mpet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import Retrofit.DataPost.PrijavaMethod;
import Retrofit.RemotePost.onLoginValidation;
import mpet.project2018.air.mpet.fragments.Login;
import mpet.project2018.air.mpet.fragments.Pocetna_neulogirani;
import mpet.project2018.air.mpet.fragments.Pocetna_ulogirani;
import mpet.project2018.air.mpet.fragments.Registracija;

public class LoginActivity extends AppCompatActivity implements onLoginValidation {

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;
    Button btnPrijavaOdustani;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnPrijavaOdustani = (Button) findViewById(R.id.btnPrijavaOdustani);


        SharedPreferences sharedPreferences=getSharedPreferences("MyPreferences",0);
        if (sharedPreferences.getString("ulogiraniKorisnikId","").toString().equals("ulogiraniKorisnikId")){
            Intent intent=new Intent(LoginActivity.this,Pocetna_ulogirani.class);
            startActivity(intent);
        }

      /*  btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapFragment();
            }
        });

        private void swapFragment(){
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, new Pocetna_neulogirani());
            ft.commit();
        }*/
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                //validate form
                if(validateLogin(username, password)){
                    //do login
                    doLogin(username, password);
                }
            }
        });


        btnPrijavaOdustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrame, new Pocetna_ulogirani());
                ft.commit();

            }
        });

    }

    private boolean validateLogin(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Potrebno je unijeti korisničko ime...", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(password == null || password.trim().length() == 0){
            Toast.makeText(this, "Potrebno je unijeti lozinku...", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doLogin(final String username, final String password) {
        String username1 = edtUsername.getText().toString();
        String password1 = edtPassword.getText().toString();

        PrijavaMethod postMetodaZaPrijavu = new PrijavaMethod(this);

        String id = "";

        String response = "";

        postMetodaZaPrijavu.Upload(username1,password1);
    }

    @Override
    public void onDataLoaded(String id) {

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        //validate form
        if (Integer.parseInt(id) != 0) {

           // startActivity(new Intent(LoginActivity.this, Pocetna_ulogirani.class));
            //startActivity(new Intent(LoginActivity.this,Pocetna_ulogirani.class));


            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ulogiraniKorisnikId",id); //provjerava od tud vrijednosti gore ako je ulogirani
            //------------------------
            editor.commit();


            Intent intent = new Intent(LoginActivity.this, Pocetna_ulogirani.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(LoginActivity.this, "Korisnicko ime ili lozinka su netocni", Toast.LENGTH_SHORT).show();
        }

    }


}
