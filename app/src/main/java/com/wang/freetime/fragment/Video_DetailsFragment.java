package com.wang.freetime.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wang.freetime.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Video_DetailsFragment extends Fragment {

    private ImageButton mbar;
    private WebView mWebView;
    private Context context;
    private TextView mTitle;
    private Activity_inf_fragmnet inf;
    public Video_DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
        if (context instanceof Activity_inf_fragmnet ){
            inf= (Activity_inf_fragmnet) context;
        }
    }

    public interface Activity_inf_fragmnet{
        void goback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_video__details, container, false);
        /**
         * Created by wang on 2017.6.18
         * 初始化组件
         */
        mTitle= (TextView) view.findViewById(R.id.web_title);
        mbar= (ImageButton) view.findViewById(R.id.go_back);
        mWebView= (WebView) view.findViewById(R.id.web_video);
        mbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoBack()){
                    mWebView.goBack();
                }else {
                    inf.goback();
                }
            }
        });

        web_content(getArguments().getString("url"));
        // Inflate the layout for this fragment
        return view;
    }

    private void web_content(String url){
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new MyWebclient());
        mWebView.setWebChromeClient(new MyChrome());
        WebSettings webtool=mWebView.getSettings();
        webtool.setJavaScriptEnabled(true);
        webtool.setLoadWithOverviewMode(true);
        webtool.setUseWideViewPort(true);
        webtool.setBuiltInZoomControls(false);
        webtool.setSupportZoom(true);
        webtool.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }

    class MyWebclient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(String.valueOf(request.getUrl()));
            return super.shouldOverrideUrlLoading(view, request);
        }
    }
    class MyChrome extends WebChromeClient{
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            mTitle.setText(title);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.reload();
    }
}
