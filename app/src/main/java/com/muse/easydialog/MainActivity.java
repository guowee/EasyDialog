package com.muse.easydialog;

import android.content.Context;
import android.media.MediaCodec;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.muse.easy.dialog.EasyDialog;

public class MainActivity extends AppCompatActivity {

    Context mContext;
    AppCompatButton button;
    static String title = "Tips";
    static String message = "Talk is cheap. Show me the code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        button = findViewById(R.id.show_msg_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyDialog.Builder builder = new EasyDialog.Builder(mContext);
                builder.title(title).content(message).positiveText("OK").negativeText("CANCEL");
                builder.create().show();
            }
        });

    }
}
