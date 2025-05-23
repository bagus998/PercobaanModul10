package com.example.percobaanmodul10.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.percobaanmodul10.R;
import com.example.percobaanmodul10.model.Mahasiswa;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.MahasiswaViewHolder> {
    private List<Mahasiswa> mahasiswaList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Mahasiswa mahasiswa, int position);
        void onDeleteClick(Mahasiswa mahasiswa, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setMahasiswaList(List<Mahasiswa> mahasiswaList) {
        this.mahasiswaList = mahasiswaList;
        notifyDataSetChanged();
    }

    public void removeMahasiswa(int position) {
        mahasiswaList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public MahasiswaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mahasiswa, parent, false);
        return new MahasiswaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MahasiswaViewHolder holder, int position) {
        Mahasiswa mahasiswa = mahasiswaList.get(position);
        holder.bind(mahasiswa, listener, position);
    }

    @Override
    public int getItemCount() {
        return mahasiswaList.size();
    }

    static class MahasiswaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama;
        private TextView tvNrp;
        private TextView tvEmail;
        private TextView tvJurusan;

        public MahasiswaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNrp = itemView.findViewById(R.id.tvNrp);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvJurusan = itemView.findViewById(R.id.tvJurusan);
        }

        public void bind(Mahasiswa mahasiswa, OnItemClickListener listener, int position) {
            tvNama.setText(mahasiswa.getNama());
            tvNrp.setText("NRP: " + mahasiswa.getNrp());
            tvEmail.setText("Email: " + mahasiswa.getEmail());
            tvJurusan.setText("Jurusan: " + mahasiswa.getJurusan());

            if (listener != null) {
                itemView.setOnClickListener(v -> listener.onEditClick(mahasiswa, position));
                itemView.setOnLongClickListener(v -> {
                    listener.onDeleteClick(mahasiswa, position);
                    return true;
                });
            }
        }
    }
}