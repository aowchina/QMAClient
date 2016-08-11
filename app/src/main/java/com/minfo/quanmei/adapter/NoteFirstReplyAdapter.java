package com.minfo.quanmei.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minfo.quanmei.R;
import com.minfo.quanmei.activity.LoginActivity;
import com.minfo.quanmei.activity.NoteDetailActivity;
import com.minfo.quanmei.entity.NoteFirstReply;
import com.minfo.quanmei.entity.NoteReply;
import com.minfo.quanmei.entity.SecondReply;
import com.minfo.quanmei.http.BaseResponse;
import com.minfo.quanmei.http.RequestListener;
import com.minfo.quanmei.http.VolleyHttpClient;
import com.minfo.quanmei.utils.Constant;
import com.minfo.quanmei.utils.ToastUtils;
import com.minfo.quanmei.utils.Utils;
import com.minfo.quanmei.widget.LLUserTitle;
import com.minfo.quanmei.widget.LimitListView;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liujing on 15/9/30.
 * 帖子一级回复列表adapter
 */
public class NoteFirstReplyAdapter extends BaseAdapter {
    private List<NoteFirstReply> firstReplies;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener operateListener;

    protected VolleyHttpClient httpClient;
    protected Utils utils;
    protected String TAG = getClass().getSimpleName();

    public NoteFirstReplyAdapter(Context context, List<NoteFirstReply> firstReplies, ClickListener listener) {
        this.context = context;
        this.firstReplies = firstReplies;
        this.inflater = LayoutInflater.from(context);
        this.operateListener = listener;

        httpClient = new VolleyHttpClient(context);
        utils = new Utils(context);
    }

    public void addAll(Collection<? extends NoteFirstReply> list) {
        firstReplies.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return firstReplies.size();
    }

    @Override
    public Object getItem(int position) {
        return firstReplies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NoteFirstReply firstReply = firstReplies.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_note_first_reply, null);
            viewHolder.userTitle = (LLUserTitle) convertView.findViewById(R.id.ll_user_title);
            viewHolder.FirstContent = (TextView) convertView.findViewById(R.id.tv_first_content);
            viewHolder.llReply = (LinearLayout) convertView.findViewById(R.id.ll_reply);
            viewHolder.llPraise = (LinearLayout) convertView.findViewById(R.id.ll_praise);
            viewHolder.llvSecondReply = (LimitListView) convertView.findViewById(R.id.llv_second_reply);
            viewHolder.tvMoreSecondReply = (TextView) convertView.findViewById(R.id.tv_more_second_reply);
            viewHolder.haveSecondReply = convertView.findViewById(R.id.view_have_reply);
            viewHolder.ivPraise = (ImageView) convertView.findViewById(R.id.iv_praise);
            viewHolder.vfoot = convertView.findViewById(R.id.v_foot);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setValue(firstReply, viewHolder);
        if (position == firstReplies.size() - 1) {
            viewHolder.vfoot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.vfoot.setVisibility(View.GONE);
        }

