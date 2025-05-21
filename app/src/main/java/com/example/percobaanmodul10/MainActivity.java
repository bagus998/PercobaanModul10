package com.example.percobaanmodul10;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.percobaanmodul10.api.ApiConfig;
import com.example.percobaanmodul10.model.AddMahasiswaResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText edtNrp;
    private EditText edtNama;
    private EditText edtEmail;
    private EditText edtJurusan;
    private ProgressBar progressBar;
    private Button btnAdd;
    private Button btnListData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtNrp = findViewById(R.id.edtNrp);
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtJurusan = findViewById(R.id.edtJurusan);
        progressBar = findViewById(R.id.progressBar);
        btnAdd = findViewById(R.id.btnAdd);
        btnListData = findViewById(R.id.btnList);
        btnAdd.setOnClickListener(view -> {
            addDataMahasiswa();
        });
        btnListData.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchMahasiswaActivity.class);
            startActivity(intent);
        });
    }
    private void addDataMahasiswa() {
        showLoading(true);
        String nrp = edtNrp.getText().toString();
        String nama = edtNama.getText().toString();
        String email = edtEmail.getText().toString();
        String jurusan = edtJurusan.getText().toString();

        if (nrp.isEmpty() || nama.isEmpty() || email.isEmpty() || jurusan.isEmpty()) {
            Toast.makeText(MainActivity.this, "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show();
            showLoading(false);
        } else {
            Call<AddMahasiswaResponse> client = ApiConfig.getApiService().addMahasiswa(nrp, nama, email, jurusan);

            client.enqueue(new Callback<AddMahasiswaResponse>() {
                @Override
                public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
                    showLoading(false);
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(MainActivity.this, "Berhasil menambahkan! Silakan cek data pada halaman list!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("API_ERROR", "onResponse failed: " + response.errorBody());
                        Toast.makeText(MainActivity.this, "Gagal menambahkan data: " + response.body(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                    showLoading(false);
                    Log.e("API_FAILURE", "onFailure: " + t.getMessage());
                    Toast.makeText(MainActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showLoading(Boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}