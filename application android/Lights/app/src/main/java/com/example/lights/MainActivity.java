package com.example.lights;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    Button add;
    LinearLayout layout;
    AlertDialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button rapport=findViewById(R.id.rapport);
        rapport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,ReportActivity.class);
                startActivity(i);
            }
        });
        add = findViewById(R.id.add);
        layout = findViewById(R.id.container);
        buildDialog();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

            }
        });

    }
    private void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog, null);

        EditText name = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Nom de la lampe").setPositiveButton("Ajouter", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard(name.getText().toString());
                        String nom=name.getText().toString();
                        saveData(nom);
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog = builder.create();
        saveData(name.toString());


    }
    private void addCard(String name) {
        final View view = getLayoutInflater().inflate(R.layout.item, null);

        TextView nameView = view.findViewById(R.id.nom);
        saveData(nameView.toString());
        TextView etat=view.findViewById(R.id.etat);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = mDatabase.getReference().child("Lampe").child("etat");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String medi1 = dataSnapshot.child(date).getValue().toString();
                    etat.setText(medi1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        Button delete = view.findViewById(R.id.delete);
        nameView.setText(name);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.removeView(view);
            }
        });

        layout.addView(view);
    }



    private void saveData(String input) {

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        Log.d("instance",mDatabase.toString());
        DatabaseReference reference = mDatabase.getReference().child("Lampe").child("Nom");
        reference.setValue(input);




    }

}