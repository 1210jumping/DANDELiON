package com.example.myeducationapp.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.databinding.ActivityRegisterBinding;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.HashMap;
//@author: all members
//RegisterActivity class
public class RegisterActivity extends AppCompatActivity {
    private static final String tag = "RegisterActivity";
    private String uid;
    private String email;
    private ActivityRegisterBinding RegisterBinding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        Global.update();
        setContentView(R.layout.activity_register);
        RegisterBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(RegisterBinding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TermsCheck
        //author u6706220 Zhuxuan Yan and u7518626 Pin-Shen Chang
        CheckBox checkBox = RegisterBinding.TermsCheck;
        String text = checkBox.getText().toString();
        SpannableString spannableString = new SpannableString(text);
        int startIndex = text.indexOf("Terms of Use");
        int endIndex = startIndex + "Terms of Use".length();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, TermOfUseActivity.class);
                startActivity(intent);
                //finish();
            }
        };
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBox.setText(spannableString);
        checkBox.setMovementMethod(LinkMovementMethod.getInstance());


        //PrivacyCheck
        //author u6706220 Zhuxuan Yan and u7518626 Pin-Shen Chang
        CheckBox checkBoxPP = RegisterBinding.PrivacyCheck;
        String textPP = checkBoxPP.getText().toString();
        SpannableString spannableStringPP = new SpannableString(textPP);
        int startIndexPP = textPP.indexOf("Privacy Policy");
        int endIndexPP = startIndexPP + "Privacy Policy".length();
        ClickableSpan clickableSpanPP = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(RegisterActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                //finish();
            }
        };
        spannableStringPP.setSpan(clickableSpanPP, startIndexPP, endIndexPP, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        checkBoxPP.setText(spannableStringPP);
        checkBoxPP.setMovementMethod(LinkMovementMethod.getInstance());


        // final Button
        final Button JoinNowButton = RegisterBinding.joinnow;

        final CheckBox YearCheck = RegisterBinding.TermsCheck;
        final CheckBox PrivacyCheck = RegisterBinding.PrivacyCheck;

        final EditText user_name = RegisterBinding.name;
        final EditText email = RegisterBinding.email;
        final EditText email_con = RegisterBinding.email2;
        final EditText pwd = RegisterBinding.password2;
        final EditText pwd_con = RegisterBinding.password3;

        final ProgressBar loadingProgressBar = RegisterBinding.loading;

        JoinNowButton.setEnabled(false);

        //author u7560434 Ethan Yifan Zhu
        //button listener
        JoinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // set up loding circle
                loadingProgressBar.setVisibility(View.VISIBLE);

                String u_name = user_name.getText().toString();
                String u_email = email.getText().toString();
                String u_email_con = email_con.getText().toString();
                String u_pwd = pwd.getText().toString();
                String u_pwd_con = pwd_con.getText().toString();

                ArrayList<String> arrayList = new ArrayList<>();
                for(User u:Global.userList)
                    arrayList.add(u.getEmail());

                // case: user name is empty
                if (u_name == null || u_name.length() == 0) {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.U_empty)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
                } else if (u_email == null || u_email.length() == 0 || u_email_con == null || u_email_con.length() == 0) {
                    // case: email is empty
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.n_email)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
                } else if (!u_email.equals(u_email_con)) {
                    // case: two email inputs are not consistent
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.c_email)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
                } else if (u_pwd == null || u_pwd.length() <= 5 || u_pwd_con == null || u_pwd_con.length() <= 5) {
                    // case: length of password is less then 5
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.n_pwd)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
                } else if (!u_pwd.equals(u_pwd_con)) {
                    // case: two password inputs are not consistent
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.c_pwd)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
                } else if (arrayList.contains(u_email)) {
                    // case: email has been registered
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.xx_email)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
                } else {
                    // correct input
                    createUser(u_email, u_pwd, u_name);
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle(R.string.notice)
                            .setMessage(R.string.Register_successful)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(RegisterActivity.this, NavigationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();

                }
            }
        });

        //add listener
        //author u6706220 Zhuxuan Yan
        YearCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (YearCheck.isChecked() && PrivacyCheck.isChecked()) {
                    JoinNowButton.setEnabled(true);
                } else {
                    JoinNowButton.setEnabled(false);
                }
            }
        });

        PrivacyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (YearCheck.isChecked() && PrivacyCheck.isChecked()) {
                    JoinNowButton.setEnabled(true);
                } else {
                    JoinNowButton.setEnabled(false);
                }
            }
        });

        // author u7560434 Ethan Yifan Zhu
        // Check input validation
        TextWatcher afterTextChangedListener0 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
                if (user_name == null || user_name.getText().toString().length() == 0)
                    user_name.setError("Not a valid email");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (user_name == null || user_name.getText().toString().length() == 0)
                    user_name.setError("Not a valid email");
            }
        };
        user_name.addTextChangedListener(afterTextChangedListener0);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isUserNameValid(email.getText().toString()))
                    email.setError("Not a valid email");
                if (!isUserNameValid(email_con.getText().toString()))
                    email_con.setError("Not a valid email");

            }
        };
        email.addTextChangedListener(afterTextChangedListener);
        email_con.addTextChangedListener(afterTextChangedListener);

        TextWatcher afterTextChangedListener2 = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (pwd.getText().toString().length() < 5)
                    pwd.setError("Password length should be greater than 5");
                if (pwd_con.getText().toString().length() < 5)
                    pwd_con.setError("Password length should be greater than 5");

            }
        };
        pwd.addTextChangedListener(afterTextChangedListener2);
        pwd_con.addTextChangedListener(afterTextChangedListener2);
    }

    // A placeholder username validation check
    //author u6706220 Zhuxuan Yan
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // Check if user is signed in (non-null) and update UI accordingly.
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d("currentUser", currentUser.toString());
        }
    }

    //add new users
    static boolean flag = false;
    private boolean createUser(String mail, String pwd, String name) {
        mAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser fuser = mAuth.getCurrentUser();
                    uid = fuser.getUid();
                    email = fuser.getEmail();
                    HashMap<String, Object> userList = new HashMap<>();
                    userList.put("uid", uid);
                    userList.put("email", email);
                    userList.put("name", name);
                    userList.put("sex", 1);
                    userList.put("image", "");
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("userList").document(uid).set(userList)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegisterActivity.this, "create user with email complete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                    flag = true;
                                    User user = new User(uid, name, email, 1, "", "");
                                    Global.currentUser = MyUser.getInstance(user);
                                    Global.firebaseUser=fuser;
                                    Global.update();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    flag = false;
                                    Log.w(tag, "Error adding document", e);
                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return flag;
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
