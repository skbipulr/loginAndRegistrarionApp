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

import apnar.jela.loginandregistrationapp.loginModel.LoginResponse;
import apnar.jela.loginandregistrationapp.model.OTPResponse;
import apnar.jela.loginandregistrationapp.webApi.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    private String mobile, password;
    EditText mobileET, passwordET;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobileET = findViewById(R.id.mobileET);
        passwordET = findViewById(R.id.passwordET);
    }

    public void loginBtn(View view) {

        setData();
    }

    public void setData() {
        mobile = mobileET.getText().toString().trim();
        password = passwordET.getText().toString().trim();

        apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);

        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        Call<LoginResponse> call = apiInterface.setUserInfoForLogin(mobile, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                LoginResponse res = response.body();
                if (response.code() == 200 && res != null) {

                   // startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "" + res.getMessage(), Toast.LENGTH_SHORT).show();

                    mDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                Toast.makeText(LoginActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Faild: ", t.getMessage());
                //  Toast.makeText(MainActivity.this, "Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

    }
}