package com.example.percobaanmodul10;

import android.app.AlertDialog;
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

public class EditMahasiswaActivity extends AppCompatActivity {
    public static final String EXTRA_MAHASISWA_NRP = "extra_mahasiswa_nrp";
    public static final String EXTRA_MAHASISWA_NAMA = "extra_mahasiswa_nama";
    public static final String EXTRA_MAHASISWA_EMAIL = "extra_mahasiswa_email";
    public static final String EXTRA_MAHASISWA_JURUSAN = "extra_mahasiswa_jurusan";

    private EditText edtNrp;
    private EditText edtNama;
    private EditText edtEmail;
    private EditText edtJurusan;
    private ProgressBar progressBar;
    private Button btnUpdate;
    private Button btnCancel;
    private Button btnDelete;

    private String originalNrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_mahasiswa);

        initViews();
        getDataFromIntent();
        setupClickListeners();
    }

    private void initViews() {
        edtNrp = findViewById(R.id.edtNrp);
        edtNama = findViewById(R.id.edtNama);
        edtEmail = findViewById(R.id.edtEmail);
        edtJurusan = findViewById(R.id.edtJurusan);
        progressBar = findViewById(R.id.progressBar);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnCancel = findViewById(R.id.btnCancel);
        btnDelete = findViewById(R.id.btnDelete);
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        originalNrp = intent.getStringExtra(EXTRA_MAHASISWA_NRP);
        String nama = intent.getStringExtra(EXTRA_MAHASISWA_NAMA);
        String email = intent.getStringExtra(EXTRA_MAHASISWA_EMAIL);
        String jurusan = intent.getStringExtra(EXTRA_MAHASISWA_JURUSAN);

        edtNrp.setText(originalNrp);
        edtNama.setText(nama);
        edtEmail.setText(email);
        edtJurusan.setText(jurusan);
    }

    private void setupClickListeners() {
        btnUpdate.setOnClickListener(v -> updateMahasiswa());
        btnCancel.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> confirmDelete());
    }

    private void confirmDelete() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus data ini?")
                .setPositiveButton("Ya", (dialog, which) -> deleteMahasiswa())
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteMahasiswa() {
        showLoading(true);

        Call<AddMahasiswaResponse> client = ApiConfig.getApiService().deleteMahasiswa(originalNrp);
        
        Log.d("API_DEBUG", "Delete NRP: " + originalNrp);

        client.enqueue(new Callback<AddMahasiswaResponse>() {
            @Override
            public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditMahasiswaActivity.this, "Berhasil menghapus data!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Log.e("API_ERROR", "Delete failed: " + response.code());
                    
                    // Try to log error body
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR", "Error body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error reading error body: " + e.getMessage());
                    }
                    
                    Toast.makeText(EditMahasiswaActivity.this, "Gagal menghapus data: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                showLoading(false);
                Log.e("API_FAILURE", "Delete failed: " + t.getMessage());
                Toast.makeText(EditMahasiswaActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateMahasiswa() {
        showLoading(true);

        String newNrp = edtNrp.getText().toString().trim();
        String nama = edtNama.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String jurusan = edtJurusan.getText().toString().trim();

        if (newNrp.isEmpty() || nama.isEmpty() || email.isEmpty() || jurusan.isEmpty()) {
            Toast.makeText(this, "Silahkan lengkapi form terlebih dahulu", Toast.LENGTH_SHORT).show();
            showLoading(false);
            return;
        }

        // We can only update the name, email, and jurusan
        // The NRP (nim) is used as the identifier in the path
        Call<AddMahasiswaResponse> client = ApiConfig.getApiService().updateMahasiswa(originalNrp, nama, email, jurusan);
        
        Log.d("API_DEBUG", "Update URL: " + client.request().url());
        Log.d("API_DEBUG", "Original NRP: " + originalNrp);

        client.enqueue(new Callback<AddMahasiswaResponse>() {
            @Override
            public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditMahasiswaActivity.this, "Berhasil mengupdate data!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Log.e("API_ERROR", "onResponse failed: " + response.code());
                    
                    // Try to log error body
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.e("API_ERROR", "Error body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error reading error body: " + e.getMessage());
                    }
                    
                    Toast.makeText(EditMahasiswaActivity.this, "Gagal mengupdate data: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                showLoading(false);
                Log.e("API_FAILURE", "onFailure: " + t.getMessage());
                Toast.makeText(EditMahasiswaActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnUpdate.setEnabled(false);
            btnDelete.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnUpdate.setEnabled(true);
            btnDelete.setEnabled(true);
        }
    }
}