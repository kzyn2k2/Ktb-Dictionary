package com.ktbbeta;

import android.database.Cursor;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

public class ResultList {

 private TextView result;
 private TextView word;

 public ResultList(View row) {
        this.word = row.findViewById(R.id.Word);
        this.result = row.findViewById(R.id.Result);
        Typeface font = Typeface.createFromAsset(row.getContext().getAssets(), "fonts/pyi.ttf");
        this.word.setTypeface(font);
        this.result.setTypeface(font);
 }

    public void populateFrom(Cursor mycursor, DBHelper help) {
        this.word.setText(String.valueOf(help.getItem(mycursor, 0)) + "." + help.getItem(mycursor, 1));
        this.result.setText(ZgToUni.zg2uni(help.getItem(mycursor, 2)));

    }

}
