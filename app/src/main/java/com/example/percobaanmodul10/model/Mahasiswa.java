package com.example.percobaanmodul10.model;

import com.google.gson.annotations.SerializedName;

public class Mahasiswa {
    @SerializedName("nama")
    private String nama;
    @SerializedName("prodi")
    private String jurusan;
    @SerializedName("id")

    private String id;
    @SerializedName("nim")
    private String nrp;
    @SerializedName("email")
    private String email;
    public String getNama(){
        return nama;
    }
    public String getJurusan(){
        return jurusan;
    }
    public String getId(){
        return id;
    }
    public String getNrp(){
        return nrp;
    }
    public String getEmail(){
        return email;
    }
}
