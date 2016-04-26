package com.minfo.quanmei.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.entity.HospitalPJ;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.NoteReply;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.utils.Utils;
import com.minfo.quanmei.widget.LLUserTitle;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangjiachang on 15/8/25.
 */
public class HospitalCommentAdapter extends BaseAdapter {
    private List<HospitalPJ> commentList;
    private Context context;
    private ClickListener operateListener;

    public HospitalCommentAdapter(Context context, List<HospitalPJ> commentList, ClickListener listener) {
        this.context = context;
        this.commentList = commentList;
        this.operateListener = listener;
    }


    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_comment_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        final HospitalPJ hospitalPJ = commentList.get(position);
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.ratingBar.setMax(5);
//        viewHolder.ratingBar.setProgress(Integer.parseInt(hospitalPJ.getStars()));
        viewHolder.ratingBar.setRating(Float.parseFloat(hospitalPJ.getStars()));
        viewHolder.ratingBar.setIsIndicator(true);
        UniversalImageUtils.displayImageUseDefOptions(hospitalPJ.getSimg(), viewHolder.ivProductSimg);
        viewHolder.tvEffect.setText("【"+hospitalPJ.getFname()+"】"+hospitalPJ.getName());

        viewHolder.userTitle.setNickName(hospitalPJ.getUsername());
        viewHolder.userTitle.setTime(hospitalPJ.getPubtime());
        viewHolder.userTitle.setUserAvatar(hospitalPJ.getUserimg());
        viewHolder.content.setText(hospitalPJ.getText());

        return convertView;
    }

    class ViewHolder {
        private RatingBar ratingBar;
        private LLUserTitle userTitle;
        private TextView content;
        private TextView tvEffect;
        private ImageView ivProductSimg;

        public ViewHolder(View view) {
            userTitle = (LLUserTitle) view.findViewById(R.id.ll_user_title);

            content = (TextView) view.findViewById(R.id.tv_comment_content);
            tvEffect = (TextView) view.findViewById(R.id.tv_comment_effect);
            ratingBar = ((RatingBar) view.findViewById(R.id.rb_hos_comment));
            ivProductSimg = (ImageView) view.findViewById(R.id.iv_product_simg);

        }
    }


    public interface ClickListener {
        /**
         * type:1进入全部回复页面
         * 2：点击回复按钮进入二级回复页面
         * 3：点击二级回复item进入二级回复页面
         *
         * @param reply
         * @param type
         */
        void click(NoteReply reply, int type);
    }


    /**
     * 请求点赞接口
     *
     * @param type 1：文章  2：评论
     * @param desId 表示点赞目标id，type为1，则为文章id，type为2，则为评论id
     */
    protected VolleyHttpClient httpClient;
    protected Utils utils;
    protected String TAG = getClass().getSimpleName();
    ;

    private void reqReprove(final int type, String desId, final ImageView img, final NoteFirstReply firstReply) {
        httpClient = new VolleyHttpClient(context);
        utils = new Utils(context);
        String url = context.getResources().getString(R.string.api_baseurl) + "Zan_add.php";


        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + type + "*" + desId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                ToastUtils.show(context, "成功！");
                img.setImageResource(R.mipmap.img_praised);
                firstReply.setStatus(1);
                notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                Log.e(TAG, response.getErrorcode() + "");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(context, msg);
            }
        });

    }

    /**
     * 请求取消点赞接口
     *
     * @param type  1：文章  2：评论
     * @param desId 表示点赞目标id，type为1，则为文章id，type为2，则为评论id
     */
    private void reqUnReprove(final int type, String desId, final ImageView img, final NoteFirstReply firstReply) {
        String url = context.getResources().getString(R.string.api_baseurl) + "Zan_delete.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + type + "*" + desId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(context, "取消成功！");
                img.setImageResource(R.mipmap.praise);
                firstReply.setStatus(0);
                notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                Log.e(TAG, response.getErrorcode() + "");
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(context, msg);
            }
        });

    }
}
