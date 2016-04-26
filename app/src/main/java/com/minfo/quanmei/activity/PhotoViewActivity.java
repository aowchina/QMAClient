package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.PhotoGridAdapter;
import com.minfo.quanmei.entity.ImageFloder;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
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
    private List<String> imgPaths = new ArrayList<>();

    private ArrayList<String> selectedPaths = new ArrayList<>();//当前已选照片的路径
    private ArrayList<String> tempList = new ArrayList<>();

    private PhotoGridAdapter photoGridAdapter;
    private boolean isWriteDiary;
    private String dorn = "";

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
        tvCancel.setOnClickListener(this);
        btnComplete.setOnClickListener(this);
    }

    @Override
    protected void initViews() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            imageFloder = (ImageFloder) bundle.getSerializable("imageLoader");
            isWriteDiary = bundle.getBoolean("isWriteDiary");
            selectedPaths = bundle.getStringArrayList("imgPaths");
            if (bundle.getString("dorn") != null && !bundle.getString("dorn").equals("")) {
                dorn = bundle.getString("dorn");
            }
            for (int i = 0; i < selectedPaths.size(); i++) {
                tempList.add(selectedPaths.get(i));
            }
        }
        File imageFolderDir = new File(imageFloder.getDir());
        imgPaths = Arrays.asList(imageFolderDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        }));
        photoGridAdapter = new PhotoGridAdapter(this, imgPaths, R.layout.photo_grid_item, imageFolderDir.getAbsolutePath(), this, selectedPaths);

        gvPhoto.setAdapter(photoGridAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }

    @Override
    public void showSelected(List<String> selectImgUrls) {
        this.selectedPaths = (ArrayList<String>) selectImgUrls;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appManager.finishActivity();
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
        }
    }
}
