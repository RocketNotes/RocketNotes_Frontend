package com.upc.rocketnotes

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST;

interface PlaceHolder {

    @POST("authentication/sign-up")
    fun signUp(@Body signUpResource: SignUpResource): Call<UserResource>

    @POST("authentication/sign-in")
    fun signIn(@Body signInRequest: SignInRequest): Call<SignInResponse>


    @GET("students")
    fun getStudents(@Header("Authorization") authHeader: String): Call<List<StudentResource>>

    @Headers("Content-Type: application/json")
    @POST("students")
    fun addStudent(
        @Body student: StudentResource,
        @Header("Authorization") authHeader: String // Asegúrate de que esto esté incluido
    ): Call<StudentResource>
}