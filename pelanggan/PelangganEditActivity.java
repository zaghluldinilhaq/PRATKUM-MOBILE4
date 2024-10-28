package com.example.appslaundry.pelanggan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.appslaundry.R;
import com.example.appslaundry.database.SQLiteHelper;

public class PelangganEditActivity extends AppCompatActivity {
    private String id, name, email, hp;
    private EditText edPelEditNama, edPelEditEmail, edPelEditHp;
    private Button btnPelEditSimpan, btnPelEditHapus, btnPelEditBatal;
    private SQLiteHelper db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pelanggan_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new SQLiteHelper(this);

        // Retrieve intent extras
        id = getIntent().getStringExtra("id");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        hp = getIntent().getStringExtra("hp");

        // Initialize views
        edPelEditNama = findViewById(R.id.edPelEditNama);
        edPelEditEmail = findViewById(R.id.edPelEditEmail);
        edPelEditHp = findViewById(R.id.edPelEditHp);
        btnPelEditSimpan = findViewById(R.id.btnPelEditSimpan);
        btnPelEditHapus = findViewById(R.id.btnPelEditDelete);
        btnPelEditBatal = findViewById(R.id.btnPelEditBatal);

        // Set existing data to fields
        edPelEditNama.setText(name);
        edPelEditEmail.setText(email);
        edPelEditHp.setText(hp);

        btnPelEditSimpan.setOnClickListener(v -> updateCustomer());
        btnPelEditHapus.setOnClickListener(v -> deleteCustomer());
        btnPelEditBatal.setOnClickListener(v -> cancelEdit());
    }

    private void updateCustomer() {
        String updatedName = edPelEditNama.getText().toString();
        String updatedEmail = edPelEditEmail.getText().toString();
        String updatedHp = edPelEditHp.getText().toString();

        if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedHp.isEmpty()) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isUpdated = db.updatePelanggan(id, updatedName, updatedEmail, updatedHp);
        if (isUpdated) {
            Toast.makeText(this, "Customer updated successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, PelangganActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to update customer", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCustomer() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Confirmation")
                .setMessage("Are you sure you want to delete this customer?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    boolean isDeleted = db.deletePelanggan(id);
                    if (isDeleted) {
                        Toast.makeText(this, "Customer deleted successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, PelangganActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to delete customer", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelEdit() {
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelEdit();
    }
}
