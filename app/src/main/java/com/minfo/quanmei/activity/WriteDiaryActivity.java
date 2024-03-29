package com.minfo.quanmei.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.adapter.InvitationDetailGRAdapter;
import com.minfo.quanmei.config.ImageSelConfig;
import com.minfo.quanmei.entity.GroupTag;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.widget.LoadingDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 写日记界面
 */
public class WriteDiaryActivity extends BaseActivity implements View.OnClickListener, InvitationDetailGRAdapter.SelectTagListener {

    //top
    private ImageView ivLeft;
    private TextView tvTitle;
    private TextView tvRight;

    private ImageView ivAddImg;
    private ImageView ivAddImg2;
    private LinearLayout llImgContainer;
    private ImageView ivFace;
    private ImageView ivLabel;
    private TextView tvPrice;
    private GridView gridViewLable;
    private TextView faceTV;

    private EditText etTitle;
    private EditText etContent;
    private EditText etHosName;

    private List<GroupTag> list = new ArrayList<GroupTag>();
    private InvitationDetailGRAdapter invitationDetailGRAdapter;
    private boolean lableTag;


    private boolean isFace;//face开启或关闭的标志
    private boolean isLabel;//lable开启或关闭的标志
    private List<GroupTag> groupTags;
    private List<GroupTag> selectedTags = new ArrayList<GroupTag>();//已选择的标签
    private LinearLayout llBottom;//底部线性布局
    private ListView llvPrice;
    private String hosName = "";


    private String[] prices = {"3000元以下", "3000-6000元", "6000-1万元", "1-2万元", "2-5万", "5万元以上"};
    private int priceId = 6;



