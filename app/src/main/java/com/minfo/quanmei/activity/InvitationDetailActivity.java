package com.minfo.quanmei.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.InvitationDetailGRAdapter;
import com.minfo.quanmei.config.ImageSelConfig;
import com.minfo.quanmei.entity.Group;
import com.minfo.quanmei.entity.GroupTag;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MinfoImg;
import com.minfo.quanmei.utils.MinfoUtils;
import com.minfo.quanmei.utils.MyFileUpload;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LoadingDialog;

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
 * 发帖详情页面
 * 2015年10月3日
 * zhang jiachang
 * 发帖写日记页面 2015-10-19 liujing
 */
public class InvitationDetailActivity extends BaseActivity implements View.OnClickListener, InvitationDetailGRAdapter.SelectTagListener {

    private ImageView back;
    private ImageView upLoad;
    private ImageView upLoad2;
    private ImageView face;
    private ImageView lable;
    private TextView newInvitation;
    private TextView writeDiary;
    private TextView release;
    private EditText title;
    private EditText content;
    private PopupWindow popupWindow;
    private DisplayMetrics outMetrics;
    private LinearLayout linearLayoutInvitation;
    private LinearLayout linearLayoutDiary;
    private LinearLayout lLDiaryRelease;
    private String[] arr = new String[]{"双眼皮", "开眼角"};
    private GridView gridViewLable;

    private LinearLayout llImgContainer;//显示图片的布局
    private boolean t1;//face开启或关闭的标志
    private boolean t2;//lable开启或关闭的标志

    private boolean isWriteDiary;//表示是发帖还是写日记，默认为false,即写日记

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


    private String cameraSavePath;
    private String takephotoname;
    private MinfoImg mfi;
    private MinfoUtils mfu;
    private int sw, sh;// 屏幕宽高

    private boolean isExit = false;

    private Group group;


