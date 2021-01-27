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

import apnar.jela.loginandregistrationapp.model.OTPResponse;
import apnar.jela.loginandregistrationapp.model.RegistrationResponse;
import apnar.jela.loginandregistrationapp.webApi.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    //share preference
    public static final String MyPREFERENCES = "MyPrefs";
    private SharedPreferences sharedpreferences;
    private String otp_id, user_id, otp;

    private ApiInterface apiInterface;
    private EditText otpET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p);

        otpET = findViewById(R.id.otpET);

    }

    public void setOTP(View view) {

       setData();
    }


    public void setData() {

        //share preference
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        otp_id = sharedpreferences.getString(Common.OTP_ID, "");
        user_id = sharedpreferences.getString(Common.USER_ID, "");
        otp = sharedpreferences.getString(Common.OTP, "");
       // otp = otpET.getText().toString().trim();

        Toast.makeText(this, "user id: "+user_id, Toast.LENGTH_SHORT).show();

        otpET.setText(otp);

        apiInterface = RetrofitClient.getRetrofit().create(ApiInterface.class);

        final ProgressDialog mDialog = new ProgressDialog(OTPActivity.this);
        mDialog.setMessage("Please waiting...");
        mDialog.show();

        Call<OTPResponse> call = apiInterface.setOTPResponse(Integer.parseInt(otp_id),
                Integer.parseInt(user_id), Integer.parseInt(otp));

//        Call<OTPResponse> call = apiInterface.setOTPResponse(72,
//                31, 1234);

        call.enqueue(new Callback<OTPResponse>() {
            @Override
            public void onResponse(Call<OTPResponse> call, Response<OTPResponse> response) {

                OTPResponse res = response.body();
                if (response.code() == 200 && res != null) {
                    OTPResponse meg = response.body();

                    startActivity(new Intent(OTPActivity.this, LoginActivity.class));
                    Toast.makeText(getApplicationContext(), "" + meg.getMessage(), Toast.LENGTH_SHORT).show();

                    mDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<OTPResponse> call, Throwable t) {

                Toast.makeText(OTPActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("Faild: ", t.getMessage());
                //  Toast.makeText(MainActivity.this, "Failed "+t.getMessage(), Toast.LENGTH_SHORT).show();
                mDialog.dismiss();
            }
        });

    }
}