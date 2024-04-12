package com.example.myeducationapp.ui.login;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myeducationapp.DAO.UserDAO.MyUser;
import com.example.myeducationapp.DAO.UserDAO.User;
import com.example.myeducationapp.Global;
import com.example.myeducationapp.databinding.ActivityLoginBinding;
import com.example.myeducationapp.ui.navigation.NavigationActivity;
import com.example.myeducationapp.ui.register.PrivacyPolicyActivity;
import com.example.myeducationapp.R;
import com.example.myeducationapp.ui.register.RegisterActivity;
import com.example.myeducationapp.ui.register.TermOfUseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
 //@author：all members
 // login page activity
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    ProgressBar loadingProgressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Location[] location = {null};
        final String[] country = {null};
        // check location permission
        /**
         * @author u7560434 Ethan Yifan Zhu
         * Get current location, then ajust the language
         * **/
        if (checkLocationPermission()) {
            try {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Notice")
                        .setMessage("Do you want to use current location as your language?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                location[0] = getLocation();
                                country[0] = getAddress(location[0]);
                                setLanguage(country[0]);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

            }
            catch (Exception exception){
                new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.alert)
                            .setMessage(R.string.location_error)
                            .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    loadingProgressBar.setVisibility(View.GONE);
                                }
                            }).show();
            }

        }


        setContentView(R.layout.activity_login);

        Log.d("GG", "Oncreate");
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final Button RegisterButton = binding.register;
        loadingProgressBar = binding.loading;
        final TextView Terms = binding.Terms;
        final TextView Privacy = binding.Privacy;

        Log.d("create", "Oncreate");

        loginButton.setEnabled(false);
        //check log in information
        //author u7532738 Jinhan Tan
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                //finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(usernameEditText.getText().toString().equals("") || passwordEditText.getText().toString().equals(""))
                    loginButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                if(!usernameEditText.getText().toString().equals("") && !passwordEditText.getText().toString().equals(""))
                    loginButton.setEnabled(true);
                if(passwordEditText.getText().length()<=5)
                    passwordEditText.setError("Password must be >5 characters");
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        //check password
        //author u7532738 Jinhan Tan
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });


        //button listener
        // author: u6706220 Zhuxuan Yan
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        Terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TermOfUseActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgressBar.setVisibility(View.VISIBLE);
                String userName = usernameEditText.getText().toString();
                String pwd = passwordEditText.getText().toString();
                loginWithEmailAndPwd(userName, pwd);
            }
        });
    }
    public void updateUiWithUser(LoggedInUserView model) {
    }

    public void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

     /**
      * @author u7518626 Pin-Shen Chang
      * check Location Permission
      */
    private boolean checkLocationPermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
            return false;
        } else {
            return true;
        }
    }

     /**
      * @author u7518626 Pin-Shen Chang
      * on Request Permissions Result
      */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(this, R.string.x_location, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * @author U7560434 Ethan Yifan Zhu
     * Return the location by GPS
     * **/
    private Location getLocation() {
        // open location service
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // set provider to Network
        String provider = LocationManager.NETWORK_PROVIDER;
        if (provider == null) {
            return null;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        return locationManager.getLastKnownLocation(provider);
    }

    /**
     * @author u7560434 Ethan Yifan Zhu
     * accorfing to given location, get the specific address
     * **/
    private String getAddress(Location location) {
        Geocoder gc = new Geocoder(this, Locale.getDefault());
        List<String> res = null;
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String country = locationList.get(0).getCountryName().toString();
        return country;
    }

    /**
     * @author u7560434 Ethan Yifan Zhu
     * according to given country, setting the language
     * **/
    private void setLanguage(String country) {
        Configuration config = new Configuration();
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        if (country.equals("China")) {
            config.setLocale(Locale.SIMPLIFIED_CHINESE);
        } else if (country.equals("French")) {
            config.setLocale(Locale.FRENCH);
        }else if (country.equals("Japan")){
            config.setLocale(Locale.JAPANESE);
        } else if (country.equals("Taiwan")) {
            config.setLocale(Locale.TRADITIONAL_CHINESE);
        }else{
            config.setLocale(Locale.ENGLISH);
        }
        resources.updateConfiguration(config, dm);
    }

    static boolean flag = false;
     //author u7532738 Jinhan Tan
     //check email and password matched
    public boolean loginWithEmailAndPwd(String email, String pwd) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Login!", "UID:" + email);
                            FirebaseUser fUser = mAuth.getCurrentUser();
                            String uid = fUser.getUid();
                            db.collection("userList").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Map<String, Object> data = documentSnapshot.getData();
                                    String uid = data.get("uid").toString();
                                    String name = data.get("name").toString();
                                    String email = data.get("email").toString();
                                    String image = data.get("image").toString();
                                    int sex = Integer.parseInt(data.get("sex").toString());
                                    User user = new User(uid, name, email, sex, "", image);
                                    Global.currentUser = MyUser.getInstance(user);
                                    Global.firebaseUser=fUser;
                                    Global.update();
                                    Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        } else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle(R.string.alert)
                                    .setMessage(R.string.login_fail)
                                    .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            loadingProgressBar.setVisibility(View.GONE);
                                        }
                                    }).show();
                        }
                    }
                });
        return flag;
    }
}