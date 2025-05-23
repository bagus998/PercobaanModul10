package com.example.percobaanmodul10.api;

import com.example.percobaanmodul10.model.AddMahasiswaResponse;
import com.example.percobaanmodul10.model.MahasiswaResponse;
import com.example.percobaanmodul10.model.MahasiswaListResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/mahasiswa/{nim}")
    Call<MahasiswaResponse> getMahasiswa(@Path("nim") String nrp);

    @POST("/mahasiswa/new")
    @FormUrlEncoded
    Call<AddMahasiswaResponse> addMahasiswa(
            @Field("nim") String nrp,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("prodi") String jurusan
    );

    @GET("/mahasiswa/all")
    Call<MahasiswaListResponse> getMahasiswaList();

    @PUT("/mahasiswa/{nim}")
    @FormUrlEncoded
    Call<AddMahasiswaResponse> updateMahasiswa(
            @Path("nim") String nrp,
            @Field("nama") String nama,
            @Field("email") String email,
            @Field("prodi") String jurusan
    );

    @DELETE("/mahasiswa/{nim}")
    Call<AddMahasiswaResponse> deleteMahasiswa(
            @Path("nim") String nrp
    );
}