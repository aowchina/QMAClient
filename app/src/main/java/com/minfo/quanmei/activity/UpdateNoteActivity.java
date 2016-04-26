package com.minfo.quanmei.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.InvitationDetailGRAdapter;
import com.minfo.quanmei.entity.Group;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.entity.GroupTag;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MinfoImg;
import com.minfo.quanmei.utils.MinfoUtils;
import com.minfo.quanmei.utils.MyCheck;
import com.minfo.quanmei.utils.MyFileUpload;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.SelectPicDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更新帖子详情页面
 * 2015年11月11日
 * zhang jiachang
 */
public class UpdateNoteActivity extends BaseActivity implements View.OnClickListener, SelectPicDialog.SelectPicDialogClickListener, InvitationDetailGRAdapter.SelectTagListener {
    private GroupArticle groupArticle;
    private ImageView back;
    private ImageView upLoad;
    private ImageView upLoad2;
    private ImageView face;
    private ImageView lable;
    private TextView update;
    private TextView updateTv;
    private EditText title;
    private EditText content;
    private DisplayMetrics outMetrics;
    private LinearLayout linearLayoutInvitation;
    private String[] arr = new String[]{"双眼皮", "开眼角"};
    private GridView gridViewLable;

    private LinearLayout llImgContainer;//显示图片的布局
    private boolean t1;//face开启或关闭的标志
    private boolean t2;//lable开启或关闭的标志

    private boolean isWriteDiary;//表示是发帖还是写日记，默认为false,即写日记

    private SelectPicDialog selectPicDialog;

    private List<GroupTag> list = new ArrayList<GroupTag>();
    private InvitationDetailGRAdapter invitationDetailGRAdapter;
    private boolean lableTag;
    private TextView faceTV;
    public int num;

    private List<Bitmap> bitmapList = new ArrayList<>();
    private List<Map<String, File>> files = new ArrayList<>();
    private String strTitle;
    private String strContent;
    private Group groupDetail;
    private List<GroupTag> groupTags;
    private List<GroupTag> selectedTags = new ArrayList<>();//已选择的标签

    private ArrayList<String> imgPaths = new ArrayList<>();//从相册选择的照片的路径
    private String dorn="";

    private String cameraSavePath;
    private String takephotoname;
    private MinfoImg mfi;
    private MinfoUtils mfu;
    private int sw, sh;// 屏幕宽高

    private boolean isExit = false;


