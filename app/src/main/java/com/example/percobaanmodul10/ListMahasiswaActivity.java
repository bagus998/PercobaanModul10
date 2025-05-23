package com.example.percobaanmodul10;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.percobaanmodul10.adapter.MahasiswaAdapter;
import com.example.percobaanmodul10.api.ApiConfig;
import com.example.percobaanmodul10.model.AddMahasiswaResponse;
import com.example.percobaanmodul10.model.Mahasiswa;
import com.example.percobaanmodul10.model.MahasiswaListResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMahasiswaActivity extends AppCompatActivity implements MahasiswaAdapter.OnItemClickListener {
    private RecyclerView rvMahasiswa;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private MahasiswaAdapter adapter;

    private ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadMahasiswaData(); // Refresh data after edit
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mahasiswa);

        initViews();
        setupRecyclerView();
        loadMahasiswaData();
    }

    private void initViews() {
        rvMahasiswa = findViewById(R.id.rvMahasiswa);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmpty);
    }

    private void setupRecyclerView() {
        adapter = new MahasiswaAdapter();
        adapter.setOnItemClickListener(this);
        rvMahasiswa.setLayoutManager(new LinearLayoutManager(this));
        rvMahasiswa.setAdapter(adapter);
    }

    private void loadMahasiswaData() {
        showLoading(true);

        Call<MahasiswaListResponse> client = ApiConfig.getApiService().getMahasiswaList();

        client.enqueue(new Callback<MahasiswaListResponse>() {
            @Override
            public void onResponse(Call<MahasiswaListResponse> call, Response<MahasiswaListResponse> response) {
                showLoading(false);

                Log.d("API_DEBUG", "Response code: " + response.code());
                Log.d("API_DEBUG", "Response successful: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    MahasiswaListResponse mahasiswaResponse = response.body();

                    Log.d("API_DEBUG", "Response status: " + mahasiswaResponse.isStatus());
                    Log.d("API_DEBUG", "Response message: " + mahasiswaResponse.getMessage());
                    Log.d("API_DEBUG", "Response data null: " + (mahasiswaResponse.getData() == null));

                    if (mahasiswaResponse.getData() != null) {
                        Log.d("API_DEBUG", "Data size: " + mahasiswaResponse.getData().size());
                    }

                    // Check if data exists, regardless of status field
                    if (mahasiswaResponse.getData() != null && !mahasiswaResponse.getData().isEmpty()) {
                        showEmptyData(false);
                        adapter.setMahasiswaList(mahasiswaResponse.getData());
                        Toast.makeText(ListMahasiswaActivity.this,
                                "Berhasil memuat " + mahasiswaResponse.getData().size() + " data mahasiswa",
                                Toast.LENGTH_SHORT).show();
                    } else if (mahasiswaResponse.getData() != null && mahasiswaResponse.getData().isEmpty()) {
                        showEmptyData(true);
                        Toast.makeText(ListMahasiswaActivity.this,
                                "Tidak ada data mahasiswa",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        showEmptyData(true);
                        String errorMsg = mahasiswaResponse.getMessage() != null ?
                                mahasiswaResponse.getMessage() : "Data tidak tersedia";
                        Toast.makeText(ListMahasiswaActivity.this,
                                "Gagal memuat data: " + errorMsg,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showEmptyData(true);
                    Log.e("API_ERROR", "onResponse failed: " + response.code());

                    // Try to log error body
                    try {
                        if (response.errorBody() != null) {
                            Log.e("API_ERROR", "Error body: " + response.errorBody().string());
                        }
                    } catch (Exception e) {
                        Log.e("API_ERROR", "Error reading error body: " + e.getMessage());
                    }

                    Toast.makeText(ListMahasiswaActivity.this,
                            "Gagal memuat data: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MahasiswaListResponse> call, Throwable t) {
                showLoading(false);
                showEmptyData(true);
                Log.e("API_FAILURE", "onFailure: " + t.getMessage());
                Toast.makeText(ListMahasiswaActivity.this,
                        "Koneksi gagal: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            rvMahasiswa.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            rvMahasiswa.setVisibility(View.VISIBLE);
        }
    }

    private void showEmptyData(boolean isEmpty) {
        if (isEmpty) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvMahasiswa.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvMahasiswa.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onEditClick(Mahasiswa mahasiswa, int position) {
        Intent intent = new Intent(this, EditMahasiswaActivity.class);
        intent.putExtra(EditMahasiswaActivity.EXTRA_MAHASISWA_NRP, mahasiswa.getNrp());
        intent.putExtra(EditMahasiswaActivity.EXTRA_MAHASISWA_NAMA, mahasiswa.getNama());
        intent.putExtra(EditMahasiswaActivity.EXTRA_MAHASISWA_EMAIL, mahasiswa.getEmail());
        intent.putExtra(EditMahasiswaActivity.EXTRA_MAHASISWA_JURUSAN, mahasiswa.getJurusan());
        editLauncher.launch(intent);
    }

    @Override
    public void onDeleteClick(Mahasiswa mahasiswa, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Apakah Anda yakin ingin menghapus data " + mahasiswa.getNama() + "?")
                .setPositiveButton("Ya", (dialog, which) -> deleteMahasiswa(mahasiswa, position))
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteMahasiswa(Mahasiswa mahasiswa, int position) {
        showLoading(true);

        Call<AddMahasiswaResponse> client = ApiConfig.getApiService().deleteMahasiswa(mahasiswa.getNrp());
        
        Log.d("API_DEBUG", "Delete NRP: " + mahasiswa.getNrp());

        client.enqueue(new Callback<AddMahasiswaResponse>() {
            @Override
            public void onResponse(Call<AddMahasiswaResponse> call, Response<AddMahasiswaResponse> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ListMahasiswaActivity.this, "Berhasil menghapus data!", Toast.LENGTH_SHORT).show();
                    adapter.removeMahasiswa(position);

                    // Check if list is empty after deletion
                    if (adapter.getItemCount() == 0) {
                        showEmptyData(true);
                    }
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
                    
                    Toast.makeText(ListMahasiswaActivity.this, "Gagal menghapus data: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddMahasiswaResponse> call, Throwable t) {
                showLoading(false);
                Log.e("API_FAILURE", "Delete failed: " + t.getMessage());
                Toast.makeText(ListMahasiswaActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}