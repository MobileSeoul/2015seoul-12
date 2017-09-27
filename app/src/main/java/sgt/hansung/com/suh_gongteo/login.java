package sgt.hansung.com.suh_gongteo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;


public class login extends AppCompatActivity {

    EditText etUserId, etUserPwd, etUserPwdConfirm, etUserEmail;
    ImageView logIn;


    ProgressDialog progressDialog; // ProgressDialog

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("로그인");
        etUserId = (EditText)findViewById(R.id.userId);
        etUserPwd = (EditText)findViewById(R.id.userPwd);
        etUserPwdConfirm = (EditText)findViewById(R.id.userPwdConfirm);
        etUserEmail = (EditText)findViewById(R.id.userEmail);

        logIn = (ImageView)findViewById(R.id.logIn);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.prepage) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public void btn_login(View v) {
        progressDialog = ProgressDialog.show(login.this, "로딩중입니다", "잠시만 기다려주세요", true);
        ParseUser.logInInBackground(etUserId.getText().toString()
                , etUserPwd.getText().toString(), new LogInCallback() {
            public void done(ParseUser user, ParseException e) {

                if (user != null) {
                    if (getIntent().getStringExtra("prevActivity") != null) {
                        Intent intent = new Intent(login.this, Detail.class);
                        intent.putExtra("address", getIntent().getStringExtra("prevActivity"));
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(login.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인해 주세요.", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });

    }

    public void sign_up(View v) {
        Intent intent = new Intent(login.this, SignUp.class);
        startActivity(intent);
        finish();
    }

    public void login_facebook(View view) {
        progressDialog = ProgressDialog.show(login.this, "로딩중입니다", "잠시만 기다려주세요", true);
        ParseFacebookUtils.logIn(this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user == null) {
                    Log.e("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.e("MyApp", "User signed up and logged in through Facebook!");
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("MyApp", "User logged in through Facebook!");
                    Intent intent = new Intent(login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                progressDialog.dismiss();
            }

        });
    }

    //사용자 기기에 Facebook 앱이 설치되어 있지 않은 경우 기본 대화상자 기반 인증을 하는 함수. 이 기능을 SSO(Single-Sign On)이라고 한다.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }
}