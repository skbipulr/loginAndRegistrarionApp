package apnar.jela.loginandregistrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import apnar.jela.loginandregistrationapp.model.RegistrationResponse;
import apnar.jela.loginandregistrationapp.webApi.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    //SharedPreferences
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;

    EditText emailET, passwordET, passwordConfirmationET, mobileET;
    String email, password, passwordConfirmation, mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);
        passwordConfirmationET = findViewById(R.id.passwordConfirmationET);
        mobileET = findViewById(R.id.mobileET);

    }

    public void registrationBtn(View view) {
        setData();
    }

    public void  setData(){
        email = emailET.getText().toString().trim();
        password = passwordET.getText().toString().trim();
        passwordConfirmation = passwordConfirmationET.getText().toString().trim();
        mobile = mobileET.getText().toString().trim();

        apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);

        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        Call<RegistrationResponse> call = apiInterface.setUserInfoForRegistration(email,mobile,password,passwordConfirmation);

        call.enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {

                RegistrationResponse res = response.body();
                if (response.code() == 200 && res != null) {
                    RegistrationResponse meg = response.body();

                    startActivity(new Intent(MainActivity.this, OTPActivity.class));
                    Toast.makeText(getApplicationContext(), "" + meg.getMessage(), Toast.LENGTH_SHORT).show();

                    //INI SHAREPREFE
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Common.OTP_ID, String.valueOf(meg.getData().getId()));
                    editor.putString(Common.USER_ID, String.valueOf(meg.getData().getUserId()));
                    editor.putString(Common.OTP, String.valueOf(meg.getData().getOtp()));
                    editor.apply();

                    mDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {

                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Faild: ", t.getMessage());
                //  Toast.makeText(MainActivity.this, "Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

    }
}