package com.example.bai6_1sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText editmalop, edittenlop, editsiso;
    Button btninsert, btndelete, btnupdate, btnquery;
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        editmalop = findViewById(R.id.editmalop);
        edittenlop = findViewById(R.id.edittenlop);
        editsiso = findViewById(R.id.editsiso);
        btninsert = findViewById(R.id.btninsert);
        btndelete = findViewById(R.id.btndelete);
        btnupdate = findViewById(R.id.btnupdate);
        btnquery = findViewById(R.id.btnquery);
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,mylist);
        lv.setAdapter(myadapter);
        mydatabase = openOrCreateDatabase("qlsinhvien.db",MODE_PRIVATE,null);
        try {
            String sql = "Create table tbllop(malop Text primary key,tenlop Text, siso INTEGER)";
            mydatabase.execSQL(sql);
        }catch (Exception e)
        {
            Log.e("Error","Table đã tồn tại");
        }
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = editmalop.getText().toString();
                String tenlop = edittenlop.getText().toString();
                int siso = Integer.parseInt(editsiso.getText().toString());
                ContentValues myvalue = new ContentValues();
                myvalue.put("malop",malop);
                myvalue.put("tenlop",tenlop);
                myvalue.put("siso",siso);
                String msg = "";
                if(mydatabase.insert("tbllop",null,myvalue) == -1)
                {
                    msg = "Fail to Insert Record!";
                }
                else {
                    msg = "Insert record Succesfully";
                }
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String malop = editmalop.getText().toString();
                int n = mydatabase.delete("tbllop","malop=?",new String[]{malop});
                String msg = "";
                if(n == 0)
                {
                    msg = "No record to Delete";
                }
                else {
                    msg = n +"record is deleted";
                }
                Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int siso = Integer.parseInt(editsiso.getText().toString());
                String malop = editmalop.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("siso",siso);
                int n = mydatabase.delete("tbllop","malop=?",new String[]{malop});
                String msg = "";
                if(n == 0)
                {
                    msg = "No record to Update";
                }
                else {
                    msg = n +"record is update";
                }
                Toast.makeText(MainActivity.this,msg, Toast.LENGTH_SHORT).show();
            }
        });
        btnquery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mylist.clear();
                Cursor c = mydatabase.query("tbllop",null,null,null,null,null,null);
                c.moveToNext();
                String data = "";
                while (c.isAfterLast()== false){
                    data = c.getString(0)+"-"+c.getString(1)+"-"+c.getString(2);
                    c.moveToNext();
                    mylist.add(data);
                }
                c.close();
                myadapter.notifyDataSetChanged();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}