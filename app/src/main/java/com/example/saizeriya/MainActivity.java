package com.example.saizeriya;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.webkit.WebViewCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvErrorMessage = findViewById(R.id.tvErrorMessage);

        // 【追加】外部のリンクなどからアプリが起動された場合、そのURLを受け取る
        Intent intent = getIntent();
        String action = intent.getAction();
        
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri data = intent.getData();
            if (data != null) {
                String externalUrl = data.toString();
                // 受け取ったURLを判定処理に渡す
                checkQrCodeAndOpen(externalUrl);
                return;
            }
        }

        // 通常起動時のテスト用URL（必要に応じてQRリーダーの処理に書き換えます）
        String scannedUrl = "https://ioes.saizeriya.co.jp";
        checkQrCodeAndOpen(scannedUrl);
    }

    // QRコードやURL、WebViewのバージョンを判定する関数
    private void checkQrCodeAndOpen(String url) {
        // 1. スマホの WebView のバージョンを調べる
        int webViewMajorVersion = getWebViewMajorVersion();

        // 2. バージョンが 130以下 のときは、ふつうのブラウザで開く
        if (webViewMajorVersion > 0 && webViewMajorVersion <= 130) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return;
        }

        // 3. 読み取ったURLが正しい注文用URLかチェックする
        if ("https://ioes.saizeriya.co.jp".equals(url)) {
            // 正しいURLなら注文画面（OrderActivity）へ進む
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            intent.putExtra("ORDER_URL", url);
            startActivity(intent);
        } else {
            // 違うURLならエラーメッセージを表示
            tvErrorMessage.setText("このコードは注文用QRではありませんもう一度正しいコードを読み取ってください");
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    // WebViewのバージョンを調べる関数
    private int getWebViewMajorVersion() {
        try {
            PackageInfo packageInfo = WebViewCompat.getCurrentWebViewPackage(this);
            if (packageInfo != null && packageInfo.versionName != null) {
                String versionName = packageInfo.versionName;
                String majorVersionStr = versionName.split("\\.")[0];
                return Integer.parseInt(majorVersionStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}