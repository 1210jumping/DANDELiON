package com.example.myeducationapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.myeducationapp.Global;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.login.LoginActivity;
//terms activity class
//@author u6706220 Zhuxuan Yan
public class TermOfUseActivity extends AppCompatActivity {

    private Activity fromActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_of_use);
        Activity fromActivity = Global.getActivityFromIntent(getIntent());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView privacyTextView = findViewById(R.id.textView2);
        privacyTextView.setText(Html.fromHtml(getString(R.string.Terms_of_use)));
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