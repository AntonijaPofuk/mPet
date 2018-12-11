package mpet.project2018.air.mpet.prijava;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import Retrofit.DataPost.PrijavaMethod;
import Retrofit.RemotePost.onLoginValidation;
import mpet.project2018.air.mpet.MainActivity;
import mpet.project2018.air.mpet.R;
import mpet.project2018.air.mpet.fragments.Login;
import mpet.project2018.air.mpet.fragments.Pocetna;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements onLoginValidation {

    EditText edtUsername;
    EditText edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);

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

        // Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

        //validate form
        if (Integer.parseInt(id) != 0) {
            startActivity(new Intent(LoginActivity.this, Pocetna.class));
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ulogiraniKorisnikId",id); //provjerava ako je ulogirani

        }
        else {
            Toast.makeText(LoginActivity.this, "Korisnicko ime ili lozinka su netocni", Toast.LENGTH_SHORT).show();
        }

    }

}
