package sgt.hansung.com.suh_gongteo;

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
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by MinJeong on 2015-10-29.
 */
public class SignUp extends AppCompatActivity {

    EditText etUserId, etUserPwd, etUserPwdConfirm, etUserEmail;
    ImageView join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        setTitle("회원가입");
        etUserId = (EditText)findViewById(R.id.userId);
        etUserPwd = (EditText)findViewById(R.id.userPwd);
        etUserPwdConfirm = (EditText)findViewById(R.id.userPwdConfirm);
        etUserEmail = (EditText)findViewById(R.id.userEmail);

        join = (ImageView)findViewById(R.id.join);
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

    public void signUp(){
        final user signInUser= new user();
        signInUser.userId = etUserId.getText().toString();
        signInUser.userPwd = etUserPwd.getText().toString();
        signInUser.userPwdConfirm = etUserPwdConfirm.getText().toString();
        signInUser.userEmail = etUserEmail.getText().toString();

        if(signInUser.userPwd.equals(signInUser.userPwdConfirm)){
            ParseUser user = new ParseUser();
            user.logOut();
            user.setUsername(signInUser.userId);
            user.setPassword(signInUser.userPwd);
            user.setEmail(signInUser.userEmail);
            // 전화번호 추가
            // user.put("phone", "650-253-0000");
            if(signInUser.userEmail.equals("")){
                Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            }
            user.signUpInBackground(new SignUpCallback() {
                public void done(com.parse.ParseException e) {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        ParseUser.logInInBackground(signInUser.userId, signInUser.userPwd, new LogInCallback() {
                            public void done(ParseUser user, com.parse.ParseException e) {
                                if (user != null) {
                                    // Hooray! The user is logged in.
                                    Toast.makeText(getApplicationContext(),"회원가입 성공, 로그인 성공",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SignUp.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Signup failed. Look at the ParseException to see what happened.
                                    Toast.makeText(getApplicationContext(),"회원가입 성공, 로그인 실패",Toast.LENGTH_SHORT).show();
                                    Log.e("에러 메시지", e.toString());
                                }
                            }
                        });
                    } else {
                        String errorMsg = e.toString();
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Log.e("에러 메시지",errorMsg);
                        if(errorMsg.equals("com.parse.ParseException: java.lang.IllegalArgumentException: Username cannot be missing or blank")){
                            Toast.makeText(getApplicationContext(),"아이디를 입력하세요.",Toast.LENGTH_LONG).show();
                        }else if(errorMsg.equals("com.parse.ParseException: java.lang.IllegalArgumentException: Password cannot be missing or blank")){
                            Toast.makeText(getApplicationContext(),"비밀번호를 입력하세요.",Toast.LENGTH_LONG).show();
                        }else if(errorMsg.equals("com.parse.ParseRequest$ParseRequestException: invalid email address")){
                            Toast.makeText(getApplicationContext(),"이메일 형식을 확인해주세요.",Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),"비밀번호를 확인해 주세요.",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    public void btn_join(View v) {
        signUp();
    }
}