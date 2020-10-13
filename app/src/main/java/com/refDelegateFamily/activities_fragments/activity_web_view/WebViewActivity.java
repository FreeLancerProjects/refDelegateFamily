package com.refDelegateFamily.activities_fragments.activity_web_view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.refDelegateFamily.R;
import com.refDelegateFamily.databinding.ActivityWebViewBinding;
import com.refDelegateFamily.language.Language_Helper;
import com.refDelegateFamily.models.PackageResponse;

public class WebViewActivity extends AppCompatActivity {
    private ActivityWebViewBinding binding;
    private PackageResponse response;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Language_Helper.getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        response = (PackageResponse) intent.getSerializableExtra("data");
    }

    private void initView() {
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.getSettings().setSupportZoom(false);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.loadUrl(response.getUrl());
        binding.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (url.equals(response.getSucces_url())){
                    setResult(RESULT_OK);
                    finish();
                }else if (url.equals(response.getCanceled_url())){
                    setResult(RESULT_CANCELED);
                    finish();
                }else if (url.equals(response.getDeclined_url())){

                }
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                binding.progBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
    }

}