package com.viniciusam.filepicker.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.viniciusam.filepicker.FilePickerActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_FILE = 1;
    private static final int REQUEST_PICK_FOLDER = 2;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);

        Button pickFileButton = (Button) findViewById(R.id.pick_file);
        Button pickFolderButton = (Button) findViewById(R.id.pick_folder);

        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.EXTRA_PICKER_TYPE, FilePickerActivity.PICK_FILE);
                startActivityForResult(intent, REQUEST_PICK_FILE);
            }
        });

        pickFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FilePickerActivity.class);
                intent.putExtra(FilePickerActivity.EXTRA_PICKER_TYPE, FilePickerActivity.PICK_FOLDER);
                startActivityForResult(intent, REQUEST_PICK_FOLDER);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_PICK_FILE == requestCode && resultCode == RESULT_OK) {
            File pickedFile = new File(data.getStringExtra(FilePickerActivity.RESULT_PATH));
            mTextView.setText(pickedFile.getPath());
            return;
        }

        if (REQUEST_PICK_FOLDER == requestCode && resultCode == RESULT_OK) {
            File pickedFile = new File(data.getStringExtra(FilePickerActivity.RESULT_PATH));
            mTextView.setText(pickedFile.getPath());
            return;
        }
    }

}
