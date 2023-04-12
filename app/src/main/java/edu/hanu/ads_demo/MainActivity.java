package edu.hanu.ads_demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


public class MainActivity extends AppCompatActivity {

    private AdView bannerAd;
    private View nativeAd;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;

    private final String TAG = "MainActivity";

    private Button bannerAdBtn, interstitialAdBtn, nativeAdBtn, rewardedAdBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();

//        Banner Ad
        bannerAd = findViewById(R.id.bannerAd);
        bannerAdBtn = findViewById(R.id.bannerAdBtn);

        bannerAd.loadAd(adRequest);
        bannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                bannerAd.setVisibility(View.GONE);
            }
        });

        bannerAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bannerAd.setVisibility(View.VISIBLE);
            }
        });

//        Interstitial Ad
        interstitialAdBtn = findViewById(R.id.interstitialAdBtn);

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd mInterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd = mInterstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        interstitialAd = null;
                    }
                });

        interstitialAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interstitialAd != null) {
                    interstitialAd.show(MainActivity.this);
                } else {
                    Log.d("TAG", "The interstitial ad wasn't ready yet.");
                }
            }
        });

        //native Ad
        MobileAds.initialize(this);
        AdLoader adLoader = new AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
                .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                    @Override
                    public void onNativeAdLoaded(NativeAd nativeAd) {
                        ColorDrawable background = new ColorDrawable();
                        NativeTemplateStyle styles = new
                                NativeTemplateStyle.Builder().withMainBackgroundColor(background).build();
                        TemplateView template = findViewById(R.id.my_template);
                        template.setStyles(styles);
                        template.setNativeAd(nativeAd);
                    }
                })
                .build();

        adLoader.loadAd(new AdRequest.Builder().build());

        nativeAdBtn = findViewById(R.id.nativeAdBtn);
        nativeAd = findViewById(R.id.nativeAd);

        nativeAdBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(nativeAd.getVisibility() == View.GONE) {
                    nativeAd.setVisibility(View.VISIBLE);
                } else nativeAd.setVisibility(View.GONE);
            }
        });

        //rewarded Ad

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.toString());
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                        Log.d(TAG, "Ad was loaded.");
                    }
                });

        rewardedAdBtn = findViewById(R.id.rewardedAdBtn);

        rewardedAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Activity activityContext = MainActivity.this;
                   if(rewardedAd != null){
                    rewardedAd.show(activityContext, null);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }
            }
        });

    }
}