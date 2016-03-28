package com.viniciusam.filepicker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Vinicius on 20/03/2016.
 */
public class FileInfoAdapter extends ListRecyclerAdapter<FileInfo, FileInfoAdapter.ViewHolder> {

    protected static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault());

    protected Context mContext;
    protected boolean mDisableFiles;
    protected ClickHandlers mClickHandlers;

    private String mParentLinkDesc;
    private Drawable mBackDrawable;
    private Drawable mDirectoryDrawable;
    private Drawable mFileDrawable;

    public FileInfoAdapter(Context context, boolean disableFiles, ClickHandlers clickHandlers) {
        mContext = context;
        mDisableFiles = disableFiles;
        mClickHandlers = clickHandlers;

        Resources res = context.getResources();
        mParentLinkDesc = res.getString(R.string.parent_desc);
        mBackDrawable = res.getDrawable(R.drawable.ic_keyboard_backspace_grey600_48dp);
        mDirectoryDrawable = res.getDrawable(R.drawable.ic_folder_grey600_48dp);
        mFileDrawable = res.getDrawable(R.drawable.ic_file_grey600_48dp);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fileinfo_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v, mClickHandlers);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, FileInfo fileInfo) {
        // Convert the file date and size to a readable format.
        String fileDate = DATE_FORMAT.format(new Date(fileInfo.getLastModified()));

        if (fileInfo.isParentLink()) {
            holder.mFileImage.setImageDrawable(mBackDrawable);
            holder.mFileName.setText(fileInfo.getFileName());
            holder.mFileDate.setText(mParentLinkDesc);
            holder.mFileSize.setText(null);
        } else if (fileInfo.isDirectory()) {
            holder.mFileImage.setImageDrawable(mDirectoryDrawable);
            holder.mFileName.setText(fileInfo.getFileName());
            holder.mFileDate.setText(fileDate);
            holder.mFileSize.setText(null);
        } else {
            String fileSize = Formatter.formatFileSize(mContext, fileInfo.getFileSize());
            holder.mFileImage.setImageDrawable(mFileDrawable);
            holder.mFileName.setText(fileInfo.getFileName());
            holder.mFileDate.setText(fileDate);
            holder.mFileSize.setText(fileSize);
        }

        if (mDisableFiles && !fileInfo.isDirectory()) {
            holder.itemView.setEnabled(false);
        } else {
            holder.itemView.setEnabled(true);
        }
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mFileImage;
        public TextView mFileName;
        public TextView mFileDate;
        public TextView mFileSize;

        private ClickHandlers mClickHandlers;

        public ViewHolder(View v, ClickHandlers clickHandlers) {
            super(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandlers.onItemClick(getAdapterPosition());
                }
            });

            mClickHandlers = clickHandlers;

            mFileImage = (ImageView) v.findViewById(R.id.file_image);
            mFileName = (TextView) v.findViewById(R.id.file_name);
            mFileDate = (TextView) v.findViewById(R.id.file_date);
            mFileSize = (TextView) v.findViewById(R.id.file_size);
        }

    }

    public interface ClickHandlers {
        void onItemClick(int position);
    }

}
