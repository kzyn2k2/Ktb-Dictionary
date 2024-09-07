package com.ktbbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ImageView clearSearchBox;
    private ListViewAdapter adapter;
    private ListViewAdapter adapter2;
    private ListView lstShow;
    private ImageView search;
    private Cursor myCursor;
    private Cursor searchcursor;
    private EditText txtWord;
    private CheckBox chkexact;
    private DBHelper help;
    private AdapterView.OnItemClickListener onListItem = new AdapterView.OnItemClickListener() {
        @SuppressLint("Range")
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            MainActivity.this.searchcursor.moveToPosition(i);
            Intent intent = new Intent(MainActivity.this.getBaseContext(), Details.class);
            intent.putExtra("KANA", MainActivity.this.searchcursor.getString(MainActivity.this.searchcursor.getColumnIndex("kana")));
            intent.putExtra("MY_IMI", MainActivity.this.searchcursor.getString(MainActivity.this.searchcursor.getColumnIndex("my_imi")));
            intent.putExtra("KANJI", MainActivity.this.searchcursor.getString(MainActivity.this.searchcursor.getColumnIndex("kanji")));
            intent.putExtra("POS", MainActivity.this.searchcursor.getString(MainActivity.this.searchcursor.getColumnIndex("part_of_speech")));
            intent.putExtra("EG", MainActivity.this.searchcursor.getString(MainActivity.this.searchcursor.getColumnIndex("eg")));
            intent.putExtra("JP_IMI", MainActivity.this.searchcursor.getString(MainActivity.this.searchcursor.getColumnIndex("jp_imi")));
            if(MainActivity.this.searchcursor != null) {
                MainActivity.this.searchcursor.close();
            }
            if(MainActivity.this.myCursor != null) {
                MainActivity.this.myCursor.close();
            }
            startActivity(intent);


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getSupportActionBar() != null){
            this.getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        try {
            this.help = new DBHelper(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Db error", Toast.LENGTH_SHORT).show();
        }
        this.clearSearchBox = findViewById(R.id.clear_search_box);
        this.txtWord = findViewById(R.id.txttext);
        this.search = findViewById(R.id.search_image);
        this.lstShow = findViewById(R.id.lstShow);
        this.txtWord.addTextChangedListener(this);
        //startManagingCursor(this.myCursor);
        this.myCursor = this.help.getAll();
        this.search.setOnClickListener(this);
        this.clearSearchBox.setOnClickListener(this);
        this.lstShow.setOnItemClickListener(this.onListItem);
        this.adapter = new ListViewAdapter(this.myCursor);
        this.chkexact = findViewById(R.id.Chkbox);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (this.txtWord.getText().toString().equals("")) {
            this.clearSearchBox.setVisibility(View.GONE);
        } else {
            this.clearSearchBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear_search_box /* 2131230726 */:
                this.clearSearchBox.setVisibility(View.GONE);
                this.txtWord.setText("");
                return;
            case R.id.search_image /* 2131230727 */:
                this.searchcursor = this.help.search(this.txtWord.getText().toString(), this.chkexact.isChecked());
                //startManagingCursor(this.searchcursor);
                this.adapter2 = new ListViewAdapter(this.searchcursor);
                this.lstShow.setAdapter((ListAdapter) this.adapter2);
                return;
            default:
                return;
        }

    }

    class ListViewAdapter extends CursorAdapter{

        public ListViewAdapter(Cursor c){
            super(MainActivity.this, c);
        }

        @Override // android.widget.CursorAdapter
        public void bindView(View row, Context arg1, Cursor mycursor) {
            ResultList holder = (ResultList) row.getTag();
            holder.populateFrom(mycursor, MainActivity.this.help);
        }

        @Override // android.widget.CursorAdapter
        public View newView(Context arg0, Cursor arg1, ViewGroup parent) {
            LayoutInflater inflater = MainActivity.this.getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            ResultList holder = new ResultList(row);
            row.setTag(holder);
            return row;
        }

    }
}

