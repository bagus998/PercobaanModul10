package com.example.percobaanmodul10;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.percobaanmodul10.api.ApiConfig;
import com.example.percobaanmodul10.model.Mahasiswa;
import com.example.percobaanmodul10.model.MahasiswaResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchMahasiswaActivity extends AppCompatActivity {
    private EditText edtChecNrp;
    private Button btnSearch;
    private ProgressBar progressBar;
    private TextView tvNrp;
    private TextView tvNama;
    private TextView tvEmail;
    private TextView tvJurusan;
    private Mahasiswa mahasiswa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_mahasiswa);
        edtChecNrp = findViewById(R.id.edtChckNrp);
        btnSearch = findViewById(R.id.btnSearch);
        progressBar = findViewById(R.id.progressBar);
        tvNrp = findViewById(R.id.tvValNrp);
        tvNama = findViewById(R.id.tvValNama);
        tvEmail = findViewById(R.id.tvValEmail);
        tvJurusan = findViewById(R.id.tvValJurusan);

        btnSearch.setOnClickListener(view -> {
            showLoading(true);
            String nrp = edtChecNrp.getText().toString();
            if (nrp.isEmpty()) {
                edtChecNrp.setError("Silakan isi NRP terlebih dahulu");
                showLoading(false);
            } else {
                Call<MahasiswaResponse> client = ApiConfig.getApiService().getMahasiswa(nrp);
                client.enqueue(new Callback<MahasiswaResponse>() {
                    @Override
                    public void onResponse(Call<MahasiswaResponse> call, Response<MahasiswaResponse> response) {
                        showLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            mahasiswa = response.body().getData();
                            setData(mahasiswa);
                        } else {
                            Log.e("API ERROR", "onResponse: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<MahasiswaResponse> call, Throwable t) {
                        showLoading(false);
                        Log.e("Error Retrofit", "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void setData(Mahasiswa mahasiswa) {
        tvNrp.setText(mahasiswa.getNrp());
        tvNama.setText(mahasiswa.getNama());
        tvEmail.setText(mahasiswa.getEmail());
        tvJurusan.setText(mahasiswa.getJurusan());
    }

    private void showLoading(Boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
