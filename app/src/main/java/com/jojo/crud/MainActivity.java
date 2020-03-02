package com.jojo.crud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton add;
    Button update, remove;
    LinearLayout layout;
    studentDBHandler db;
    TextView count;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add = findViewById(R.id.button_add);
        update = findViewById(R.id.updateButt);
        remove = findViewById(R.id.removeButt);
        layout = findViewById(R.id.linearLayout);
        count = findViewById(R.id.textViewCount);
        db = new studentDBHandler(this);

        handler = new Handler();
        repeatTask();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.add_details, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Data")
                        .setView(formElementsView)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText name = formElementsView.findViewById(R.id.add_name);
                                EditText sem = formElementsView.findViewById(R.id.add_sem);
                                db.insertData(name.getText().toString(), sem.getText().toString());
                                Toast.makeText(MainActivity.this, "Data Added Successfully",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                readRecords();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.remove_details, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter ID to remove Data")
                        .setView(view)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText id = view.findViewById(R.id.remove_id);
                                db.deleteData(id.getText().toString());
                                Toast.makeText(MainActivity.this, "Data Removed Successfully",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                readRecords();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.remove_details, null, false);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Enter ID to remove Data")
                        .setView(view)
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final EditText id = view.findViewById(R.id.remove_id);
                                updateData(id.getText().toString());
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
                readRecords();
            } finally {
                handler.postDelayed(status, 500);
            }
        }
    };
    void repeatTask(){
        status.run();
    }


    private void updateData(final String id) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view1 = inflater.inflate(R.layout.add_details, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Enter Details to Update Data")
                .setView(view1)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name1 = view1.findViewById(R.id.add_name);
                        EditText sem1 = view1.findViewById(R.id.add_sem);
                        db.updateData(id, name1.getText().toString(), sem1.getText().toString());
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
    }

    public void readRecords() {
        layout.removeAllViews();
        List<studentDetails> students = new studentDBHandler(this).getAllData();
        if (students.size() > 0) {
            count.setVisibility(View.VISIBLE);
            count.setGravity(Gravity.CENTER_HORIZONTAL);
            count.setText(students.size() + " Records found!");
            for (studentDetails obj : students) {
                int id = obj.id;
                String studentName = obj.name;
                int studentEmail = obj.sem;
                String textViewContents = studentName + " - " + studentEmail;
                TextView item = new TextView(this);
                item.setPadding(10, 10, 10, 10);
                item.setText(textViewContents);
                item.setTextSize(20);
                item.setGravity(Gravity.CENTER_HORIZONTAL);
                item.setTag(Integer.toString(id));
                layout.addView(item);
                update.setVisibility(View.VISIBLE);
                remove.setVisibility(View.VISIBLE);
            }
        }
        else {
            TextView item = new TextView(this);
            item.setPadding(8, 8, 8, 8);
            item.setText("No records yet.");
            layout.addView(item);
        }
    }
}