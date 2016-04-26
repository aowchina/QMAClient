package com.minfo.quanmei.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.utils.MinfoImg;
import com.minfo.quanmei.utils.MinfoUtils;
import com.minfo.quanmei.utils.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 回帖页面
 * 2015年10月7日
 * zhang jiachang
 */
public class ResponseInvitationActivity extends BaseActivity implements View.OnClickListener {

    private ImageView back;
    private ImageView upLoad;
    private ImageView upLoad2;
    private ImageView face;

    private EditText content;
    private PopupWindow popupWindow;
    private DisplayMetrics outMetrics;

    private boolean t1;//face开启或关闭的标志

    private TextView faceTV;
    private TextView response;
    private int number;
    private String cameraSavePath;
    private String takephotoname;
    private MinfoImg mfi;
    private MinfoUtils mfu;
    private int sw, sh;// 屏幕宽高




    private List<HashMap<String, String>> imgList = new ArrayList<HashMap<String, String>>();
    private LinearLayout imgLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_invitation);
    }

    @Override
    protected void findViews() {
        back = ((ImageView) findViewById(R.id.responseinvitation_back));
        upLoad = ((ImageView) findViewById(R.id.iv_upload_responseinvitation));
        upLoad2 = ((ImageView) findViewById(R.id.iv_upload_responseinvitation2));
        face = ((ImageView) findViewById(R.id.iv_face_responseinvitation));
        content = ((EditText) findViewById(R.id.et_content_responseinvitation));

        faceTV = ((TextView) findViewById(R.id.tv_face_responseinvitation));
        response = ((TextView) findViewById(R.id.tv_response_invitation));

    }

    @Override
    protected void initViews() {
        back.setOnClickListener(this);
        upLoad.setOnClickListener(this);
        upLoad2.setOnClickListener(this);
        face.setOnClickListener(this);

        content.setOnClickListener(this);
        response.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.responseinvitation_back:
                finish();
                break;
            case R.id.iv_upload_responseinvitation:
                getPopupWindow(v);
                break;
            case R.id.iv_upload_responseinvitation2:
                getPopupWindow(v);
                break;

            case R.id.iv_face_responseinvitation:
                faceControl();
                break;


            case R.id.et_content_responseinvitation:
                break;
            case R.id.tv_response_invitation:
                response();
                break;
        }

    }

    //表情布局
    public void faceControl() {
        if (!t1) {
            faceTV.setVisibility(View.VISIBLE);
            face.setImageResource(R.mipmap.btn_insert_face_sel);

            t1 = true;
        } else {
            faceTV.setVisibility(View.GONE);
            face.setImageResource(R.mipmap.btn_insert_face_nor);
            t1 = false;
        }
    }
    //回帖
    public void response(){
        String res=response.getText().toString();
        if (res.equals("")){
            ToastUtils.show(ResponseInvitationActivity.this,"回复内容不能为空");
        }else {
            //startActivity(new Intent(ResponseInvitationActivity.this,SecondAllReplyActivity.class));
        }
    }
    //调用拍照功能
    public void callCamera() {

        boolean IsSDcardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (IsSDcardExist) {
            if (!makeImgPath()) {
                ToastUtils.show(ResponseInvitationActivity.this, "请检查您的SD卡");
                return;
            }
        } else {
            ToastUtils.show(ResponseInvitationActivity.this, "请检查您的SD卡");
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

                Bitmap bm = mfi.getBpFromPath(cameraSavePath + File.separator + takephotoname);
            }
        } else {
            ToastUtils.show(ResponseInvitationActivity.this, "照相失败");

        }
    }

    //创建照片保存路径
    private boolean makeImgPath() {
        cameraSavePath = Environment.getExternalStorageDirectory().getPath() +
                File.separator + "minfo_ipet";
        File filePath = new File(cameraSavePath);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        if (!filePath.exists()) {
            return false;
        }
        return true;
    }

    private void initImgList() {
        imgLine.setPadding(5, 0, 5, 0);
        int imgwidth = (sw - 40) / 3;
        imgLine.removeAllViews();
        for (int i = 0; i < imgList.size(); i++) {
            ImageView addimg = new ImageView(this);
            addimg.setImageBitmap(mfi.getBpFromPath(imgList.get(i).get("imgpath")));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(imgwidth,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addimg.setScaleType(ImageView.ScaleType.CENTER_CROP);
            lp.setMargins(5, 10, 5, 10);
            imgLine.addView(addimg, lp);
        }
    }



    /**
     * 创建PopupWindow
     */

    public void initPopuptWindow(View v) {
        final View layout1 = LayoutInflater.from(this).inflate(
                R.layout.layout_invitation_pop, null);

        TextView camara = (TextView) layout1.findViewById(R.id.tv_camara_pop);
        TextView photo = (TextView) layout1.findViewById(R.id.tv_photo_pop);
        TextView cancle = (TextView) layout1.findViewById(R.id.tv_canle_pop);
        popupWindow = new PopupWindow(layout1, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;

        getWindow().setAttributes(params);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        //设置popupWindow弹出窗体的背景

        outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        popupWindow.setWidth(outMetrics.widthPixels * 4 / 5);
        popupWindow.setHeight(outMetrics.heightPixels * 1 / 3);
        //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_ab_share_pack_mtrl_alpha));
        popupWindow.showAtLocation(back, Gravity.LEFT, outMetrics.widthPixels * 1 / 10, outMetrics.heightPixels * 1 / 20);

        camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
                callCamera();

            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closePopupWindow();
            }
        });

    }


    /***
     * 获取PopupWindow实例
     */
    private void getPopupWindow(View v) {

        if (null != popupWindow) {
            closePopupWindow();
            return;
        } else {
            initPopuptWindow(v);
        }
    }

    /**
     * 关闭窗口
     */
    private void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1f;
            getWindow().setAttributes(params);
        }
    }
}