        return convertView;
    }

    /**
     * 为控件赋值，绑定事件
     *
     * @param firstReply
     * @param viewHolder
     */

    private void setValue(final NoteFirstReply firstReply, final ViewHolder viewHolder) {
        viewHolder.userTitle.setNickName(firstReply.getUsername());
        viewHolder.userTitle.setTime(firstReply.getPubtime());
        viewHolder.userTitle.setUserAvatar(firstReply.getUserimg());
        viewHolder.userTitle.setUserid(firstReply.getUserid());

        viewHolder.FirstContent.setText(firstReply.getText());
        List<SecondReply> secondReplies = firstReply.getSon();
        NoteSecondReplyAdapter secondReplyAdapter;
        if (secondReplies.size() > 0) {
            viewHolder.haveSecondReply.setVisibility(View.VISIBLE);
        } else {
            viewHolder.haveSecondReply.setVisibility(View.GONE);
        }
        for (SecondReply secondReply : secondReplies) {
            secondReply.setParentId(firstReply.getId());
        }

        secondReplyAdapter = new NoteSecondReplyAdapter(context, secondReplies, operateListener);
        viewHolder.llvSecondReply.setAdapter(secondReplyAdapter);

        if (firstReply.getMore_hf() > 0) {
            viewHolder.tvMoreSecondReply.setText("更多" + (firstReply.getMore_hf()) + "回帖");
            viewHolder.tvMoreSecondReply.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tvMoreSecondReply.setVisibility(View.GONE);
        }


        if (firstReply.getStatus() == 0) {
            viewHolder.ivPraise.setImageResource(R.mipmap.praise);
        } else {
            viewHolder.ivPraise.setImageResource(R.mipmap.img_praised);
        }

        viewHolder.tvMoreSecondReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateListener.click(firstReply, 1);
            }
        });


//
        viewHolder.llReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateListener.click(firstReply, 2);
            }
        });
        viewHolder.llPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //operateListener.click(firstReply,4);
                if (firstReply.getStatus() == 0) {
                    reqReprove(2, firstReply.getId(), viewHolder.ivPraise, firstReply);
                } else {
                    reqUnReprove(2, firstReply.getId(), viewHolder.ivPraise, firstReply);

                }
            }
        });
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

    class ViewHolder {
        private LLUserTitle userTitle;
        private TextView FirstContent;
        private LinearLayout llReply;
        private LinearLayout llPraise;
        private LimitListView llvSecondReply;
        private TextView tvMoreSecondReply;
        private View haveSecondReply;
        private ImageView ivPraise;
        private View vfoot;
    }

    /**
     * 请求点赞接口
     *
     * @param type  1：文章  2：评论
     * @param desId 表示点赞目标id，type为1，则为文章id，type为2，则为评论id
     */

    private void reqReprove(final int type, String desId, final ImageView img, final NoteFirstReply firstReply) {
        httpClient = new VolleyHttpClient(context);
        utils = new Utils(context);
        String url = context.getResources().getString(R.string.api_baseurl) + "wenzhang/AddZan.php";


        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + utils.getUserid() + "*" + type + "*" + desId);
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
                int errorcode = response.getErrorcode();
                if (errorcode == 10||errorcode==14) {
                    LoginActivity.isJumpLogin = true;
                    NoteDetailActivity activity = (NoteDetailActivity) context;
                    activity.startActivityForResult(new Intent(activity,LoginActivity.class),1);
                } else if (errorcode == 16) {
                    ToastUtils.show(context, "您已赞过，请勿重复操作");
                } else {
                    ToastUtils.show(context, "服务器繁忙");
                }
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
        String url = context.getResources().getString(R.string.api_baseurl) + "wenzhang/DelZan.php";
        Map<String, String> params = utils.getParams(utils.getBasePostStr() + "*" + Constant.user.getUserid() + "*" + type + "*" + desId);
        httpClient.post(url, params, R.string.loading_msg, new RequestListener() {
            @Override
            public void onPreRequest() {

            }

            @Override
            public void onRequestSuccess(BaseResponse response) {
                ToastUtils.show(context, "取消成功！");
                img.setImageResource(R.mipmap.img_praised);
                firstReply.setStatus(0);
                notifyDataSetChanged();

            }

            @Override
            public void onRequestNoData(BaseResponse response) {
                int errorcode = response.getErrorcode();
                if (errorcode == 14) {
                    ToastUtils.show(context, "您处于未登录状态，请先登录");
                    LoginActivity.isJumpLogin = true;
                    utils.jumpAty(context, LoginActivity.class, null);
                } else if (errorcode == 15) {
                    ToastUtils.show(context, "记录不存在或已被删除");
                } else if (errorcode == 16) {
                    ToastUtils.show(context, "用户没有点过赞,不能取消");
                } else {
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