    private android.os.Handler handler;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_detail);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.invitatindetail_back));
        upLoad = ((ImageView) findViewById(R.id.iv_upload_invitation));
        upLoad2 = ((ImageView) findViewById(R.id.iv_upload_invitation2));
        face = ((ImageView) findViewById(R.id.iv_face_invitation));
        lable = ((ImageView) findViewById(R.id.iv_lable_invitation));
        newInvitation = ((TextView) findViewById(R.id.tv_newinvitation));
        writeDiary = ((TextView) findViewById(R.id.tv_write_diary));
        release = ((TextView) findViewById(R.id.tv_release_invitation));
        title = ((EditText) findViewById(R.id.et_title_invitation));
        content = ((EditText) findViewById(R.id.et_content_invitation));
        linearLayoutInvitation = ((LinearLayout) findViewById(R.id.ll_new_invitation));
        linearLayoutDiary = ((LinearLayout) findViewById(R.id.ll_diary_invitation));

        release.setOnClickListener(this);

        lLDiaryRelease = ((LinearLayout) findViewById(R.id.ll_diary_release));
        gridViewLable = ((GridView) findViewById(R.id.gl_lable_inv));
        faceTV = ((TextView) findViewById(R.id.tv_face));
        llImgContainer = (LinearLayout) findViewById(R.id.ll_img_container);

        loadingDialog = new LoadingDialog(this);

    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        upLoad.setOnClickListener(this);
        upLoad2.setOnClickListener(this);
        face.setOnClickListener(this);
        lable.setOnClickListener(this);
        newInvitation.setOnClickListener(this);
        writeDiary.setOnClickListener(this);
        release.setOnClickListener(this);
        title.setOnClickListener(this);
        content.setOnClickListener(this);
        lLDiaryRelease.setOnClickListener(this);

        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            imgPaths = bundle.getStringArrayList("imgUrls");
            isWriteDiary=bundle.getBoolean("isWriteDiary");
            if (isWriteDiary){
                refreshTitleLabel(R.id.tv_write_diary);
            }
            if (imgPaths != null) {
                showImgs();
            }
        }

        refreshTag();
        initHandler();
    }

    private void initHandler() {

        handler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    loadingDialog.dismiss();
                    if(msg.obj!=null) {
                        try {
                            JSONObject jsonObject = new JSONObject(msg.obj.toString());
                            int errorcode = jsonObject.getInt("errorcode");
                            switch (errorcode) {
                                case 14:
                                    ToastUtils.show(InvitationDetailActivity.this, "标题不合法");
                                    break;
                                case 15:
                                    ToastUtils.show(InvitationDetailActivity.this, "请输入帖子内容");
                                    break;
                                case 18:
                                case 19:
                                    ToastUtils.show(InvitationDetailActivity.this, "您还未登录,请先登录~~");
                                    LoginActivity.isJumpLogin = true;
                                    utils.jumpAty(InvitationDetailActivity.this, LoginActivity.class, null);
                                    break;
                                case 0:
                                    ToastUtils.show(InvitationDetailActivity.this, "发布成功");
                                    Bundle bundle = new Bundle();
                                    Constant.currentGroupIndex = 0;
                                    bundle.putSerializable("group", Constant.groupDetail);
                                    utils.jumpAty(InvitationDetailActivity.this, GroupTypeActivity.class, bundle);
                                    appManager.finishActivity(InvitationDetailActivity.this);
                                    break;
                                default:
                                    ToastUtils.show(InvitationDetailActivity.this, "服务器繁忙");
                                    break;
                            }
                        } catch (JSONException e) {
                            ToastUtils.show(InvitationDetailActivity.this, "服务器繁忙");
                            e.printStackTrace();
                        }
                    }else{
                        ToastUtils.show(InvitationDetailActivity.this, "服务器繁忙");
                    }

                }
            }
        };
    }

    /**
     * 绑定标签数据
     */
    private void refreshTag() {
        groupTags = Constant.groupDetail.getTag();
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
        for (int i = 0; imgPaths!=null&&i < imgPaths.size(); i++) {
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
            case R.id.ll_diary_release:
                if (imgPaths.size() < 9) {
                    Constant.imageSelConfig = new ImageSelConfig.Builder().multiSelect(true).needCamera(true).build();
                    Intent intent = new Intent(this,PhotoViewActivity.class);
                    intent.putExtra("imgUrls",imgPaths);
                    startActivityForResult(intent,1);
                } else {
                    ToastUtils.show(this, "图片最多只能上传9张");
                }
                break;
            case R.id.iv_face_invitation:
                faceControl();
                break;
            case R.id.iv_lable_invitation:
                lableControl();
                break;
            case R.id.tv_newinvitation:
                refreshTitleLabel(R.id.tv_newinvitation);
                break;
            case R.id.tv_write_diary:
                refreshTitleLabel(R.id.tv_write_diary);
                break;
            case R.id.tv_release_invitation://点击发布
                if (checkInput()) {
                    if(utils.isOnLine(this)) {
                        loadingDialog.show();
                        reqServer();
                    }else{
                        ToastUtils.show(this,"暂时无网络，请检查设置");
                    }
                }
                break;
            case R.id.et_title_invitation:
                break;
            case R.id.et_content_invitation:
                break;
        }

    }

    /**
     * 切换顶部标签
     *
     * @param resId
     */
    private void refreshTitleLabel(int resId) {
        switch (resId) {
            case R.id.tv_newinvitation:
                linearLayoutInvitation.setVisibility(View.VISIBLE);
                linearLayoutDiary.setVisibility(View.GONE);
                release.setVisibility(View.VISIBLE);
                newInvitation.setTextColor(Color.RED);
                newInvitation.setBackgroundResource(R.drawable.text_group_left);
                writeDiary.setTextColor(Color.GRAY);
                writeDiary.setBackgroundResource(R.drawable.text_group_right_un);
                isWriteDiary = false;
                break;
            case R.id.tv_write_diary:
                linearLayoutInvitation.setVisibility(View.GONE);
                linearLayoutDiary.setVisibility(View.VISIBLE);
                release.setVisibility(View.GONE);
                newInvitation.setTextColor(Color.GRAY);
                newInvitation.setBackgroundResource(R.drawable.text_group_left_un);
                writeDiary.setTextColor(getResources().getColor(R.color.basic_color));
                writeDiary.setBackgroundResource(R.drawable.text_group_right);
                isWriteDiary = true;
                break;
        }
    }

    /**
     * 发帖请求接口
     */
    private void reqServer() {
        for (int i = 0; imgPaths!=null&&i < imgPaths.size(); i++) {
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

        final String url = getResources().getString(R.string.api_baseurl) + "wenzhang/AddTz.php";
        String title = utils.convertChinese(strTitle);
        String content = utils.convertChinese(strContent);
        if (Constant.groupDetail != null) {
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 检查输入
     */
    private boolean checkInput() {
        strTitle = title.getText().toString();
        strContent = content.getText().toString();
        if (TextUtils.isEmpty(strTitle)) {
            ToastUtils.show(InvitationDetailActivity.this, "帖子标题不能为空");
            return false;
        }
        if (TextUtils.isEmpty(strContent)) {
            ToastUtils.show(InvitationDetailActivity.this, "帖子内容不能为空");
            return false;
        }
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

                imgPaths.add(cameraSavePath + File.separator + takephotoname);
                showImgs();
            }else if(resultCode==PhotoViewActivity.SELECT_PHOTO){
                imgPaths = data.getBundleExtra("info").getStringArrayList("imgUrls");
                showImgs();
            }
        } else {
            ToastUtils.show(InvitationDetailActivity.this, "照相失败");
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

    /**
     * 页面finish时清除临时保存的内容
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        appManager.finishActivity(InvitationDetailActivity.class);
    }

    @Override
    public void selectTags(List<GroupTag> tags) {
        if (tags != null) {
            this.selectedTags = tags;
        }
    }
}
