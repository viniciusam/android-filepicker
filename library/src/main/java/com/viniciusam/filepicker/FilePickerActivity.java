package com.viniciusam.filepicker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FilePickerActivity extends AppCompatActivity
        implements FileListFragment.FileListHandlers {

    public static final String EXTRA_ROOT_FOLDER = "root_folder";
    public static final String EXTRA_PICKER_TYPE = "picker_type";

    public static final String RESULT_PATH = "result_path";

    public static final String TAG_FILELIST = "filelist";

    public static final int PICK_FILE = 1;
    public static final int PICK_FOLDER = 2;

    private int mPickerType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filepicker);

        Bundle args = getIntent().getExtras();
        if (args.containsKey(EXTRA_PICKER_TYPE)) {
            mPickerType = args.getInt(EXTRA_PICKER_TYPE);
        } else {
            mPickerType = PICK_FILE;
        }

        String rootFolder = getIntent().getStringExtra(EXTRA_ROOT_FOLDER);
        if (rootFolder == null) {
            rootFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
        }

        if (savedInstanceState == null) {
            FileListFragment fileListFragment =
                    FileListFragment.newInstance(rootFolder, mPickerType == PICK_FOLDER, false);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content, fileListFragment, TAG_FILELIST)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_picker, menu);

        if (mPickerType != PICK_FOLDER) {
            MenuItem pickItem = menu.findItem(R.id.pick);
            pickItem.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (R.id.pick == item.getItemId()) {
            FileListFragment fileListFragment =
                    (FileListFragment) getSupportFragmentManager().findFragmentByTag(TAG_FILELIST);
            String currentFolder = fileListFragment.getCurrentFolder();

            Intent intent = new Intent();
            intent.putExtra(RESULT_PATH, currentFolder);
            setResult(RESULT_OK, intent);
            finish();
            return true;
        }

        if (R.id.cancel == item.getItemId()) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void navigateTo(String path, boolean disableFiles, boolean showParentLink) {
        FileListFragment fileListFragment =
                FileListFragment.newInstance(path, disableFiles, showParentLink);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                .replace(R.id.content, fileListFragment, TAG_FILELIST)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onFileSelected(FileInfo fileInfo) {
        if (fileInfo.isParentLink()) {
            getSupportFragmentManager().popBackStack();
        } else if (fileInfo.isDirectory()) {
            navigateTo(fileInfo.getFilePath(), mPickerType == PICK_FOLDER, true);
        } else {
            Intent intent = new Intent();
            intent.putExtra(RESULT_PATH, fileInfo.getFilePath());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onFolderListed(String name) {
        setTitle(name);
    }
}
