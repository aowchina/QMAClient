package com.minfo.quanmei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.entity.GroupArticle;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.UniversalImageUtils;
import com.minfo.quanmei.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangjiachang on 15/11/11.
 */
public class MyPostActivityAdapter extends BaseAdapter {
    private Context context;
    private List<GroupArticle> list;
    private String str;

    public MyPostActivityAdapter(Context context,List<GroupArticle> list,String str) {
        this.context = context;
        this.list=list;
        this.str=str;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_diary_itme, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        final GroupArticle groupArticle = list.get(position);
        viewHolder.content.setText(groupArticle.getTitle());
        viewHolder.name.setText(groupArticle.getUsername());
        viewHolder.time.setText(groupArticle.getPubtime());
        viewHolder.tv_note_post.setText(groupArticle.getTitle());
        UniversalImageUtils.disCircleImage(groupArticle.getUserimg(), viewHolder.useIcon);

        viewHolder.iconA.setVisibility(View.VISIBLE);
        viewHolder.iconB.setVisibility(View.VISIBLE);


        if (groupArticle.getImgs().size() >= 2) {
            UniversalImageUtils.displayImageUseDefOptions(groupArticle.getImgs().get(0), viewHolder.iconA);
            UniversalImageUtils.displayImageUseDefOptions(groupArticle.getImgs().get(1), viewHolder.iconB);
        } else if (groupArticle.getImgs().size() == 1) {
            UniversalImageUtils.displayImageUseDefOptions(groupArticle.getImgs().get(0), viewHolder.iconA);
            viewHolder.iconB.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.iconA.setVisibility(View.GONE);
            viewHolder.iconB.setVisibility(View.GONE);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqDeleteArticle(groupArticle.getId(), position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView iconA;
        private ImageView iconB;
        private ImageView useIcon;
        private TextView content;
        private TextView time;
        private TextView name;
        private ImageView delete;
        private TextView tv_note_post;
        private LinearLayout imgContiner;

        public ViewHolder(View view) {
            iconA = (ImageView) view.findViewById(R.id.iv_note_img1);
            iconB = (ImageView) view.findViewById(R.id.iv_note_img2);
            imgContiner=((LinearLayout) view.findViewById(R.id.ll_note_img));
            useIcon = (ImageView) view.findViewById(R.id.iv_note_icon);
            content = (TextView) view.findViewById(R.id.tv_note_content);
            name = (TextView) view.findViewById(R.id.tv_response_name);
            time = (TextView) view.findViewById(R.id.tv_response_time);
            delete = (ImageView) view.findViewById(R.id.iv_delete);
            tv_note_post = (TextView)view.findViewById(R.id.tv_note_post);

        }
    }
    /**
     * 删除文章接口
     */
    protected VolleyHttpClient httpClient;
    protected Utils utils;
    private void reqDeleteArticle(String articleId, final int position){
        httpClient = new VolleyHttpClient(context);
        utils = new Utils(context);
        String url = context.getResources().getString(R.string.api_baseurl)+"user/DelWz.php";

        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+articleId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {

                ToastUtils.show(context, "删除成功！");
                list.remove(position);
                notifyDataSetChanged();
            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if(errorcode==13){
                    ToastUtils.show(context, "用户处于未登录状态");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(context, LoginActivity.class, null);
                }else if(errorcode==14){
                    ToastUtils.show(context, "记录不存在");
                }else{
                    ToastUtils.show(context, "服务器繁忙");
                }
            }

            @Override
            public void onRequestError(int code, String msg) {
                ToastUtils.show(context, msg);
            }
        });

    }
}
