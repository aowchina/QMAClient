package com.minfo.quanmei.chat.widget.chatrow;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.util.EMLog;
import com.minfo.quanmei.R;

public class EaseChatRowVoice extends EaseChatRow{

    private ImageView voiceImageView;
    private TextView voiceLengthView;
    private ImageView readStatusView;

    public EaseChatRowVoice(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_voice : R.layout.ease_row_sent_voice, this);
    }

    @Override
    protected void onFindViewById() {
        voiceImageView = ((ImageView) findViewById(R.id.iv_voice));
        voiceLengthView = (TextView) findViewById(R.id.tv_length);
        readStatusView = (ImageView) findViewById(R.id.iv_unread_voice);
    }

    @Override
    protected void onSetUpView() {
        EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
        int len = voiceBody.getLength();
        if(len>0){
            voiceLengthView.setText(voiceBody.getLength() + "\"");
            voiceLengthView.setVisibility(View.VISIBLE);
        }else{
            voiceLengthView.setVisibility(View.INVISIBLE);
        }
        if (EaseChatRowVoicePlayClickListener.playMsgId != null
                && EaseChatRowVoicePlayClickListener.playMsgId.equals(message.getMsgId()) && EaseChatRowVoicePlayClickListener.isPlaying) {
            AnimationDrawable voiceAnimation;
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.voice_from_icon);
            } else {
                voiceImageView.setImageResource(R.drawable.voice_to_icon);
            }
            voiceAnimation = (AnimationDrawable) voiceImageView.getDrawable();
            voiceAnimation.start();
        } else {
            if (message.direct() == EMMessage.Direct.RECEIVE) {
                voiceImageView.setImageResource(R.drawable.ease_chatfrom_voice_playing);
            } else {
                voiceImageView.setImageResource(R.drawable.ease_chatto_voice_playing);
            }
        }
        
        if (message.direct() == EMMessage.Direct.RECEIVE) {
            if (message.isListened()) {
                // hide the unread icon
                readStatusView.setVisibility(View.INVISIBLE);
            } else {
                readStatusView.setVisibility(View.VISIBLE);
            }
            EMLog.d(TAG, "it is receive msg");
            if (voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    voiceBody.downloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                progressBar.setVisibility(View.VISIBLE);
                setMessageReceiveCallback();
            } else {
                progressBar.setVisibility(View.INVISIBLE);

            }
            return;
        }

        // until here, handle sending voice message
        handleSendMessage();
    }

    protected void handleSendMessage() {
        setMessageSendCallback();
        switch (message.status()) {
            case SUCCESS:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.INVISIBLE);
                break;
            case FAIL:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
            case INPROGRESS:
                progressBar.setVisibility(View.VISIBLE);
                if(percentageView != null){
                    percentageView.setVisibility(View.VISIBLE);
                    percentageView.setText(message.progress() + "%");
                }
                statusView.setVisibility(View.INVISIBLE);
                break;
            default:
                progressBar.setVisibility(View.INVISIBLE);
                if(percentageView != null)
                    percentageView.setVisibility(View.INVISIBLE);
                statusView.setVisibility(View.VISIBLE);
                break;
        }
    }




    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
        new EaseChatRowVoicePlayClickListener(message, voiceImageView, readStatusView, adapter, activity).onClick(bubbleLayout);
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EaseChatRowVoicePlayClickListener.currentPlayListener != null && EaseChatRowVoicePlayClickListener.isPlaying) {
            EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
        }
    }
    
}
