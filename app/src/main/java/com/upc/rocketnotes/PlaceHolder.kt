package com.upc.rocketnotes

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST;
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @GET("teachers")
    fun getTeachers(@Header("Authorization") authHeader: String): Call<List<TeacherResource>>

    @POST("teachers")
    fun addTeacher(
        @Body teacher: TeacherResource,
        @Header("Authorization") authHeader: String
    ): Call<TeacherResource>

    @GET("facilities")
    fun getFacilities(@Header("Authorization") authHeader: String): Call<List<FacilityResource>>

    @POST("facilities")
    fun addFacility(
        @Body facility: FacilityResource,
        @Header("Authorization") authHeader: String
    ): Call<FacilityResource>

    @PUT("facilities/{id}")
    fun updateFacility(
        @Path("id") id: String,
        @Body facility: FacilityResource,
        @Header("Authorization") authHeader: String
    ): Call<FacilityResource>

    @DELETE("facilities/{id}")
    fun deleteFacility(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String
    ): Call<Void>

    @GET("facilities/{id}")
    fun getFacilityById(
        @Path("id") id: String,
        @Header("Authorization") authHeader: String
    ): Call<FacilityResource>
}