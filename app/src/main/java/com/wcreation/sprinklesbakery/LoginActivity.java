package com.wcreation.sprinklesbakery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView txtMain,txtSignUp;
    EditText edtUserEmail, edtUserPass;
    AppCompatButton btnLogin;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    RequestQueue mrequestQueue;
    StringRequest mstringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String userName = sharedPreferences.getString("username", "");

        if (userName.length()!=0){
            startActivity(new Intent(LoginActivity.this, UserHome.class));
        }


        txtMain = findViewById(R.id.fastdel);
        txtSignUp = findViewById(R.id.txtSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        edtUserEmail = findViewById(R.id.editUserName);
        edtUserPass = findViewById(R.id.editUserPass);




        String mainText = "Good To See You \nBack";

        SpannableString sP = new SpannableString(mainText);

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);

        sP.setSpan(fcsRed, 17, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        txtMain.setText(sP);

        txtSignUp.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, UserRegistration.class);
            startActivity(i);
        });

        btnLogin.setOnClickListener(view -> {
//            Intent i = new Intent(LoginActivity.this, UserHome.class);
//            startActivity(i);

            if (edtUserEmail.getText().length()==0 || edtUserPass.getText().length()==0){
                Toast.makeText(this, "All Fields Are Mandatory", Toast.LENGTH_SHORT).show();
            }else{
                signIn(edtUserEmail.getText().toString(), edtUserPass.getText().toString());
            }

        });
    }

    private void signIn(final String email, final String password){
        setProgressDialog();

        mrequestQueue = Volley.newRequestQueue(LoginActivity.this);
            mstringRequest = new StringRequest(Request.Method.POST,
                    getBaseUrl(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        String userName = jsonObject.getString("userName");
                        int userID = jsonObject.getInt("userID");
                        String userTel = jsonObject.getString("userTel");
                        String success = jsonObject.getString("success");
                        String message = jsonObject.getString("message");

                        if (success.equals("1")){
                            progressDialog.dismiss();
                            finish();
                            SharedPreferences.Editor spEditor = sharedPreferences.edit();
                            spEditor.putString("username", userName);
                            spEditor.putInt("userid", userID);
                            spEditor.putString("usertel", userTel);
                            spEditor.commit();

                            startActivity(new Intent(LoginActivity.this, UserHome.class));
                        }else if(success.equals("2")){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }else if(success.equals("0")){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        }

                    }
                    catch (JSONException e){
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("useremail",email);
                    params.put("password", password);
                    return params;
                }
            };

            mstringRequest.setShouldCache(false);
            mrequestQueue.add(mstringRequest);
    }

    private String getBaseUrl(){
        return "https://dineshwayaman.com/final/user-login.php";
    }


    public void setProgressDialog(){
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

}