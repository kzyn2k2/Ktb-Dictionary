package com.ktbbeta;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.StringTokenizer;

public class Details extends Activity {

    private String hinshi;
    private String kana;
    private String kanji;
    private String kantanimi;
    private String mmtrans;
    private String reibun;
    Object testnull;
    private TextView txthinshi;
    private TextView txtkana;
    private TextView txtkanji;
    private TextView txtkantanimi;
    private TextView txtmmtrans;
    private TextView txtreibun;
    private TextView h1;
    private TextView h2;

    @Override // android.app.Activity
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.outputlist);
        this.txtkanji = findViewById(R.id.txtkanji);
        this.txthinshi = findViewById(R.id.txthinshi);
        this.txtkana = findViewById(R.id.txtkana);
        this.txtmmtrans = findViewById(R.id.txtmmtrans);
        this.txtreibun = findViewById(R.id.txtreibun);
        this.txtkantanimi = findViewById(R.id.txtkantanimi);
        this.h2 = findViewById(R.id.h2);
        this.h1 = findViewById(R.id.h1);
    }

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.kana = extras.getString("KANA");
            this.mmtrans = ZgToUni.zg2uni(extras.getString("MY_IMI"));
            this.kanji = extras.getString("KANJI");
            this.hinshi = extras.getString("POS");
            this.reibun = extras.getString("EG");
            this.kantanimi = extras.getString("JP_IMI");
        }
        this.testnull = this.kanji;
        if (this.testnull != null) {
            this.txtkanji.setText(this.kanji);
        }
        this.testnull = this.hinshi;
        if (this.testnull != null) {
            this.txthinshi.setText(" 【" + this.hinshi + "】");
        }
        this.txtkana.setText(this.kana);
        this.txtmmtrans.setText(this.mmtrans);
        this.testnull = this.reibun;
        if (this.testnull != null) {
            String cutreibun = "";
            this.h1.setText("例文");
            String setreibun = String.valueOf("\n\n") + this.reibun;
            StringTokenizer st = new StringTokenizer(setreibun, "。");
            while (st.hasMoreTokens()) {
                cutreibun = String.valueOf(cutreibun) + st.nextToken();
                if (st.hasMoreTokens()) {
                    cutreibun = String.valueOf(cutreibun) + "\n";
                }
            }
            this.txtreibun.setText(cutreibun);
        }
        this.testnull = this.kantanimi;
        if (this.testnull != null) {
            String cutimi = "";
            this.h2.setText("単語の意味");
            String imi = String.valueOf("\n\n") + this.kantanimi;
            StringTokenizer st2 = new StringTokenizer(imi, "。");
            while (st2.hasMoreTokens()) {
                cutimi = String.valueOf(cutimi) + st2.nextToken();
                if (st2.hasMoreTokens()) {
                    cutimi = String.valueOf(cutimi) + "\n";
                }
            }
            this.txtkantanimi.setText(cutimi);
        }
    }


}
