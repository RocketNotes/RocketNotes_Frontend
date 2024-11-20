package com.upc.rocketnotes

import retrofit2.Call;
import retrofit2.Response
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

    // STUDENTS
    @GET("students")
    fun getStudents(@Header("Authorization") authHeader: String): Call<List<StudentResource>>

    @Headers("Content-Type: application/json")
    @POST("students")
    fun addStudent(
        @Body student: StudentResource,
        @Header("Authorization") authHeader: String // Asegúrate de que esto esté incluido
    ): Call<StudentResource>

    @DELETE("students/{id}")
    fun deleteStudent(@Path("id") studentId: Long, @Header("Authorization") authToken: String): Call<Void>

    @PUT("students/{id}")
    fun updateStudent(
        @Path("id") studentId: Long,
        @Body student: StudentResource,
        @Header("Authorization") authToken: String
    ): Call<StudentResource>

    //FACILITIES
    @POST("facilities")
    fun addFacilities(
        @Body facilitie: FacilitiesResource,
        @Header("Authorization") authHeader: String
    ):Call<FacilitiesResource>

    //TEACHERS
    @GET("teachers")
    fun getTeachers(@Header("Authorization") authHeader: String): Call<List<TeacherResource>>

    @POST("teachers")
    fun addTeacher(
        @Body teacher: TeacherResource,
        @Header("Authorization") authHeader: String
    ): Call<TeacherResource>
  
    @DELETE("teachers/{id}")
    fun deleteTeacher(@Path("id") teacherId: Long, @Header("Authorization") authToken: String): Call<Void>

    @PUT("teachers/{id}")
    fun updateTeacher(
        @Path("id") teacherId: Long,
        @Body teacher: TeacherResource,
        @Header("Authorization") authToken: String
    ): Call<TeacherResource>

    @GET("/api/v1/facilities")
    fun getAllFacilities(@Header("Authorization") token: String): Call<List<Facility>>

    @GET("/api/v1/facilities/{id}")
    fun getFacilityById(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<Facility>

    @POST("/api/v1/facilities")
    fun createFacility(
        @Body facility: Facility,
        @Header("Authorization") token: String
    ): Call<Facility>

    @PUT("/api/v1/facilities/{id}")
    fun updateFacility(
        @Path("id") id: Long,
        @Body facility: Facility,
        @Header("Authorization") token: String
    ): Call<Facility>

    @DELETE("/api/v1/facilities/{id}")
    fun deleteFacility(
        @Path("id") id: Long,
        @Header("Authorization") token: String
    ): Call<Void>
}