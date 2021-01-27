package apnar.jela.loginandregistrationapp;

import apnar.jela.loginandregistrationapp.loginModel.LoginResponse;
import apnar.jela.loginandregistrationapp.model.OTPResponse;
import apnar.jela.loginandregistrationapp.model.RegistrationResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("auth/register")
    Call<RegistrationResponse> setUserInfoForRegistration(
            @Field("email") String email,
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation);

    //http://ezze.dev/applycafe/api/otp-verify/otp/{otpId}/user/{userId}
    @FormUrlEncoded
    @POST("otp-verify/otp/{otpId}/user/{userId}")
    Call<OTPResponse> setOTPResponse(
            @Path("otpId") int otpId,
            @Path("userId") int userId,
            @Field("otp") int otp);

    @FormUrlEncoded
    @POST("auth/login")
    Call<LoginResponse> setUserInfoForLogin(
            @Field("mobile") String mobile,
            @Field("password") String password);

}