    private android.os.Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.invitatindetail_back));
        upLoad = ((ImageView) findViewById(R.id.iv_upload_invitation));
        upLoad2 = ((ImageView) findViewById(R.id.iv_upload_invitation2));
        face = ((ImageView) findViewById(R.id.iv_face_invitation));
        lable = ((ImageView) findViewById(R.id.iv_lable_invitation));
        update = ((TextView) findViewById(R.id.tv_update));
        updateTv = ((TextView) findViewById(R.id.tv_update_note));
        title = ((EditText) findViewById(R.id.et_title_invitation));
        content = ((EditText) findViewById(R.id.et_content_invitation));
        linearLayoutInvitation = ((LinearLayout) findViewById(R.id.ll_new_invitation));

        update.setOnClickListener(this);

        gridViewLable = ((GridView) findViewById(R.id.gl_lable_inv));
        faceTV = ((TextView) findViewById(R.id.tv_face));
        llImgContainer = (LinearLayout) findViewById(R.id.ll_img_container);

        selectPicDialog = new SelectPicDialog(this, this);

    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        upLoad.setOnClickListener(this);
        upLoad2.setOnClickListener(this);
        face.setOnClickListener(this);
        lable.setOnClickListener(this);

        title.setOnClickListener(this);
        content.setOnClickListener(this);
        if (Constant.noteTitle != null) {
            title.setText(Constant.noteTitle);
        }
        if (Constant.noteContent != null) {
            content.setText(Constant.noteContent);
        }
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            if (bundle.getStringArrayList("imgUrls")!=null) {
                imgPaths = bundle.getStringArrayList("imgUrls");
            }
            if (imgPaths != null) {
                showImgs();
            }
        }
        refreshTitleLabel();

        showTempContent();

        initHandler();


    }

    /**
     * 显示临时保存的数据，帖子标题，内容，已选择的小组标签
     */
    private void showTempContent() {
        if (Constant.groupDetail != null) {
            groupTags = Constant.groupDetail.getTag();
            refreshTag();
        }
        if (Constant.noteTitle != null) {
            title.setText(Constant.noteTitle);
        }
        if (Constant.noteContent != null) {
            content.setText(Constant.noteContent);
        }
        if (Constant.selectGroupTags != null) {
            selectedTags = Constant.selectGroupTags;
        }
    }

    private void initHandler() {

        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        int errorcode = jsonObject.getInt("errorcode");
                        switch (errorcode) {
                            case 109:
                                ToastUtils.show(UpdateNoteActivity.this, "标题不合法");
                                break;
                            case 111:
                                ToastUtils.show(UpdateNoteActivity.this, "请输入帖子内容");
                                break;
                            case 0:
                                ToastUtils.show(UpdateNoteActivity.this, "发布成功");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("group", Constant.groupDetail);
                                utils.jumpAty(UpdateNoteActivity.this, GroupTypeActivity.class, bundle);
                                appManager.finishActivity(InvitationDetailActivity.class);
                                clearTemp();
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }

    /**
     * 绑定标签数据
     */
    private void refreshTag() {
        if (!lableTag) {
            for (int i = 0; i < groupTags.size(); i++) {
                list.add(groupTags.get(i));
            }
            invitationDetailGRAdapter = new InvitationDetailGRAdapter(this, list, this);
            gridViewLable.setAdapter(invitationDetailGRAdapter);
        }
        lableTag = true;
    }

    /**
     * 显示从相册中选择的照片,并加入监听事件
     */
    public void showImgs() {
        llImgContainer.removeAllViews();
        for (int i = 0; i < imgPaths.size(); i++) {
            final int j = i;
            final View view = LayoutInflater.from(this).inflate(R.layout.item_container_img, null);
            ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            Bitmap bitmap = imgUtils.decodeSampledBitmapFromResource(imgPaths.get(i), utils.dip2px(45), utils.dip2px(45));
            ImageView ivSelectItem = (ImageView) view.findViewById(R.id.iv_select_item);
            ivSelectItem.setImageBitmap(bitmap);
            bitmapList.add(bitmap);
            llImgContainer.addView(view, i);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llImgContainer.removeView(view);
                    imgPaths.remove(j);
                    bitmapList.remove(j);
                    showImgs();

                }
            });

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.invitatindetail_back:
                onBackPressed();
                break;
            case R.id.iv_upload_invitation:
            case R.id.iv_upload_invitation2:



                    if (imgPaths.size() < 3) {
                        showSelectDialg();
                    } else {
                        ToastUtils.show(this, "图片最多只能上传3张");
                    }


                break;
            case R.id.iv_face_invitation:
                faceControl();
                break;
            case R.id.iv_lable_invitation:
                lableControl();
                break;

            case R.id.tv_update://点击发布
                if (checkInput()) {
                    reqServer();
                }
                break;
            case R.id.et_title_invitation:
                break;
            case R.id.et_content_invitation:
                break;
        }

    }

    /**
     * 确定来自帖子还是日记
     */
    private void refreshTitleLabel() {
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            groupArticle = (GroupArticle) bundle.getSerializable("group");
            dorn = bundle.getString("dorn");
            if ( dorn != null&&!dorn.equals("") ) {
                if (dorn.equals("note")) {
                    linearLayoutInvitation.setVisibility(View.VISIBLE);
                    updateTv.setText("更新帖子");


                    isWriteDiary = false;
                }
                if (dorn.equals("diary")) {
                    linearLayoutInvitation.setVisibility(View.VISIBLE);
                    updateTv.setText("更新日记");

                    isWriteDiary = true;
                }
            }
        }

    }

    /**
     * 保存临时的帖子标题和内容
     */
    private void saveTempContent() {
        strTitle = title.getText().toString();
        strContent = content.getText().toString();
        Constant.noteTitle = strTitle;
        Constant.noteContent = strContent;
        Constant.selectGroupTags = selectedTags;
    }

    /**
     * 页面暂停时保存输入内容
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (!isExit) {
            saveTempContent();
        }
    }

    /**
     * 发帖请求接口
     */
    private void reqServer() {
        for (int i = 0; i < imgPaths.size(); i++) {
            Log.e(TAG, imgPaths.toString());
            Map<String, File> map = new HashMap<>();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imgName = getFilesDir() + File.separator + "IMG_" + timeStamp + i + ".jpg";
            imgUtils.createNewFile(imgName, imgPaths.get(i));
            map.put(imgName, new File(imgName));
            files.add(map);
        }

        String tagIds = "";
        for (int i = 0; i < selectedTags.size(); i++) {
            tagIds += selectedTags.get(i).getId();
            tagIds += ",";
        }
        if (tagIds.length() > 0) {
            tagIds = tagIds.substring(0, tagIds.length() - 1);
        }

        final String url = getResources().getString(R.string.api_baseurl) + "Tiezi_add.php";
        String title = utils.convertChinese(strTitle);
        String content = utils.convertChinese(strContent);
        String str = utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + Constant.groupDetail.getId() + "*" + tagIds + "*" + title + "*" + content;
        final Map<String, String> params = utils.getParams(str);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyFileUpload fileUpload = new MyFileUpload();
                try {
                    String msg = fileUpload.postForm(url, params, files);
                    if (handler != null) {
                        Message message = handler.obtainMessage(1, msg);
                        handler.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    /**
     * 检查输入
     */
    private boolean checkInput() {
        strTitle = title.getText().toString();
        strContent = content.getText().toString();
        Constant.noteTitle = strTitle;
        Constant.noteContent = strContent;
        if (TextUtils.isEmpty(strTitle)) {
            ToastUtils.show(UpdateNoteActivity.this, "帖子标题不能为空");
            return false;
        }
        if (TextUtils.isEmpty(strContent)) {
            ToastUtils.show(UpdateNoteActivity.this, "帖子内容不能为空");
            return false;
        }
        if (!MyCheck.isNoteTitle(strTitle)) {
            ToastUtils.show(UpdateNoteActivity.this, "帖子标题不合法");
            return false;
        }

        return true;
    }

    private void showSelectDialg() {

        selectPicDialog.show();
    }

    /**
     * 表情布局
     */
    public void faceControl() {
        if (!t1) {
            faceTV.setVisibility(View.VISIBLE);
            gridViewLable.setVisibility(View.GONE);
            face.setImageResource(R.mipmap.btn_insert_face_sel);
            lable.setImageResource(R.mipmap.btn_insert_tag_nor);
            t1 = true;
            t2 = false;
        } else {
            faceTV.setVisibility(View.GONE);
            face.setImageResource(R.mipmap.btn_insert_face_nor);
            t1 = false;
        }
    }

    /**
     * 标签布局
     */
    public void lableControl() {
        if (!t2) {
            gridViewLable.setVisibility(View.VISIBLE);
            faceTV.setVisibility(View.GONE);
            face.setImageResource(R.mipmap.btn_insert_face_nor);
            lable.setImageResource(R.mipmap.btn_insert_tag_sel);
            t2 = true;
            t1 = false;
        } else {
            gridViewLable.setVisibility(View.GONE);
            lable.setImageResource(R.mipmap.btn_insert_tag_nor);
            t2 = false;
        }
    }

    /**
     * 调用拍照功能
     */
    public void callCamera() {

        boolean IsSDcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            if (!makeImgPath()) {
                ToastUtils.show(UpdateNoteActivity.this, "请检查您的SD卡");
                return;
            }
        } else {
            ToastUtils.show(UpdateNoteActivity.this, "请检查您的SD卡");
            return;
        }

        Intent it_zx = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        takephotoname = "IMG_" + timeStamp + ".jpg";
        File f = new File(cameraSavePath, takephotoname);
        Uri u = Uri.fromFile(f);
        it_zx.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        it_zx.putExtra(MediaStore.EXTRA_OUTPUT, u);
        startActivityForResult(it_zx, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                imgPaths.add(cameraSavePath + File.separator + takephotoname);
                showImgs();
            }
        } else {
            ToastUtils.show(UpdateNoteActivity.this, "照相失败");

        }
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
    public void selectClick(SelectPicDialog.Type type) {
        switch (type) {
            case CAMERA:
                callCamera();
                break;
            case ALBUM:
                //跳转到相册
                if ( dorn != null&&!dorn.equals("")) {
                    if (dorn.equals("note")) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isWriteDiary", isWriteDiary);
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        bundle.putString("dorn","note");
                        utils.jumpAty(this, AlbumActivity.class, bundle);
                        appManager.finishActivity();
                    }
                    if (dorn.equals("diary")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("dorn","diary");
                        bundle.putBoolean("isWriteDiary", isWriteDiary);
                        bundle.putStringArrayList("imgPaths", imgPaths);
                        utils.jumpAty(this, AlbumActivity.class, bundle);
                        appManager.finishActivity();
                    }
                }

                break;
        }
    }

    /**
     * 页面finish时清除临时保存的内容
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearTemp();
        appManager.finishActivity(InvitationDetailActivity.class);
    }

    private void clearTemp() {
        imgPaths.clear();
        isExit = true;
        Constant.noteTitle = null;
        Constant.noteContent = null;
        Constant.selectGroupTags = null;
    }

    @Override
    public void selectTags(List<GroupTag> tags) {
        if (tags != null) {
            this.selectedTags = tags;
        }
    }
}
