package com.jojo.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button update, remove, add;
    dbHandler handler;
    LinearLayout layout;
    TextView record;
    Handler repeat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        update = findViewById(R.id.updateButt);
        remove = findViewById(R.id.deleteButt);
        add = findViewById(R.id.addButt);
        layout = findViewById(R.id.linearLayout);
        record = findViewById(R.id.records);
        handler = new dbHandler(this);
        repeat = new Handler();
        repeatTask();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View form = inflater.inflate(R.layout.add_data, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add data")
                        .setView(form)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText name = form.findViewById(R.id.add_name);
                                EditText sem = form.findViewById(R.id.add_sem);
                                if(name.getText().toString().isEmpty() && sem.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Fill the empty fields", Toast.LENGTH_LONG).show();
                                } else {
                                    handler.insertData(name.getText().toString(), sem.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Data uploaded succcessfully", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View form = inflater.inflate(R.layout.remove_data, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter ID to remove data")
                        .setView(form)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText id = form.findViewById(R.id.remove_id);
                                if(id.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Fill the empty fields", Toast.LENGTH_LONG).show();
                                } else {
                                    updateData(id.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View form = inflater.inflate(R.layout.remove_data, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter ID to remove data")
                        .setView(form)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText id = form.findViewById(R.id.remove_id);
                                if(id.getText().toString().isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Fill the empty fields", Toast.LENGTH_LONG).show();
                                } else {
                                    handler.deleteData(id.getText().toString());
                                    Toast.makeText(getApplicationContext(), "Data deleted succcessfully", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
    }

    final Runnable status = new Runnable() {
        @Override
        public void run() {
            try {
                readData();
            } finally {
                repeat.postDelayed(status, 500);
            }
        }
    };
    void repeatTask(){
        status.run();
    }

    private void updateData(final String id) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.add_data, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Details to Update Data")
                .setView(view1)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name1 = view1.findViewById(R.id.add_name);
                        EditText sem1 = view1.findViewById(R.id.add_sem);
                        handler.updateData(id, name1.getText().toString(), sem1.getText().toString());
                        Toast.makeText(MainActivity.this, "Data Updated Successfully",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
        readData();
    }
    @SuppressLint("SetTextI18n")
    public void readData(){
        layout.removeAllViews();
        List<stdDetails> details = new dbHandler(this).getAllData();
        if(details.size() > 0){
            record.setVisibility(View.VISIBLE);
            record.setGravity(Gravity.CENTER_HORIZONTAL);
            String textViewData = "ID - Name - Sem";
            TextView tv = new TextView(this);
            tv.setPadding(10, 10, 10, 10);
            tv.setText(textViewData);
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            layout.addView(tv);
            record.setText(details.size() + " Records found!");
            for (stdDetails obj : details) {
                int id = obj.id;
                String studentName = obj.name;
                int studentEmail = obj.sem;
                String textViewContents = id+ " - " + studentName + " - " + studentEmail;
                TextView item = new TextView(this);
                item.setPadding(10, 10, 10, 10);
                item.setText(textViewContents);
                item.setTextSize(20);
                item.setGravity(Gravity.CENTER_HORIZONTAL);
                layout.addView(item);
                update.setVisibility(View.VISIBLE);
                remove.setVisibility(View.VISIBLE);
            }
        }
        else {
            record.setVisibility(View.VISIBLE);
            record.setGravity(Gravity.CENTER_HORIZONTAL);
            record.setText("No Records found!");
            update.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
        }
    }

}
