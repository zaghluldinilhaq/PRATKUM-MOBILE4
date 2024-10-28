package com.example.appslaundry.pelanggan;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.appslaundry.R;
import com.example.appslaundry.adapter.AdapterPelanggan;
import com.example.appslaundry.database.SQLiteHelper;
import com.example.appslaundry.model.ModelPelanggan;

import java.util.ArrayList;
import java.util.List;

public class PelangganActivity extends AppCompatActivity {

    SQLiteHelper db; // Untuk operasi database SQLite
    Button btnPelAdd; // Tombol untuk tambah pelanggan
    RecyclerView rvPelanggan; // RecyclerView untuk menampilkan data pelanggan
    AdapterPelanggan adapterPelanggan; // Adapter untuk RecyclerView
    ArrayList<ModelPelanggan> list; // List untuk menyimpan data pelanggan
    ProgressDialog progressDialog; // Untuk menampilkan loading dialog
    AlphaAnimation btnAnimasi = new AlphaAnimation(1F, 0.5F); // Animasi tombol

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pelanggan);

        // Set window insets untuk system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi view dan event handling
        setView();
        eventHandling();
        getData();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.startAnimation(btnAnimasi); // Menjalankan animasi tombol
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            ModelPelanggan mp = list.get(position); // Ambil data pelanggan dari posisi yang diklik
            // kirimdata ke activity lain
            Intent intent = new Intent(PelangganActivity.this, PelangganEditActivity.class);
            intent.putExtra("id", mp.getId());
            intent.putExtra("name", mp.getId());
            intent.putExtra("email", mp.getId());
            intent.putExtra("hp", mp.getId());
            startActivity(intent);

        }
    };

    private void getData() {
        list.clear(); // Bersihkan list sebelum mengambil data baru
        showMsg(); // Tampilkan loading dialog
        progressDialog.dismiss(); // Sembunyikan loading dialog setelah data berhasil diambil

        try {
            List<ModelPelanggan> p = db.getPelanggan(); // Ambil data dari database
            if (p.size() > 0) {
                for (ModelPelanggan pel : p) {
                    ModelPelanggan mp = new ModelPelanggan();
                    mp.setId(pel.getId());
                    mp.setNama(pel.getNama());
                    mp.setEmail(pel.getEmail());
                    mp.setHp(pel.getHp());
                    list.add(mp); // Tambahkan data ke list
                }

                // Inisialisasi adapter dan set ke RecyclerView
                adapterPelanggan = new AdapterPelanggan(this, list);
                adapterPelanggan.notifyDataSetChanged(); // Beritahu adapter bahwa ada data baru
                rvPelanggan.setAdapter(adapterPelanggan);

                // Set event handling untuk klik item di RecyclerView
                adapterPelanggan.setOnItemClickListener(onClickListener);
            } else {
                Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventHandling() {
        btnPelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PelangganActivity.this, PelangganAddActivity.class));
            }
        });
    }

    private void setView() {
        db = new SQLiteHelper(this); // Inisialisasi SQLiteHelper
        progressDialog = new ProgressDialog(this); // Inisialisasi ProgressDialog
        btnPelAdd = findViewById(R.id.btnPLAdd); // Temukan tombol tambah pelanggan dari layout
        rvPelanggan = findViewById(R.id.rvPelanggan); // Temukan RecyclerView dari layout
        list = new ArrayList<>(); // Inisialisasi list untuk menampung data pelanggan

        // Inisialisasi LinearLayoutManager untuk RecyclerView
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL); // Mengatur orientasi menjadi vertikal
        rvPelanggan.setHasFixedSize(true); // Optimalkan ukuran RecyclerView
        rvPelanggan.setLayoutManager(llm); // Set layout manager ke RecyclerView
    }

    private void showMsg() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Informasi");
            progressDialog.setMessage("Loading Data...");
            progressDialog.setCancelable(false); // Tidak bisa dibatalkan dengan menekan di luar
        }
        progressDialog.show(); // Tampilkan dialog
    }
}
