package com.example.myeducationapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
//PrivacyPolicyActivity class
//author u6706220 Zhuxuan Yan
public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        Activity fromActivity = Global.getActivityFromIntent(getIntent());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView privacyTextView = findViewById(R.id.textView3);
        privacyTextView.setText(Html.fromHtml(getString(R.string.privacy_policy)));
        privacyTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
