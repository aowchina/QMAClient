package com.minfo.quanmei.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.AlbumAdapter;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.adapter.PhotoGridAdapter;
import com.minfo.quanmei.entity.ImageFloder;
import com.minfo.quanmei.utils.ToastUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 相册图片展示列表 create by liujing 2015-10-07
 */
public class PhotoViewActivity extends BaseActivity implements View.OnClickListener, PhotoGridAdapter.ItemSelectedListener {
    private GridView gvPhoto;
    private TextView tvCancel;
    private Button btnComplete;
    private ImageFloder imageFloder;
    //某个相册里所有图片的本地路径
    private List<String> currentDirImgPaths = new ArrayList<>();

    private ImageFloder currentFolder;

    private ArrayList<String> selectedPaths = new ArrayList<>();//当前已选照片的路径
    private ArrayList<String> tempList = new ArrayList<>();


    private PhotoGridAdapter photoGridAdapter;


    private ListPopupWindow folderPopupWindow;
    private ProgressDialog mProgressDialog;
    private RelativeLayout rlFolder;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();
    private ArrayList<String> imgPaths = new ArrayList<>();
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<>();
    private AlbumAdapter albumAdapter;

    private TextView tvFolderName;
    private ImageView ivLeft;

    private boolean isWriteDiary;
    private String dorn = "";
    public static String type = "";
    public static final int SELECT_PHOTO = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            bindData();
        }
    };
    private File imageFolderDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);
    }

    @Override
    protected void findViews() {
        gvPhoto = (GridView) findViewById(R.id.gv_photo);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        btnComplete = (Button) findViewById(R.id.btn_complete);
        tvFolderName = (TextView) findViewById(R.id.tv_folder_name);
        rlFolder = (RelativeLayout) findViewById(R.id.rl_folder_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
        tvFolderName.setOnClickListener(this);
    }

    @Override
    protected void initViews() {

        selectedPaths = getIntent().getStringArrayListExtra("imgUrls");

        createPopupFolderList(utils.getScreenWidth(),800);
        getImages();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_folder_name:
                folderPopupWindow.show();
                break;
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.btn_complete:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("imgUrls",selectedPaths);
                intent.putExtra("info",bundle);
                setResult(SELECT_PHOTO,intent);
                finish();
                break;
        }


        /*switch (v.getId()) {
            case R.id.tv_cancel:
                onBackPressed();

                break;
            case R.id.btn_complete:
                Bundle bundle = new Bundle();
                if (!isWriteDiary) {
                    Intent intent = new Intent();
                    bundle.putSerializable("imgUrls", selectedPaths);

                    if (dorn != null && !dorn.equals("")) {
                        if (dorn.equals("note")) {
                            bundle.putString("dorn", "note");
                            intent.putExtra("info", bundle);
                            intent.setClass(this, UpdateNoteActivity.class);
                        }
                    } else {
                        //bundle.putString("dorn", "");
                        intent.putExtra("info", bundle);
                        intent.setClass(this, InvitationDetailActivity.class);
                    }
                    startActivity(intent);


                } else {
                    bundle.putStringArrayList("imgUrls", selectedPaths);
                    if (dorn.equals("diary")) {
                        bundle.putString("dorn", "diary");

                        utils.jumpAty(this, WriteDiaryActivity.class, bundle);
                    } else {
                        bundle.putString("dorn", "");
                        utils.jumpAty(this, WriteDiaryActivity.class, bundle);
                    }
                }
                appManager.finishActivity();
                appManager.finishActivity(AlbumActivity.class);
                break;
        }*/
    }

    @Override
    public void showSelected(List<String> selectImgUrls) {
        this.selectedPaths = (ArrayList<String>) selectImgUrls;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*appManager.finishActivity();
        appManager.finishActivity(AlbumActivity.class);
        Bundle bundle0 = new Bundle();
        bundle0.putStringArrayList("imgUrls", tempList);
        if (!isWriteDiary) {
            if (dorn != null && !dorn.equals("")) {
                if (dorn.equals("note")) {
                    bundle0.putString("dorn", "note");
                    utils.jumpAty(this, UpdateNoteActivity.class, bundle0);
                }
            } else {
                bundle0.putString("dorn", "");
                utils.jumpAty(PhotoViewActivity.this, InvitationDetailActivity.class, bundle0);
            }
        } else {
            if (dorn != null) {
                if (dorn.equals("diary")) {
                    bundle0.putString("dorn", "diary");
                    if (selectedPaths.size() > 0) {
                        utils.jumpAty(PhotoViewActivity.this, WriteDiaryActivity.class, bundle0);
                    } else {
                        utils.jumpAty(this, UpdateNoteActivity.class, bundle0);
                    }
                }
                if (dorn.equals("")) {
                    bundle0.putString("dorn", "");
                    if (selectedPaths.size() > 0) {
                        bundle0.putStringArrayList("imgPaths", selectedPaths);

                        utils.jumpAty(PhotoViewActivity.this, WriteDiaryActivity.class, bundle0);
                    } else {
                        bundle0.putBoolean("isWriteDiary", isWriteDiary);
                        utils.jumpAty(this, InvitationDetailActivity.class, bundle0);
                    }
                }
            }
        }*/
    }


    /**
     * 获取所有图片，按文件夹分开
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            ToastUtils.show(this, "暂无外部存储");
            return;
        }
        // 显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = PhotoViewActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                    }

                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }

    /**
     * 获取当前文件夹内的图片路径
     * @param currentFolder
     */
    private void initCurrentFolderPaths(ImageFloder currentFolder){

        this.imageFolderDir = new File(currentFolder.getDir());
        currentDirImgPaths = Arrays.asList(imageFolderDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));

        photoGridAdapter = new PhotoGridAdapter(PhotoViewActivity.this, currentDirImgPaths, R.layout.photo_grid_item, imageFolderDir.getAbsolutePath(), PhotoViewActivity.this, selectedPaths);
        gvPhoto.setAdapter(photoGridAdapter);
    }


    /**
     * 相册列表文件夹
     */
    private void bindData() {

        initCurrentFolderPaths(mImageFloders.get(0));
        tvFolderName.setText(mImageFloders.get(0).getName().substring(1));



        albumAdapter = new AlbumAdapter(this,mImageFloders,R.layout.album_dir_item);

        folderPopupWindow.setAdapter(albumAdapter);

        folderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvFolderName.setText(mImageFloders.get(position).getName().substring(1));
                folderPopupWindow.dismiss();
                initCurrentFolderPaths(mImageFloders.get(position));
            }
        });
    }



    private void createPopupFolderList(int width, int height) {
        folderPopupWindow = new ListPopupWindow(this);
        folderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        folderPopupWindow.setAdapter(albumAdapter);
        folderPopupWindow.setContentWidth(width);
        folderPopupWindow.setWidth(width);
        folderPopupWindow.setHeight(height);
        folderPopupWindow.setAnchorView(rlFolder);
        folderPopupWindow.setModal(true);
    }

}
