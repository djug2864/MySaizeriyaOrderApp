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

        // エラーメッセージを表示する部品を結びつける
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        
        // 【テスト用】QRコードが読み取られたと仮定したURL
        String scannedUrl = "https://ioes.saizeriya.co.jp";
        
        // QRコードを読み取ったときの処理を呼び出す
        checkQrCodeAndOpen(scannedUrl);
    }

    // QRコードのURLと、WebViewのバージョンを判定する関数
    private void checkQrCodeAndOpen(String url) {
        // 1. スマホの WebView のバージョンを調べる
        int webViewMajorVersion = getWebViewMajorVersion();

        // 2. バージョンが 130以下 のときは、アプリ内ではなくスマホのふつうのブラウザで開く
        if (webViewMajorVersion > 0 && webViewMajorVersion <= 130) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return;
        }

        // 3. 読み取ったURLがサイゼリヤの正しい注文用URLかチェックする
        if ("https://ioes.saizeriya.co.jp".equals(url)) {
            // 正しいURLなら、注文画面（OrderActivity）へ進む
            Intent intent = new Intent(MainActivity.this, OrderActivity.class);
            intent.putExtra("ORDER_URL", url);
            startActivity(intent);
        } else {
            // 違うURLなら、エラーメッセージを表示して次の画面には進まない
            tvErrorMessage.setText("このコードは注文用QRではありませんもう一度正しいコードを読み取ってください");
            tvErrorMessage.setVisibility(View.VISIBLE);
        }
    }

    // WebViewのバージョン（数字）を自動で調べる補助関数
    private int getWebViewMajorVersion() {
        try {
            PackageInfo packageInfo = WebViewCompat.getCurrentWebViewPackage(this);
            if (packageInfo != null && packageInfo.versionName != null) {
                // バージョン名（例: "131.0.6778.39"）の最初の数字（131など）を取り出す
                String versionName = packageInfo.versionName;
                String majorVersionStr = versionName.split("\\.")[0];
                return Integer.parseInt(majorVersionStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // バージョンが取得できないときは0を返す
    }
}