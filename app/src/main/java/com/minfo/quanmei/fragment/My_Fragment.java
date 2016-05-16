package com.minfo.quanmei.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.InitActivity;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.activity.MyCourseActivity;
import com.minfo.quanmei.activity.MyDiaryActivity;
import com.minfo.quanmei.activity.MyNoteActivity;
import com.minfo.quanmei.activity.MyReceiveActivity;
import com.minfo.quanmei.activity.OrderListActivity;
import com.minfo.quanmei.activity.PocketActivity;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MyFileUpload;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.DownLoadingDialog;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.SelectPicDialog;
import com.minfo.quanmei.widget.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class My_Fragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rldiary;
    private RelativeLayout rlnote;
    private RelativeLayout rlcollect;
    private RelativeLayout rlPocket;
    private RelativeLayout rlorder;
    private RelativeLayout rlCourse;
    private RelativeLayout rlsetting;
    private RelativeLayout rlUpdate;
    private ImageView myHeadImage;
    private TextView myNickname;
    private TextView myLevel;
    private TextView myAge;
    private TextView myCity;
    private Button btnExit;
    private ImageView ivInfoBg;
    private SelectPicDialog selectPicDialog;
    private Handler handler;


    private String downloadUrl = "";
    private int newVersionCode = 1;
    private int versionCode = 1;
    private User myinfo = null;
    private String downloadPath = "";
    private UpdateDialog updateDialog;
    private String apkName = "";
    private static final int PHOTO_REQUEST = 1;
    private static final int CAMERA_REQUEST = 2;
    private static final int PHOTO_CLIP = 3;
    private Bitmap photo;
    private List<Map<String, File>> files = new ArrayList<>();

    private LoadingDialog loadingDialog;

    private DownLoadingDialog downLoadingDialog;
    private File file;
    private File imgFile;
    private Bitmap resizeBmp;

    @Override
    protected View initViews() {
        View view = View.inflate(mActivity, R.layout.activity_personal_center, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findview(view);
    }


    private void findview(View view) {
        rldiary = (RelativeLayout) view.findViewById(R.id.rl_diary);
        rlnote = (RelativeLayout) view.findViewById(R.id.rl_note);
        rlcollect = (RelativeLayout) view.findViewById(R.id.rl_collect);
        rlorder = (RelativeLayout) view.findViewById(R.id.rl_order);
        rlCourse = (RelativeLayout) view.findViewById(R.id.rl_course);
        rlsetting = (RelativeLayout) view.findViewById(R.id.rl_setting);
        rlUpdate = (RelativeLayout) view.findViewById(R.id.rl_update);
        rlPocket = (RelativeLayout) view.findViewById(R.id.rl_pocket);
        myHeadImage = (ImageView) view.findViewById(R.id.civ_head_image);
        myNickname = (TextView) view.findViewById(R.id.personal_code);
        myAge = (TextView) view.findViewById(R.id.my_age);
        myCity = (TextView) view.findViewById(R.id.my_city);
        myLevel = (TextView) view.findViewById(R.id.my_level);
        btnExit = (Button) view.findViewById(R.id.btn_exit_login);
        ivInfoBg = (ImageView) view.findViewById(R.id.iv_info_bg);
        btnExit.setOnClickListener(this);
        myHeadImage.setOnClickListener(this);
        rldiary.setOnClickListener(this);
        rlnote.setOnClickListener(this);
        rlcollect.setOnClickListener(this);
        rlorder.setOnClickListener(this);
        rlsetting.setOnClickListener(this);
        rlUpdate.setOnClickListener(this);
        ivInfoBg.setOnClickListener(this);
        rlCourse.setOnClickListener(this);
        rlPocket.setOnClickListener(this);
        selectPicDialog = new SelectPicDialog(mActivity, selectListener);
        reqMyInfo();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        reqMyInfo();
    }

    private void initView() {
        if (myinfo != null) {
            UniversalImageUtils.disCircleImage(myinfo.getUserimg(), myHeadImage);
            myNickname.setText(myinfo.getUsername());
            myAge.setText(myinfo.getAge() + "");
            myCity.setText(myinfo.getCity());
            myLevel.setText("LV" + myinfo.getLevel());
            if (myinfo.getBgimg() != null && !"".equals(myinfo.getBgimg())) {
                UniversalImageUtils.displayImageUseDefOptions(myinfo.getBgimg(), ivInfoBg);
            }
        }
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
                                ivInfoBg.setImageBitmap(resizeBmp);
                                ToastUtils.show(mActivity, "修改成功");
                                break;
                            case 12:
                                ToastUtils.show(mActivity, "请上传一张图片");
                                break;
                            case 13:
                                ToastUtils.show(mActivity, "所选上传图片格式不正确");
                                break;
                            case 14:
                                ToastUtils.show(mActivity, "用户未登录");
                                LoginActivity.isJumpLogin = true;
                                utils.jumpAty(mActivity, LoginActivity.class, null);
                                break;
                            default:
                                ToastUtils.show(mActivity, "服务器繁忙");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.civ_head_image:
                if (myinfo != null) {
                    Intent intent = new Intent(getActivity(), PersonalHomePageActivity.class);
                    intent.putExtra("userid", myinfo.getUserid() + "");
                    startActivity(intent);
                }
                break;*/
            case R.id.rl_diary:
                startActivity(new Intent(getActivity(), MyDiaryActivity.class));
                break;
            case R.id.rl_note:
                startActivity(new Intent(getActivity(), MyNoteActivity.class));
                break;
            case R.id.rl_collect:
                startActivity(new Intent(getActivity(), MyReceiveActivity.class));
                break;
            case R.id.rl_order:
                utils.jumpAty(getActivity(), OrderListActivity.class, null);
                break;
            case R.id.rl_update:
                reqUpdate();
                break;
            case R.id.rl_course:
                utils.jumpAty(getActivity(), MyCourseActivity.class, null);
                break;
            case R.id.rl_pocket:
                utils.jumpAty(getActivity(), PocketActivity.class, null);
                break;
            case R.id.btn_exit_login:
                reqExitLogin();
                break;
            case R.id.iv_info_bg:
                selectPicDialog.show();
                break;
        }
    }

    /**
     * 检查更新
     */
    private void reqUpdate() {

        updateDialog = new UpdateDialog(mActivity);

        String url = getResources().getString(R.string.api_baseurl) + "public/Update.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {
                loadingDialog = new LoadingDialog(mActivity);
                loadingDialog.show();

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response.getData().toString());
                    newVersionCode = jsonObject.getInt("version");
                    downloadUrl = jsonObject.getString("url");
                    refreshDialog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                updateDialog.setMessage("目前已经是最新版本：" + utils.getVersionName());
                updateDialog.show();
                loadingDialog.dismiss();
            }

            @Override
            public void onRequestError(int code, String msg) {
                loadingDialog.dismiss();
                ToastUtils.show(mActivity, msg);
            }
        });
    }

    /**
     * 根据版本更新状态显示不同的dialog布局
     */
    private void refreshDialog() {
        versionCode = utils.getVersionCode();
        if (versionCode < newVersionCode) {//有更新
            updateDialog.setIsUpdated(true);
            updateDialog.setMessage("发现新版本，是否更新？");
            updateDialog.setListener(new UpdateDialog.DownLoadingListener() {
                @Override
                public void download() {
                    new DownLoadTask().execute(downloadUrl);
                }
            });
        } else {
            updateDialog.setMessage("目前已经是最新版本:" + utils.getVersionName());
        }

        updateDialog.show();
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
                myinfo = response.getObj(User.class);
                Constant.user = myinfo;
                initView();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(mActivity, "服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, msg);
            }
        });
    }

    //退出登录接口
    private void reqExitLogin() {
        String url = getResources().getString(R.string.api_baseurl) + "public/GetOut.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                Constant.user = null;
                utils.setCUserid("");
                utils.setUserid(0);
                utils.jumpAty(mActivity, InitActivity.class, null);
                appManager.finishAllActivity();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 13) {
                    ToastUtils.show(mActivity, "用户处于未登录状态");
                } else {
                    ToastUtils.show(mActivity, "服务器繁忙");
                }

            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(mActivity, msg);
            }
        });
    }

    SelectPicDialog.SelectPicDialogClickListener selectListener = new SelectPicDialog.SelectPicDialogClickListener() {
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
    };


    /**
     * 调用拍照功能
     */
    public void callCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "info_bg.jpg")));
        startActivityForResult(intent, CAMERA_REQUEST);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_REQUEST:
                switch (resultCode) {
                    case -1://-1表示拍照成功
                        file = new File(Environment.getExternalStorageDirectory() + "/info_bg.jpg");
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
                        String info_bg_path = mActivity.getFilesDir().getAbsolutePath() + "info_bg.jpg";
                        boolean isScuccess = saveBitmap2file(photo, info_bg_path);
                        if (isScuccess) {
                            imgFile = new File(info_bg_path);
                            Matrix matrix = new Matrix();
                            matrix.postScale(utils.getScreenWidth() / (photo.getWidth() * 1.0f), utils.getScreenWidth() / (photo.getWidth() * 1.0f)); //长和宽放大缩小的比例
                            resizeBmp = Bitmap.createBitmap(photo, 0, 0, photo.getWidth(), photo.getHeight(), matrix, true);
                            ivInfoBg.setImageBitmap(resizeBmp);
                            reqEditHeadImg();
                        }

                    }
                }
                break;
            default:
                break;
        }

    }

    /**
     * 修改个人中心背景
     */
    private void reqEditHeadImg() {
        final String url = getString(R.string.api_baseurl) + "user/EditBgImg.php";
        final Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid());

        files.clear();
        Map<String, File> map = new HashMap<>();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgName = mActivity.getFilesDir() + File.separator + "IMG_" + timeStamp + "infobg" + ".jpg";
        imgUtils.createNewFile(imgName, imgFile.getPath());
        map.put(imgName, new File(imgName));
        files.add(map);


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


    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 2);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 250);
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
     * 更新下载
     */
    class DownLoadTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            String urlStr = params[0];
            int progress = 0;
            try {
                downloadPath = Environment.getExternalStorageDirectory() + "/" + "download";
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(downloadPath);
                if (!file.exists()) {
                    file.mkdir();
                }
                apkName = mActivity.getPackageName() + ".apk";
                File apkFile = new File(downloadPath, apkName);
                if (apkFile.exists()) {
                    apkFile.delete();
                }
                apkFile.createNewFile();
                FileOutputStream fos = new FileOutputStream(apkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    publishProgress(progress);
                    if (numread <= 0) {
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (true);
                fos.close();
                is.close();


            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean) {
                File apkfile = new File(downloadPath, apkName);
                if (!apkfile.exists()) {
                    return;
                }

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                        "application/vnd.android.package-archive");
                startActivity(i);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }

        @Override
        protected void onPreExecute() {
            downLoadingDialog = new DownLoadingDialog(mActivity);
            downLoadingDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            downLoadingDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }
    }

    /**
     * xml解析
     * @param data
     */
    private void parseXml(String data) {
        try {
            HashMap<String, String> hm = new HashMap<>();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new ByteArrayInputStream(data.getBytes()));
            Element root = document.getDocumentElement();
            NodeList childNodes = root.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) childNode;
                    if ("version".equals(childElement.getNodeName())) {
                        hm.put("version", childElement.getFirstChild().getNodeValue());
                    } else if ("name".equals(childElement.getNodeName())) {
                        hm.put("name", childElement.getFirstChild().getNodeValue());
                    } else if ("url".equals(childElement.getNodeName())) {
                        hm.put("url", childElement.getFirstChild().getNodeValue());
                    }
                }
            }

            int serviceCode = Integer.valueOf(hm.get("version"));  //获取文件中version

            int versionCode = 1;
            versionCode = mActivity.getPackageManager().getPackageInfo("com.minfo.quanmei", 0).versionCode;  //获取APP当前version
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
