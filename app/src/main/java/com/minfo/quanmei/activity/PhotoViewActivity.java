package com.minfo.quanmei.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.minfo.quanmei.adapter.PhotoGridAdapter;
import com.minfo.quanmei.config.ImageSelConfig;
import com.minfo.quanmei.entity.Image;
import com.minfo.quanmei.entity.ImageFloder;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 相册图片展示列表 create by liujing 2015-10-07
 */
public class PhotoViewActivity extends BaseActivity implements View.OnClickListener, PhotoGridAdapter.ItemSelectedListener {

    private ImageSelConfig config;

    private GridView gvPhoto;
    private TextView tvCancel;
    private Button btnComplete;
    private RelativeLayout rlBottom;


    private ArrayList<String> selectedPaths = new ArrayList<>();//当前已选照片的路径

    private PhotoGridAdapter photoGridAdapter;


    private ListPopupWindow folderPopupWindow;
    private RelativeLayout rlFolder;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<>();
    private List<Image> imageList = new ArrayList<>();
    private AlbumAdapter albumAdapter;

    private TextView tvFolderName;
    private ImageView ivLeft;

    public static String type = "";
    public static final int SELECT_PHOTO = 2;

    private String cameraSavePath;
    private String takephotoname;

    private String cropPath;

    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    private static final int REQUEST_CAMERA = 2;
    private static final int REQUEST_CROP = 3;

    private boolean hasFolderGened;

    private boolean firstJumpWriteDiary;


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

        rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom_ly);
    }

    @Override
    protected void initViews() {

        this.config = Constant.imageSelConfig;
        if(!config.multiSelect){
            rlBottom.setVisibility(View.GONE);
        }


        selectedPaths = getIntent().getStringArrayListExtra("imgUrls");
        firstJumpWriteDiary = getIntent().getBooleanExtra("firstJumpWriteDiary",false);
        if(selectedPaths==null){
            selectedPaths = new ArrayList<>();
        }

        tvFolderName.setText("所有图片");
        albumAdapter = new AlbumAdapter(this, mImageFloders, R.layout.album_dir_item);

        createPopupFolderList(utils.getScreenWidth(), 800);
        bindData();

        getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);

        gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (config.needCamera && position == 0) {
                    callCamera();
                } else {
                    if (!config.multiSelect) {
                        singleSelect(imageList.get(position).path);
                    }
                }
            }
        });

        photoGridAdapter = new PhotoGridAdapter(PhotoViewActivity.this, imageList, R.layout.photo_grid_item, PhotoViewActivity.this, selectedPaths);
        gvPhoto.setAdapter(photoGridAdapter);
    }

    /**
     * 获取所有图片，按文件夹分开
     */
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(PhotoViewActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        null, null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                CursorLoader cursorLoader = new CursorLoader(PhotoViewActivity.this,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'", null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    List<Image> tempImageList = new ArrayList<>();
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        if (!image.path.endsWith("gif"))
                            tempImageList.add(image);
                        if (!hasFolderGened) {
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            ImageFloder folder = new ImageFloder();
                            folder.setDir(folderFile.getAbsolutePath());
                            folder.setFirstImagePath(imageFile.getAbsolutePath());
                            if (!mImageFloders.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.setImageList(imageList);
                                mImageFloders.add(folder);
                            } else {
                                ImageFloder f = mImageFloders.get(mImageFloders.indexOf(folder));
                                f.getImageList().add(image);
                            }
                        }

                    } while (data.moveToNext());

                    imageList.clear();
                    if (config.needCamera)
                        imageList.add(new Image());
                    imageList.addAll(tempImageList);

                    ImageFloder folder = new ImageFloder();
                    folder.setImageList(imageList);
                    mImageFloders.add(0, folder);
                    albumAdapter.notifyDataSetChanged();
                    photoGridAdapter.notifyDataSetChanged();

                    hasFolderGened = true;
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 相册列表文件夹
     */
    private void bindData() {

        folderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    tvFolderName.setText("所有图片");
                    PhotoViewActivity.this.getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                } else {
                    imageList.clear();
                    tvFolderName.setText(mImageFloders.get(position).getName().substring(1));
                    if (config.needCamera) {
                        imageList.add(new Image());
                    }
                    imageList.addAll(mImageFloders.get(position).getImageList());
                    photoGridAdapter.notifyDataSetChanged();

                }
                folderPopupWindow.dismiss();
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

    /**
     * 调用拍照功能
     */
    public void callCamera() {

        boolean IsSDcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            if (!makeImgPath()) {
                ToastUtils.show(PhotoViewActivity.this, "请检查您的SD卡");
                return;
            }
        } else {
            ToastUtils.show(PhotoViewActivity.this, "请检查您的SD卡");
            return;
        }

        Intent it_zx = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        takephotoname = "IMG_" + timeStamp + ".jpg";
        File f = new File(cameraSavePath, takephotoname);
        Uri u = Uri.fromFile(f);
        it_zx.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        it_zx.putExtra(MediaStore.EXTRA_OUTPUT, u);
        startActivityForResult(it_zx, REQUEST_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (config.needCrop) {
                crop(cameraSavePath + File.separator + takephotoname);
            } else {
                selectedPaths.add(cameraSavePath + File.separator + takephotoname);
                complete();
            }
        } else if (requestCode == REQUEST_CROP && resultCode == RESULT_OK) {
            selectedPaths.add(cropPath);
            complete();
        }
    }


    private void crop(String path) {

        makeImgPath();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = new File(cameraSavePath, "IMG_" + timeStamp + ".jpg");
        cropPath = file.getAbsolutePath();

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", config.aspectX);
        intent.putExtra("aspectY", config.aspectY);
        intent.putExtra("outputX", config.outputX);
        intent.putExtra("outputY", config.outputY);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CROP);
    }

    /**
     * 创建照片保存路径
     */
    private boolean makeImgPath() {
        cameraSavePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "minfo_quanmei";
        File filePath = new File(cameraSavePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }
        if (!filePath.exists()) {
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                complete();
                break;
        }
    }


    /**
     * 选择图片完成
     */
    private void complete(){
        if(firstJumpWriteDiary){
            completeToJumpDiary();
        }else{
            completeNormal();
        }
    }

    /**
     * 跳转至来时的界面
     */
    private void completeNormal() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imgUrls", selectedPaths);
        intent.putExtra("info", bundle);
        setResult(SELECT_PHOTO, intent);
        finish();
    }

    /**
     * 跳转至写日记界面，比较特殊
     */
    private void completeToJumpDiary(){
        Intent intent = new Intent();
        intent.setClass(this,WriteDiaryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("imgUrls", selectedPaths);
        intent.putExtra("info", bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public void showSelected(List<String> selectImgUrls) {
        this.selectedPaths = (ArrayList<String>) selectImgUrls;
    }

    private void singleSelect(String path) {
        if (config.needCrop) {
            crop(path);
        } else {
            selectedPaths.add(path);
            complete();
        }
    }

}
