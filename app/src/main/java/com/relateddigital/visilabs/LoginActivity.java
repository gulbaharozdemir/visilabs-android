package com.relateddigital.visilabs;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.visilabs.Visilabs;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText exvisitorIdEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btn_login);

        Button btnLogout = findViewById(R.id.btn_logout);

         exvisitorIdEt = findViewById(R.id.tv_exvisitor_id);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.e("token", "getInstanceId failed", task.getException());

                                    return;
                                }

                                String token = task.getResult().getToken();

                                HashMap<String, String> parameters = new HashMap<>();
                                parameters.put("OM.exVisitorID", exvisitorIdEt.getText().toString());
                                parameters.put("OM.sys.TokenID", token);
                                parameters.put("OM.sys.AppID", "visilabs-android-sdk"); //
                                Visilabs.CallAPI().customEvent("android-visilab", parameters, LoginActivity.this);

                                Toast.makeText(getApplicationContext(), "Login", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();

                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("OM.sys.AppID", "visilabs-android-sdk");
                Visilabs.CallAPI().customEvent("Logout", parameters);

                Toast.makeText(getApplicationContext(), "Logout", Toast.LENGTH_LONG).show();

            }
        });
    }
}