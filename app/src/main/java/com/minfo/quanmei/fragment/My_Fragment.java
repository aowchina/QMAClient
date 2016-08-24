package com.minfo.quanmei.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.activity.MainActivity;
import com.minfo.quanmei.activity.MyCourseActivity;
import com.minfo.quanmei.activity.MyDiaryActivity;
import com.minfo.quanmei.activity.MyNoteActivity;
import com.minfo.quanmei.activity.MyReceiveActivity;
import com.minfo.quanmei.activity.OrderListActivity;
import com.minfo.quanmei.activity.PhotoViewActivity;
import com.minfo.quanmei.activity.PocketActivity;
import com.minfo.quanmei.config.ImageSelConfig;
import com.minfo.quanmei.entity.User;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.MyFileUpload;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.widget.DownLoadingDialog;
import com.minfo.quanmei.widget.LoadingDialog;
import com.minfo.quanmei.widget.UpdateDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
    private TextView myAge;
    private TextView myCity;
    private Button btnExit;
    private ImageView ivInfoBg;
    private Handler handler;

    private ImageView ivGender;


    private String downloadUrl = "";
    private int newVersionCode = 1;
    private int versionCode = 1;
    private User myinfo = null;
    private String downloadPath = "";
    private UpdateDialog updateDialog;
    private String apkName = "";
    private List<Map<String, File>> files = new ArrayList<>();

    private LoadingDialog loadingDialog;

    private DownLoadingDialog downLoadingDialog;

    private static final int SELECT_PHOTO = 1;
    private String selectPhotoPath;

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
        myAge = (TextView) view.findViewById(R.id.my_age);
        myCity = (TextView) view.findViewById(R.id.my_city);
        btnExit = (Button) view.findViewById(R.id.btn_exit_login);
        ivInfoBg = (ImageView) view.findViewById(R.id.iv_info_bg);
        ivGender = (ImageView) view.findViewById(R.id.iv_gender);
        btnExit.setOnClickListener(this);
        rldiary.setOnClickListener(this);
        rlnote.setOnClickListener(this);
        rlcollect.setOnClickListener(this);
        rlorder.setOnClickListener(this);
        rlsetting.setOnClickListener(this);
        rlUpdate.setOnClickListener(this);
        ivInfoBg.setOnClickListener(this);
        rlCourse.setOnClickListener(this);
        rlPocket.setOnClickListener(this);
        reqMyInfo();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
//        reqMyInfo();
    }

    private void initView() {
        if (myinfo != null) {
            myAge.setText(myinfo.getAge() + "");
            myCity.setText(myinfo.getCity());
            utils.sendMsg(MainActivity.myHandler, 1, myinfo);

            if (myinfo.getBgimg() != null && !"".equals(myinfo.getBgimg())) {
                UniversalImageUtils.displayImageUseDefOptions(myinfo.getBgimg(), ivInfoBg);
            }
            String gender = myinfo.getSex();
            if (gender != null && (gender.equals("暂未设置") || gender.equals("女"))) {
                ivGender.setImageResource(R.mipmap.sex_female);
            } else {
                ivGender.setImageResource(R.mipmap.sex_male);
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
                                ivInfoBg.setImageURI(Uri.fromFile(new File(selectPhotoPath)));
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
                Constant.imageSelConfig = new ImageSelConfig.Builder()
                        .needCamera(true)
                        .needCrop(true).multiSelect(false).cropSize(2, 1, 1440, 720).build();
                startActivityForResult(new Intent(mActivity, PhotoViewActivity.class), SELECT_PHOTO);
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
                Log.e(TAG, response.getErrorcode() + "");
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
                utils.setUserid(0);
                utils.setLogin(false);
                utils.setUserimg("");
                appManager.finishAllActivity();
                utils.jumpAty(mActivity, MainActivity.class, null);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == PhotoViewActivity.SELECT_PHOTO) {
            if(data!=null){
                Bundle bundle = data.getBundleExtra("info");
                if(bundle!=null){
                    ArrayList<String> strArray =  bundle.getStringArrayList("imgUrls");
                    if(strArray!=null&&strArray.size()!=0){
                        selectPhotoPath = strArray.get(0);
                        reqEditHeadImg();
                    }
                }
            }
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
        File file = new File(selectPhotoPath);
        map.put(file.getName(), file);
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
     *
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
