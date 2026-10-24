package com.example.saizeriya;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        webView = findViewById(R.id.webView);

        // WebViewの基本的な設定
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // JavaScriptを使えるようにする
        webSettings.setDomStorageEnabled(true); // データの保存を使えるようにする

        // スマホの外部ブラウザに出ず、このアプリの中でホームページを開く設定
        webView.setWebViewClient(new WebViewClient());

        // 前の画面から送られてきたURLを受け取って読み込む
        String url = getIntent().getStringExtra("ORDER_URL");
        if (url != null) {
            webView.loadUrl(url);
        }
    }

    // 【戻るボタンのロック】スマホの「戻る」ボタンを押しても反応しないようにする
    @Override
    public void onBackPressed() {
        // 中身を空にすることで、戻るボタンを無効化（ロック）します
        // ユーザーはホームボタンを使ってアプリから抜けるスタイルになります
    }
}