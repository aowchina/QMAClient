package com.minfo.quanmei.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
 * Created by min-fo-012 on 15/11/11.
 */
public class MyNoteResponseAdapter  extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<GroupArticle> list;
    private String str;

    public MyNoteResponseAdapter(Context context,List<GroupArticle> list,String  str) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final GroupArticle groupArticle = list.get(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.my_note_response_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.content.setText(groupArticle.getText());
        viewHolder.from.setText(groupArticle.getText2());
        viewHolder.name.setText(groupArticle.getUsername());
        viewHolder.time.setText(groupArticle.getPubtime());
        UniversalImageUtils.disCircleImage(groupArticle.getUserimg(), viewHolder.useIcon);
        viewHolder.from.setText(Html.fromHtml("<font color='#9b9b9b'>" + groupArticle.getUsername2()+"：" + "</font>" +   groupArticle.getText2()));
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reqDeleteComment(groupArticle.getId(), position);
            }
        });

        return convertView;
    }

    class ViewHolder {

        private ImageView useIcon;
        private TextView content;
        private TextView time;
        private TextView name;
        private ImageView delete;
        private TextView from;

        public ViewHolder(View view) {

            useIcon = (ImageView) view.findViewById(R.id.iv_response_icon);
            content = (TextView) view.findViewById(R.id.tv_response_content);
            from = (TextView) view.findViewById(R.id.tv_response_from);
            name = (TextView) view.findViewById(R.id.tv_response_name);
            time = (TextView) view.findViewById(R.id.tv_response_time);
            delete = (ImageView) view.findViewById(R.id.iv_response_delete);

        }
    }
    /**
     * 删除评论接口
     */
    protected VolleyHttpClient httpClient;
    protected Utils utils;
    private void reqDeleteComment(final String commentId, final int position){
        httpClient = new VolleyHttpClient(context);
        utils = new Utils(context);
        String url = context.getResources().getString(R.string.api_baseurl)+"user/DelHf.php";

        Map<String,String> params = utils.getParams(utils.getBasePostStr()+"*"+ Constant.user.getUserid()+"*"+commentId);
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
                    utils.jumpAty(context,LoginActivity.class,null);
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
