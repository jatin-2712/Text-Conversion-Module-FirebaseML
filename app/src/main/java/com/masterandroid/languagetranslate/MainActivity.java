package com.masterandroid.languagetranslate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    TextView tvTranslateLanguage;
    Button btnTranslate;
    EditText etOriginalText;
    String originaltext =  "";
    Translator englishHindiTranslator;
    SweetAlertDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etOriginalText = findViewById(R.id.etOriginalText);
        btnTranslate = findViewById(R.id.btnTranslateNow);
        tvTranslateLanguage = findViewById(R.id.tvTranslateLanguage);

       setUpProgressDailog();
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originaltext = etOriginalText.getText().toString();
                prepareModel();
            }
        });
    }

    private void setUpProgressDailog() {
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
    }

    private void prepareModel() {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.HINDI)
                .build();
        englishHindiTranslator = Translation.getClient(options);

        pDialog.setTitleText("Translate Model Downloading....");
        pDialog.show();
        englishHindiTranslator.downloadModelIfNeeded().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                pDialog.dismissWithAnimation();
                translatelanguage();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void translatelanguage() {
        pDialog.setTitleText("Language Converting....");
        pDialog.show();
        englishHindiTranslator.translate(originaltext).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText(s);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismissWithAnimation();
                tvTranslateLanguage.setText("Error"+e.getMessage());
            }
        });
    }
}