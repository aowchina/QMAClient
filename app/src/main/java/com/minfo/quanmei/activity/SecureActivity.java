package com.minfo.quanmei.activity;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class SecureActivity extends BaseActivity implements View.OnClickListener {
    //top
    private ImageView ivLeft;
    private TextView tvTitle;

    private TextView tvIntro;
    private ImageView ivContent;
    private TextView tvContent;

    private String title;
    private String img;
    private String content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_layout);
    }

    @Override
    protected void findViews() {

        ivLeft = (ImageView) findViewById(R.id.iv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivLeft.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(this);

        tvTitle.setText("全美发布");

        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivContent = (ImageView) findViewById(R.id.iv_intro);


    }

    @Override
    protected void initViews() {
        String url = getString(R.string.api_baseurl)+"baoxian/List.php";
        Map<String,String> params = utils.getParams(utils.getBasePostStr());
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    title = jsonObject.getString("title");
                    content = jsonObject.getString("intro");
                    img = jsonObject.getString("img");
                    bindData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                ToastUtils.show(SecureActivity.this,"服务器繁忙");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(SecureActivity.this,msg);
            }
        });
    }

    private void bindData() {
        tvIntro.setText(title);
        tvContent.setText(content);
        UniversalImageUtils.loadDefImage(img, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                ivContent.setImageResource(R.mipmap.default_pic);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                ivContent.setImageResource(R.mipmap.default_pic);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                Matrix matrix = new Matrix();
                matrix.postScale(utils.getScreenWidth() / (bitmap.getWidth() * 1.0f), utils.getScreenWidth() / (bitmap.getWidth() * 1.0f)); //长和宽放大缩小的比例
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                ivContent.setImageBitmap(resizeBmp);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                ivContent.setImageResource(R.mipmap.default_pic);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_left){
            onBackPressed();
        }
    }
}
