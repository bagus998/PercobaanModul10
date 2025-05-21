package com.example.percobaanmodul10.model;

import com.google.gson.annotations.SerializedName;

public class MahasiswaResponse {
    @SerializedName("data")
    private Mahasiswa data;

    @SerializedName("status")
    private boolean status;

    public Mahasiswa getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }
}
