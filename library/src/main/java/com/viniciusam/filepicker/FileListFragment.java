package com.viniciusam.filepicker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileListHandlers} interface
 * to handle interaction events.
 * Use the {@link FileListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileListFragment extends Fragment implements FileInfoAdapter.ClickHandlers {

    public static final String ARG_CURRENT_FOLDER = "base_path";
    public static final String ARG_DISABLE_FILES = "disable_files";
    public static final String ARG_SHOW_PARENT_LINK = "show_parent_link";

    private String mCurrentFolder;
    private boolean mDisableFiles;
    private boolean mShowParentLink;

    private FileInfoAdapter mAdapter;

    /**
     * Use this method to create a new instance of this Fragment.
     * @param currentFolder the showing folder.
     * @param disableFiles must show files?
     */
    public static FileListFragment newInstance(String currentFolder, boolean disableFiles, boolean showParentLink) {
        FileListFragment fragment = new FileListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CURRENT_FOLDER, currentFolder);
        args.putBoolean(ARG_DISABLE_FILES, disableFiles);
        args.putBoolean(ARG_SHOW_PARENT_LINK, showParentLink);
        fragment.setArguments(args);
        return fragment;
    }

    public FileListFragment() {
        // Required empty public constructor.
    }

    /**
     * Returns the folder this fragment is displaying.
     * @return folder's path.
     */
    public String getCurrentFolder() {
        return mCurrentFolder;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the base navigation path with the arg passed on constructor.
        Bundle args = getArguments();
        if (args != null) {
            mCurrentFolder = args.getString(ARG_CURRENT_FOLDER);
            mDisableFiles = args.getBoolean(ARG_DISABLE_FILES);
            mShowParentLink = args.getBoolean(ARG_SHOW_PARENT_LINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_filelist, container, false);

        mAdapter = new FileInfoAdapter(getContext(), mDisableFiles, this);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Loads the folder files.
        new FileListLoader().execute();
    }

    @Override
    public void onItemClick(int position) {
        if (getListener() != null) {
            FileInfo clickedItem = mAdapter.getItem(position);
            getListener().onFileSelected(clickedItem);
        }
    }

    /**
     * Returns the Activity attached to this Fragment if it is an instanceof FileListHandlers.
     * The TargetFragment is it is also an instance of FileListHandlers.
     * Or null if either are not.
     * @return the listener for this Fragment.
     */
    protected FileListHandlers getListener() {
        if (getActivity() instanceof FileListHandlers) {
            return (FileListHandlers) getActivity();
        }

        if (getTargetFragment() instanceof FileListHandlers) {
            return (FileListHandlers) getTargetFragment();
        }

        return null;
    }

    /**
     * Async loads the directory structure.
     */
    private class FileListLoader extends AsyncTask<Void, Void, List<FileInfo>> {

        @Override
        protected List<FileInfo> doInBackground(Void... params) {
            ArrayList<FileInfo> fileList = new ArrayList<>();
            File currentFolder = new File(mCurrentFolder);

            File fileArray[] = currentFolder.listFiles();
            if (fileArray != null) {
                for (File file : fileArray) {
                    fileList.add(new FileInfo(file));
                }
                Collections.sort(fileList);
            }

            if (mShowParentLink) {
                FileInfo parentLink = FileInfo.newParentLink(currentFolder.getParentFile());
                fileList.add(0, parentLink);
            }

            return fileList;
        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfoList) {
            mAdapter.updateList(fileInfoList);
            if (getListener() != null) {
                File currentFolder = new File(mCurrentFolder);
                getListener().onFolderListed(currentFolder.getName());
            }
        }

    }

    /**
     * This interface allows objects to receive FilePicker events.
     */
    public interface FileListHandlers {

        /**
         * Fired when user selects a file/directory.
         * @param fileInfo picked file info.
         */
        void onFileSelected(FileInfo fileInfo);

        /**
         * Fired when the folder ends loading.
         * @param name the folder name.
         */
        void onFolderListed(String name);

    }

}
