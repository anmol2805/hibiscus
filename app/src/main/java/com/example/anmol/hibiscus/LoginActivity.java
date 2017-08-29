package com.example.anmol.hibiscus;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.anmol.hibiscus.Model.Students;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button  btnLogin, btnReset;
    private Button googleSignIn;
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    SessionManagement session;
    private static final String TAG = "Login";
    String email,sid,password;
    JSONObject object;
    String uid,pwd;
    String url = "http://139.59.23.157/api/hibi/login_test";
    private static long back_pressed;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        session = new SessionManagement(getApplicationContext());
        setTitle("Login");
        object = new JSONObject();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        uid = getIntent().getStringExtra("uid");
        pwd = getIntent().getStringExtra("pwd");

        if (auth.getCurrentUser() != null) {
            if(auth.getCurrentUser().isEmailVerified()){
                session.createLoginSession(auth.getCurrentUser().getEmail());
                Intent intent = new Intent(LoginActivity.this, SplashActivity.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_in_up);

            }
            else {
                auth.getCurrentUser().sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // email sent
                                    // after email is sent just logout the user and finish this activity
                                    Toast.makeText(LoginActivity.this,"Your Email is not verified",Toast.LENGTH_SHORT).show();
                                    Toast.makeText(LoginActivity.this,"We have sent you a verification email at " +auth.getCurrentUser().getEmail()+ ",verify and login again",Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this,"Failed to send Verification link",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        }

        // set the view now
        setContentView(R.layout.activity_login);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);



        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                overridePendingTransition(R.anim.slide_in_up,R.anim.still);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                overridePendingTransition(R.anim.slide_out_right,R.anim.still);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sid = inputEmail.getText().toString();
                email = sid + "@iiit-bh.ac.in";
                password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                try {
                    object.put("uid",sid);
                    object.put("pwd",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("result").equals("success")){
                                auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                // If sign in fails, display a message to the user. If sign in succeeds
                                                // the auth state listener will be notified and logic to handle the
                                                // signed in user can be handled in the listener.
                                                progressBar.setVisibility(View.INVISIBLE);
                                                if (!task.isSuccessful()) {
                                                    // there was an error
                                                    if (password.length() < 6) {
                                                        inputPassword.setError(getString(R.string.minimum_password));
                                                    } else {
                                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    if (auth.getCurrentUser().isEmailVerified()){
                                                        if(FirebaseDatabase.getInstance().getReference().getRoot().child("Students").child(auth.getCurrentUser().getUid())!=null){
                                                            FirebaseDatabase.getInstance().getReference().getRoot().child("Students").child(auth.getCurrentUser().getUid()).removeValue();
                                                        }
                                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().getRoot().child("Students").child(auth.getCurrentUser().getUid()).child("hibiscus");
                                                        Students students = new Students(sid,password,true);
                                                        ref.setValue(students);
                                                        FirebaseMessaging.getInstance().subscribeToTopic("IIITstudents");
                                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().getRoot().child("token");
                                                        db.child("token").setValue(FirebaseInstanceId.getInstance().getToken());
//                                                        String url = "https://iid.googleapis.com/iid/v1/"+FirebaseInstanceId.getInstance().getToken()+"/rel/topics/IIITstudents";
//                                                        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,null ,new Response.Listener<JSONObject>() {
//                                                            @Override
//                                                            public void onResponse(JSONObject response) {
//                                                                Toast.makeText(LoginActivity.this,FirebaseInstanceId.getInstance().getToken(),Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }, new Response.ErrorListener() {
//                                                            @Override
//                                                            public void onErrorResponse(VolleyError error) {
//                                                                Toast.makeText(LoginActivity.this,"errort",Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        });
//                                                        Mysingleton.getInstance(LoginActivity.this).addToRequestqueue(stringRequest);
                                                        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);

                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                        startActivity(intent);
                                                        finish();
                                                        overridePendingTransition(R.anim.still,R.anim.slide_in_up);
                                                    }
                                                    else {
                                                        auth.getCurrentUser().sendEmailVerification()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            // email sent
                                                                            // after email is sent just logout the user and finish this activity
                                                                            Toast.makeText(LoginActivity.this,"Your Email is not verified",Toast.LENGTH_SHORT).show();
                                                                            Toast.makeText(LoginActivity.this,"We have sent you a verification email,verify and login again",Toast.LENGTH_LONG).show();
                                                                        }
                                                                        else
                                                                        {
                                                                            Toast.makeText(LoginActivity.this,"Failed to send Verification link",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    }


                                                }
                                            }
                                        });
                            }
                            else{
                                Toast.makeText(LoginActivity.this,"Please Enter Correct password",Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this,"Connection Error...Please try again!!!",Toast.LENGTH_SHORT).show();

                    }
                });
                Mysingleton.getInstance(LoginActivity.this).addToRequestqueue(jsonObjectRequest);

                //authenticate user

            }
        });
        GoogleLogin();
    }
    private void GoogleLogin(){

        googleSignIn = (Button) findViewById(R.id.google_sign_in);

        googleSignIn.setVisibility(View.INVISIBLE);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // [START_EXCLUDE]
                // [END_EXCLUDE]
            }
        }
        else {
            //callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]




        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }



                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if(user!=null)       //Someone is logged in
        {
            Log.i(TAG,"Login was successful in Firebase");
            Log.i(TAG,"UID "+ user.getUid());

            Intent intent = new Intent(this,SplashActivity.class);


            startActivity(intent);

        }
        else
        {
            Log.i(TAG,"No user is logged in ");
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onBackPressed() {
        if(back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.still,R.anim.slide_out_down);
        }else {
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}

