package com.minfo.quanmei.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minfo.quanmei.utils.ImageLoader;


/**
 * 基于单例的通用的viewHolder
 * @author liujing
 *
 */
public class BaseViewHolder {

	private SparseArray<View> views;
	private View convertView;
	private int position;

	private BaseViewHolder(Context context, ViewGroup parent, int LayoutId,
			int position) {
		this.views = new SparseArray<View>();
		convertView = LayoutInflater.from(context).inflate(LayoutId, parent,
				false);
		convertView.setTag(this);
		this.position = position;
	}

	public static BaseViewHolder get(Context context, View convertView,
			ViewGroup parent, int layoutId, int position) {

		BaseViewHolder holder = null;
		if (convertView == null)
		{
			holder = new BaseViewHolder(context, parent, layoutId, position);
		} else
		{
			holder = (BaseViewHolder) convertView.getTag();
			holder.position = position;
		}
		return holder;
	}

	public <T extends View> T getView(int viewId) {

		View view = views.get(viewId);
		if (view == null) {
			view = convertView.findViewById(viewId);
			views.put(viewId, view);
		}
		return (T) view;
	}

	public View getConvertView() {
		return convertView;
	}


	public BaseViewHolder setText(int viewId,String text){
		TextView tvView = getView(viewId);
		tvView.setText(text);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public BaseViewHolder setImageResource(int viewId, int drawableId)
	{
		ImageView view = getView(viewId);
		view.setImageResource(drawableId);

		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public BaseViewHolder setImageBitmap(int viewId, Bitmap bm)
	{
		ImageView view = getView(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 *
	 * @param viewId
	 * @param
	 * @return
	 */
	public BaseViewHolder setImageByUrl(int viewId, String url)
	{
		ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(url, (ImageView) getView(viewId));
		return this;
	}

	public int getPosition()
	{
		return position;
	}

}
