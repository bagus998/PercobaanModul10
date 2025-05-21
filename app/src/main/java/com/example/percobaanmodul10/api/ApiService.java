package com.example.percobaanmodul10.api;

import com.example.percobaanmodul10.model.AddMahasiswaResponse;
import com.example.percobaanmodul10.model.MahasiswaResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
}
