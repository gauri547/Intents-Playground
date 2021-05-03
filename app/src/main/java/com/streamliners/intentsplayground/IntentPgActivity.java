package com.streamliners.intentsplayground;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.streamliners.intentsplayground.databinding.ActivityIntentPlaygroundBinding;

public class IntentPgActivity extends AppCompatActivity {
    private static final int REQUEST_COUNT = 100;
    ActivityIntentPlaygroundBinding b;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupLayout();
        setupExplicitIntent();
        setupImplicitIntent();
        sendDataToMainActivity();
    }
    private void sendDataToMainActivity(){
        b.btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sendData = b.sendData.getText().toString().trim();
                if(sendData.isEmpty()){
                    b.sendData.setError("Enter data");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Const.INITIAL_DATA,Integer.parseInt(sendData));
                bundle.putInt(Const.MIN_VALUE,0);
                bundle.putInt(Const.MAX_VALUE,100);

                Intent intent = new Intent(IntentPgActivity.this,MainActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_COUNT);

            }
        });
    }
    // Initialize Layout
    private void setupLayout(){
        b = ActivityIntentPlaygroundBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        setTitle("Intents playground");
    }
    // Explicit Intent
    private void setupExplicitIntent(){
        b.sendExplicitIntent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IntentPgActivity.this,MainActivity.class));
            }
        });
    }

    // Implicit Intent
    private void setupImplicitIntent(){
        b.sendExplicitIntent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String sendData = b.sendData.getText().toString().trim();
                if (sendData.isEmpty()) {
                    b.sendData.setError("Enter data");
                    return;
                }
                int id = b.radioGroup.getCheckedRadioButtonId();
                if (id == R.id.open_webPage) {
                    openWebPage(str);
                } else if (id == R.id.dial_no) {
                    openDialer(str);
                } else if (id == R.id.share_text) {
                    openShareMenu(str);
                } else {
                    Toast.makeText(IntentPgActivity.this, "Select a option", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void openShareMenu(String text){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT,text);
        startActivity(Intent.createChooser(intent,"Share text via"));
        hideError();
    }
    private void openWebPage(String url){
        if(!url.matches("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$")){
            b.data.setError("Please enter valid url");
            return;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        hideError();
    }

    //Utils
    private void hideError(){
        b.data.setError(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_COUNT && resultCode == RESULT_OK){
            b.finalData.setText("The final count value is" + data.getIntExtra(Const.FINAL_DATA,0));
            b.finalData.setVisibility(View.VISIBLE);
        }
    }
}