    private ArrayList<String> imgPaths = new ArrayList<>();
    private String strTitle;
    private String strContent;
    private LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_diary);
    }

    @Override
    protected void findViews() {
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        tvTitle.setText("写日记");
        tvRight.setText("提交");
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);

        etTitle = (EditText) findViewById(R.id.et_title_invitation);
        etContent = (EditText) findViewById(R.id.et_content_invitation);
        etHosName = (EditText) findViewById(R.id.et_hos_name);

        ivAddImg = (ImageView) findViewById(R.id.iv_upload_invitation);
        ivAddImg.setOnClickListener(this);
        ivAddImg2 = (ImageView) findViewById(R.id.iv_upload_invitation2);
        ivAddImg2.setOnClickListener(this);
        ivLabel = (ImageView) findViewById(R.id.iv_lable_invitation);
        ivLabel.setOnClickListener(this);
        llImgContainer = (LinearLayout) findViewById(R.id.ll_img_container);
        ivFace = (ImageView) findViewById(R.id.iv_face_invitation);
        ivFace.setOnClickListener(this);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvPrice.setOnClickListener(this);
        llBottom = (LinearLayout) findViewById(R.id.ll_bottom);


        gridViewLable = ((GridView) findViewById(R.id.gl_lable_inv));
        faceTV = ((TextView) findViewById(R.id.tv_face));
        llvPrice = (ListView) findViewById(R.id.llv_price);

    }

    @Override
    protected void initViews() {

        loadingDialog = new LoadingDialog(this);
        Bundle bundle = getIntent().getBundleExtra("info");
        if (bundle != null) {
            imgPaths = bundle.getStringArrayList("imgUrls");
            showImgs();
        }
        refreshTag();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.tv_right://提交
                if (checkInput()) {
                    if (utils.isOnLine(this)) {
                        reqServer();
                    } else {
                        ToastUtils.show(this, "暂时无网络，请检查设置");
                    }
                }
                break;
            case R.id.iv_upload_invitation://相册相机选择对话框
            case R.id.iv_upload_invitation2:
                if (imgPaths.size() < 9) {
                    Constant.imageSelConfig = new ImageSelConfig.Builder().multiSelect(true).needCamera(true).multiSelect(true).build();
                    Intent intent = new Intent(this, PhotoViewActivity.class);
                    intent.putExtra("imgUrls", imgPaths);
                    startActivityForResult(intent, 1);
                } else {
                    ToastUtils.show(this, "图片最多只能上传9张");
                }
                break;
            case R.id.iv_lable_invitation://标签
                lableControl();
                break;
            case R.id.iv_face_invitation://表情
                faceControl();
                break;
            case R.id.tv_price://点击花费按钮
                showPriceList();
                break;
        }
    }

    /**
     * 显示价格列表
     */
    private void showPriceList() {
        llBottom.setVisibility(View.INVISIBLE);
        llvPrice.setVisibility(View.VISIBLE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_note_price, prices);
        llvPrice.setAdapter(adapter);
        llvPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                priceId = position;
                tvPrice.setText(prices[position]);
                llvPrice.setVisibility(View.GONE);
                llBottom.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 检查输入
     */
    private boolean checkInput() {
        strTitle = etTitle.getText().toString();
        strContent = etContent.getText().toString();
        if (!TextUtils.isEmpty(etHosName.getText())) {
            hosName = utils.convertChinese(etHosName.getText().toString());
        }
        if (!(imgPaths != null && imgPaths.size() != 0)) {
            ToastUtils.show(WriteDiaryActivity.this, "写日记必须添加图片");
            return false;
        }

        return true;
    }

    /**
     * 写日记请求接口
     */
    private void reqServer() {
        Map<String, File> map = new HashMap<>();
        for (int i = 0; i < imgPaths.size(); i++) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imgName = getFilesDir() + File.separator + "IMG_" + timeStamp + i + 1 + ".jpg";
            imgUtils.createNewFile(imgName, imgPaths.get(i));
            map.put(imgName, new File(imgName));
        }

        String tagIds = "";
        for (int i = 0; i < selectedTags.size(); i++) {
            tagIds += selectedTags.get(i).getId();
            tagIds += ",";
        }
        if (tagIds.length() > 0) {
            tagIds = tagIds.substring(0, tagIds.length() - 1);
        }

        final String url = getResources().getString(R.string.api_baseurl) + "wenzhang/AddRj.php";
        String title = utils.convertChinese(strTitle);
        String content = utils.convertChinese(strContent);
        if (Constant.groupDetail != null) {
            String str = utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + Constant.groupDetail.getId() + "*" + tagIds + "*" + title + "*" + content + "*" + hosName + "*" + priceId;

            final Map<String, String> params = utils.getParams(str);
            httpClient.multiRequest(url, params, map, new RequestListener() {
                @Override
                public void onPreRequest() {
                    loadingDialog.show();
                }

                @Override
                public void onRequestSuccess(BaseResponse response) {
                    loadingDialog.dismiss();
                    ToastUtils.show(WriteDiaryActivity.this, "发布成功");
                    Bundle bundle = new Bundle();
                    Constant.currentGroupIndex = 0;
                    bundle.putSerializable("group", Constant.groupDetail);
                    utils.jumpAty(WriteDiaryActivity.this, GroupTypeActivity.class, bundle);
                    appManager.finishActivity(WriteDiaryActivity.class);

                }

                @Override
                public void onRequestNoData(BaseResponse response) {
                    loadingDialog.dismiss();
                    int errorcode = response.getErrorcode();
                    if(errorcode==14){
                        ToastUtils.show(WriteDiaryActivity.this, "标题不合法");
                    }else if(errorcode==18||errorcode==19){
                        ToastUtils.show(WriteDiaryActivity.this, "用户未登录,请先登录");
                        LoginActivity.isJumpLogin = true;
                        utils.jumpAty(WriteDiaryActivity.this, LoginActivity.class, null);
                    }else{
                        ToastUtils.show(WriteDiaryActivity.this,"服务器繁忙");
                    }
                }

                @Override
                public void onRequestError(int code, String msg) {
                    loadingDialog.dismiss();
                    ToastUtils.show(WriteDiaryActivity.this,msg);
                }
            });
        }
    }


    /**
     * 页面暂停时保存输入内容
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 表情布局
     */
    public void faceControl() {
        if (!isFace) {
            faceTV.setVisibility(View.GONE);
            gridViewLable.setVisibility(View.GONE);
            ivFace.setImageResource(R.mipmap.btn_insert_face_sel);
            ivLabel.setImageResource(R.mipmap.btn_insert_tag_nor);
            isFace = true;
            isLabel = false;
        } else {
            faceTV.setVisibility(View.GONE);
            ivFace.setImageResource(R.mipmap.btn_insert_face_nor);
            isFace = false;
        }
    }

    /**
     * 标签布局
     */
    public void lableControl() {
        if (!isLabel) {
            gridViewLable.setVisibility(View.VISIBLE);
            faceTV.setVisibility(View.GONE);
            ivFace.setImageResource(R.mipmap.btn_insert_face_nor);
            ivLabel.setImageResource(R.mipmap.btn_insert_tag_sel);
            isLabel = true;
            isFace = false;
        } else {
            gridViewLable.setVisibility(View.GONE);
            ivLabel.setImageResource(R.mipmap.btn_insert_tag_nor);
            isLabel = false;
        }
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
            ImageView ivSelectItem = (ImageView) view.findViewById(R.id.iv_select_item);
            ivSelectItem.setImageBitmap(imgUtils.decodeSampledBitmapFromResource(imgPaths.get(i), utils.dip2px(45), utils.dip2px(45)));
            llImgContainer.addView(view, i);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    llImgContainer.removeView(view);
                    imgPaths.remove(j);
                    showImgs();
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == PhotoViewActivity.SELECT_PHOTO) {
            if (data != null) {
                Bundle bundle = data.getBundleExtra("info");
                if (bundle != null) {
                    imgPaths = bundle.getStringArrayList("imgUrls");
                    if (imgPaths != null) {
                        showImgs();
                    }
                }
            }
        }
    }

    /**
     * 页面finish时清除临时保存的内容
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void selectTags(List<GroupTag> tags) {
        if (tags != null) {
            this.selectedTags = tags;
        }
    }
}
