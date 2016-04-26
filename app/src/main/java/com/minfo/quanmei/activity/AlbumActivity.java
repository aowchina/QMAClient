package com.minfo.quanmei.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.BaseViewHolder;
import com.minfo.quanmei.adapter.CommonAdapter;
import com.minfo.quanmei.entity.ImageFloder;
import com.minfo.quanmei.utils.ToastUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * 相册列表展示界面 create by liujing 2015-10-07
 */
public class AlbumActivity extends BaseActivity {

    private ListView lvAlbum;
    private ProgressDialog mProgressDialog;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<>();

    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    private int totalCount;
    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<>();
    private CommonAdapter<ImageFloder> albumAdapter;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            mProgressDialog.dismiss();
            // 为View绑定数据
//			data2View();
            // 初始化展示文件夹的popupWindw
            bindData();
        }
    };
    private File mImgDir;
    private List<String> mImgs;

    private ArrayList<String> imgPaths = new ArrayList<>();

    private boolean isWriteDiary;
    private String dorn = "";
    public static String type = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    @Override
    protected void findViews() {

        lvAlbum = (ListView) findViewById(R.id.lv_album);
    }

    @Override
    protected void initViews() {
        getImages();
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            isWriteDiary = bundle.getBoolean("isWriteDiary");
            if (bundle.getStringArrayList("imgPaths") != null) {
                imgPaths = bundle.getStringArrayList("imgPaths");
            }
            if (bundle.getString("dorn") != null && !bundle.getString("dorn").equals("")) {
                dorn = bundle.getString("dorn");
            }
        }


    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null) {
            ToastUtils.show(getApplicationContext(), "一张图片没扫描到");
            return;
        }

        mImgs = Arrays.asList(mImgDir.list());

    }

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
                ContentResolver mContentResolver = AlbumActivity.this
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
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);

                    if (picSize > mPicsSize) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
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

    private void bindData() {
        albumAdapter = new CommonAdapter<ImageFloder>(this, mImageFloders, R.layout.album_dir_item) {
            @Override
            public void convert(BaseViewHolder helper, ImageFloder item, int position) {
                helper.setText(R.id.tv_album_dir, item.getName().substring(1));
                helper.setText(R.id.tv_album_count, "(" + item.getCount() + ")");
                helper.setImageByUrl(R.id.iv_dir_item_image, item.getFirstImagePath());
            }
        };
        lvAlbum.setAdapter(albumAdapter);
        lvAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("imageLoader", mImageFloders.get(position));
                bundle.putBoolean("isWriteDiary", isWriteDiary);
                bundle.putStringArrayList("imgPaths", imgPaths);
                if (dorn != null && !dorn.equals("")) {
                    bundle.putString("dorn", dorn);
                }
                if (dorn.equals("")) {
                    bundle.putString("dorn", "");
                }

                utils.jumpAty(AlbumActivity.this, PhotoViewActivity.class, bundle);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Bundle bundle = new Bundle();
        bundle.putSerializable("imgUrls", imgPaths);
        if (!isWriteDiary) {
            if (dorn != null && !dorn.equals("")) {
                if (dorn.equals("note")) {
                    bundle.putString("dorn", "note");
                    utils.jumpAty(this, UpdateNoteActivity.class, bundle);
                }
            } else {

                bundle.putString("dorn", "");

                utils.jumpAty(this, InvitationDetailActivity.class, bundle);
            }
        } else {
            if (dorn != null) {
                if (dorn.equals("diary")) {
                    bundle.putString("dorn", "diary");
                    if (imgPaths.size() > 0) {
                        utils.jumpAty(AlbumActivity.this, WriteDiaryActivity.class, bundle);
                    } else {
                        utils.jumpAty(this, UpdateNoteActivity.class, bundle);
                    }

                }
                if (dorn.equals("")) {
                    bundle.putString("dorn", "");
                    if (imgPaths.size() > 0) {
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        utils.jumpAty(AlbumActivity.this, WriteDiaryActivity.class, bundle);
                    } else {
                        bundle.putBoolean("isWriteDiary", isWriteDiary);
                        utils.jumpAty(this, InvitationDetailActivity.class, bundle);
                    }
                }
            }


        }
    }
}
