package com.minfo.quanmei.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.City;
import com.minfo.quanmei.entity.Province;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MyFileUpload;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.ChangeAddressDialog;
import com.minfo.quanmei.widget.ChangeBirthDialog;
import com.minfo.quanmei.widget.ModifyPersonInfo;
import com.minfo.quanmei.widget.SelectPicDialog;
import com.minfo.quanmei.widget.SelectPicDialog.SelectPicDialogClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonInfoActivity extends BaseActivity implements View.OnClickListener, ModifyPersonInfo.ModifyClickListener, SelectPicDialogClickListener {
    //top
    private TextView tvTitle;
    private ImageView ivLeft;

    private ImageView civHeadImage;
    private TextView tvNickname;
    private TextView tvAge;
    private TextView tvPosition;
    private TextView tvLevel;
    private User user;
    private ModifyPersonInfo modifyNickname;//更改昵称对话框
    private String tempNickname = "";
    private ChangeAddressDialog addressDialog;
    private ChangeBirthDialog birthDialog;
    private SelectPicDialog selectPicDialog;
    private String cameraSavePath;
    private String takephotoname;

    private Province province;
    private City city;

    private String birthday = "";
    private String[] birthArray;

    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    //上传头像变量
    File file;
    private List<Map<String, File>> files = new ArrayList<>();
    private Handler handler;
    private File imgFile;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_info);
        //top
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setText("个人资料");
        modifyNickname = new ModifyPersonInfo(this, this);

        if (Constant.user != null) {
            user = Constant.user;
            UniversalImageUtils.disCircleImage(user.getUserimg(), civHeadImage);
            tvNickname.setText(user.getUsername());
            tvAge.setText(user.getAge() + "");
            tvLevel.setText("LV" + user.getLevel());
            tvPosition.setText(user.getCity());
        }

    }


    @Override
    protected void findViews() {

        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvPosition = (TextView) findViewById(R.id.tv_position);
        tvLevel = (TextView) findViewById(R.id.tv_level);

        civHeadImage = (ImageView) findViewById(R.id.civ_head_image);
        civHeadImage.setOnClickListener(this);
        int identifier = getResources().getIdentifier("", "", "");
    }

    @Override
    protected void initViews() {
        selectPicDialog = new SelectPicDialog(this, this);
        initHandler();
    }

    private void initHandler() {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg.obj.toString());
                        int errorcode = jsonObject.getInt("errorcode");
                        switch (errorcode) {
                            case 0:
                                civHeadImage.setImageBitmap(imgUtils.toRoundBitmap(photo));
                                ToastUtils.show(PersonInfoActivity.this, "头像修改成功");
                                break;
                            case 12:
                                ToastUtils.show(PersonInfoActivity.this, "请上传一张图片");
                                break;
                            case 13:
                                ToastUtils.show(PersonInfoActivity.this, "所选上传图片格式不正确");
                                break;
                            case 14:
                                ToastUtils.show(PersonInfoActivity.this, "用户未登录");
                                LoginActivity.isJumpLogin = true;
                                utils.jumpAty(PersonInfoActivity.this, LoginActivity.class, null);
                                break;
                            default:
                                ToastUtils.show(PersonInfoActivity.this,"服务器繁忙");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (msg.what == 2) {
                    tvAge.setText(msg.obj.toString());
                }
                if (msg.what == 3) {
                    tvPosition.setText(msg.obj.toString());
                }

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                onBackPressed();
                break;
            case R.id.rl_head:

                break;
            case R.id.rl_nickname:
                modifyNickname.show();
                break;
            case R.id.rl_age:
                initBirthDialog();

                break;
            case R.id.rl_region:

                initAddressDialog();

                break;
            case R.id.rl_level:
                startActivity(new Intent(PersonInfoActivity.this, MyGrageActivity.class));
                break;
            case R.id.civ_head_image:
                selectPicDialog.show();
                break;
        }
    }

    /**
     * 初始化日期dialog
     */
    private void initBirthDialog() {
        birthDialog = new ChangeBirthDialog(this);
        birthDialog.setBirthdayListener(new ChangeBirthDialog.OnBirthListener() {
            @Override
            public void onClick(String year, String month, String day) {
                birthday = year + "-" + month + "-" + day;
                birthArray = birthday.split("-");
                reqEditBirth();
            }
        });
        birthDialog.show();
    }

    /**
     * 更改年龄接口
     */
    private void reqEditBirth() {
        String url = getResources().getString(R.string.api_baseurl) + "user/EditAge.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + user.getUserid() + "*" + birthday);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(PersonInfoActivity.this, "修改成功");
                reqMyInfo();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {

                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    ToastUtils.show(PersonInfoActivity.this, "生日格式不符合要求");
                } else if (errorcode == 13) {
                    ToastUtils.show(PersonInfoActivity.this, "日期不合法");
                } else if (errorcode == 14) {
                    ToastUtils.show(PersonInfoActivity.this, "用户未登录,请先登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(PersonInfoActivity.this, LoginActivity.class, null);
                } else {
                    ToastUtils.show(PersonInfoActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PersonInfoActivity.this, msg);
            }
        });
    }


    /**
     * 初始化省市dialog
     */
    private void initAddressDialog() {
        addressDialog = new ChangeAddressDialog(this);
        addressDialog.setAddresskListener(new ChangeAddressDialog.OnAddressCListener() {
            @Override
            public void onClick(Province province, City city) {
                PersonInfoActivity.this.province = province;
                PersonInfoActivity.this.city = city;

                reqEditCity();
            }
        });
        addressDialog.setAddress(0, 0);
        addressDialog.show();
    }

    /**
     * 更改省市接口
     */
    private void reqEditCity() {
        String url = getResources().getString(R.string.api_baseurl) + "user/EditDq.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + user.getUserid() + "*" + province.getId() + "*" + city.getId());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(PersonInfoActivity.this, "修改成功");
                String msg = city.getName();
                Message message = handler.obtainMessage(3, msg);
                handler.sendMessage(message);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {

                int errorcode = response.getErrorcode();
                if (errorcode == 14) {
                    ToastUtils.show(PersonInfoActivity.this, "用户未登录,请先登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(PersonInfoActivity.this, LoginActivity.class, null);
                } else {
                    ToastUtils.show(PersonInfoActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PersonInfoActivity.this, msg);
            }
        });
    }


    @Override
    public void moreClick(ModifyPersonInfo.Type type, String nickname) {
        modifyNickname.dismiss();
        if (type == ModifyPersonInfo.Type.CONFIRM) {
            if (nickname.equals(user.getUsername())) {
                modifyNickname.dismiss();
            } else {
                tempNickname = nickname;
                reqModifyName();
            }
        }
    }

    /**
     * 更改昵称接口
     */
    private void reqModifyName() {
        String url = getResources().getString(R.string.api_baseurl) + "user/EditName.php";
        String reqNickname = utils.convertNickname(tempNickname);
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + user.getUserid() + "*" + reqNickname);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(PersonInfoActivity.this, "修改成功");
                Constant.user.setUsername(tempNickname);
                tvNickname.setText(user.getUsername());
            }

            @Override
            public void onRequestNoData(BaseResponse response) {

                int errorcode = response.getErrorcode();
                if (errorcode == 12) {
                    ToastUtils.show(PersonInfoActivity.this, "昵称不符合要求");
                } else if (errorcode == 13) {
                    ToastUtils.show(PersonInfoActivity.this, "用户未登录,请先登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(PersonInfoActivity.this, LoginActivity.class, null);
                } else {
                    ToastUtils.show(PersonInfoActivity.this, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(PersonInfoActivity.this,msg);
            }
        });
    }
    /**
     * 更改头像接口
     */
    private void reqEditHeadImg() {
        files.clear();
        Map<String, File> map = new HashMap<>();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = getFilesDir() + File.separator + "IMG_" + timeStamp + "headimg" + ".jpg";
        imgUtils.createNewFile(imgName, imgFile.getPath());
        map.put(imgName, new File(imgName));
        files.add(map);

        final String url = getResources().getString(R.string.api_baseurl) + "user/EditImg.php";
        String str = utils.getBasePostStr() + "*" + Constant.user.getUserid();

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

    @Override
    public void selectClick(SelectPicDialog.Type type) {
        switch (type) {
            case CAMERA:
                callCamera();
                break;
            case ALBUM:
                getPicFromPhoto();
                break;
        }
    }

    /**
     * 调用系统相册
     */
    private void getPicFromPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, PHOTO_REQUEST);
    }

    /**
     * 调用拍照功能
     */
    public void callCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg")));
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功
                        file = new File(Environment.getExternalStorageDirectory() + "/test.jpg");
                        if (file.exists()) {
                            photoClip(Uri.fromFile(file));
                        }
                        break;
                    default:
                        break;
                }
                break;
            case PHOTO_REQUEST:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case PHOTO_CLIP:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        photo = extras.getParcelable("data");
                        String head_img_path = getFilesDir().getAbsolutePath() + "head_img.jpg";
                        boolean isScuccess = saveBitmap2file(photo, head_img_path);
                        if (isScuccess) {
                            imgFile = new File(head_img_path);
                            reqEditHeadImg();
                        }

                    }
                }
                break;
            default:
                break;
        }

    }

    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_CLIP);
    }

    private static boolean saveBitmap2file(Bitmap bmp, String filename) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bmp.compress(format, quality, stream);
    }



    /**
     * 我的页面信息
     */
    private void reqMyInfo() {
        String url = getResources().getString(R.string.api_baseurl) + "user/Main.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Constant.user = response.getObj(User.class);
                String msg = Constant.user.getAge() + "";
                Message message = handler.obtainMessage(2, msg);
                handler.sendMessage(message);
            }

            @Override
            public void onRequestNoData(BaseResponse response) {

            }

            @Override
            public void onRequestError(int code, String msg) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
