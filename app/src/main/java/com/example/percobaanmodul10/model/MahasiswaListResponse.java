package com.example.percobaanmodul10.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MahasiswaListResponse {
    @SerializedName("data")
    private List<Mahasiswa> data;

    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    public List<Mahasiswa> getData() {
        return data;
    }

    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}