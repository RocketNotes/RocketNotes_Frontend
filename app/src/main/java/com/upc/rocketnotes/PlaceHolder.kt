package com.upc.rocketnotes

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface PlaceHolder {

    @POST("authentication/sign-up")
    fun signUp(@Body signUpResource: SignUpResource): Call<UserResource>

    @POST("authentication/sign-in")
    fun signIn(@Body signInRequest: SignInRequest): Call<SignInResponse>
}