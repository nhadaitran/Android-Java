package vn.edu.stu.library.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import vn.edu.stu.library.R;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnExit;
    EditText txtUsername, txtPassword;
    String admin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        addControls();
        addEvents();
    }

    private void addControls() {
        btnLogin = findViewById(R.id.btnLogin);
        btnExit = findViewById(R.id.btnExit);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = txtUsername.getText().toString();
                String pass = txtPassword.getText().toString();
                if (uname.equals(admin) || pass.equals(admin)) {
                    txtUsername.setText("");
                    txtPassword.setText("");
                    Intent intent = new Intent(
                            LoginActivity.this,
                            MainActivity.class
                    );
                    startActivity(intent);
                } else {
                    Toast.makeText(
                            LoginActivity.this,
                            "Sai thông tin tài khoản hoặc mật khẩu!",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAndRemoveTask();
            }
        });
    }
}