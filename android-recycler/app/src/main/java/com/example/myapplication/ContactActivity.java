package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise here");

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.qwerty)
                .setDescription("About Us")
                .addItem(new Element().setTitle("Version 1.0"))
                .addItem(adsElement)
                .addGroup("Connect with us")
                .addEmail("hamburg.icgc@gmail.com")
                .addWebsite("https://www.centralgospel.com/")
                .addFacebook("idontknow")
                .addTwitter("idontknow")
                .addYoutube("idontknow")
                .addInstagram("idontknow")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }

    private Element createCopyright() {
        Element copyright = new Element();
        final String copyrightString  = String.format("Copyright %d by ICGC", Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        copyright.setIconDrawable(R.drawable.icgc_logo);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View view) {
                Toast.makeText(ContactActivity.this,copyrightString , Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;

    }
}
