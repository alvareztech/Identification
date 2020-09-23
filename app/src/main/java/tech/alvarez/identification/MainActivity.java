package tech.alvarez.identification;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "Identification";

    private EditText countryEditText;
    private EditText phoneEditText;
    private EditText codeEditText;
    private Button getCodeButton;
    private Button loginButton;
    private Button logoutButton;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryEditText = findViewById(R.id.countryEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        codeEditText = findViewById(R.id.codeEditText);
        getCodeButton = findViewById(R.id.getCodeButton);
        loginButton = findViewById(R.id.loginButton);
        logoutButton = findViewById(R.id.logoutButton);
        messageTextView = findViewById(R.id.messageTextView);


        getCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = countryEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                getCode(country, phone);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country = countryEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String code = codeEditText.getText().toString();
                login(country, phone, code);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        checkUser();
    }

    private void logout() {
        AGConnectAuth.getInstance().signOut();

        checkUser();
    }

    private void checkUser() {
        AGConnectUser user = AGConnectAuth.getInstance().getCurrentUser();
        if (user != null) {
            messageTextView.setText("Authenticated: " + user.getPhone());
            getCodeButton.setEnabled(false);
            loginButton.setEnabled(false);
            logoutButton.setEnabled(true);
        } else {
            messageTextView.setText("Not authenticated.");
            getCodeButton.setEnabled(true);
            loginButton.setEnabled(true);
            logoutButton.setEnabled(false);
        }
    }

    private void getCode(String country, String phone) {
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30)
                .build();

        PhoneAuthProvider.verifyPhoneCode(country, phone, settings, new VerifyCodeSettings.OnVerifyCodeCallBack() {
            @Override
            public void onVerifySuccess(String s, String s1) {
                Log.d(TAG, "CODE onSuccess: " + s);
                Toast.makeText(MainActivity.this, "onSuccess: " + s + "|" + s1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerifyFailure(Exception e) {
                Log.e(TAG, "CODE onFailure", e);
                Toast.makeText(MainActivity.this, "onFailure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String country, String phone, String code) {
        AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(country, phone, null, code);
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        Log.d(TAG, "LOGIN onSuccess: " + signInResult.getUser().getPhone());
                        Toast.makeText(MainActivity.this, "LOGIN onSuccess: " + signInResult.getUser().getPhone(), Toast.LENGTH_SHORT).show();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "LOGIN onFailure", e);
                        Toast.makeText(MainActivity.this, "onFailure: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        checkUser();
                    }
                });
    }
}