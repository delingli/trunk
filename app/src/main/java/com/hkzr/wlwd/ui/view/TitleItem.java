/**  

 * @author seek 951882080@qq.com  

 * @version V1.0  

 */

package com.hkzr.wlwd.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class TitleItem {

	/**
	 * show the text if item drawable not empty
	 */
	private String itemStr;

	private Drawable itemDrawable;

	private int itemStrRes = -1;

	private int itemDrawableRes = -1;

	private OnClickListener listener;

	/**
	 * the view will be added to TitleLayout
	 */
	private View mItemView;

	/**
	 * default add type
	 */
	private ItemType itemType = ItemType.SINGLE;

	/**
	 * default add to parent's right
	 */
	private GravityType gravityType = GravityType.RIGHT;

	private Context context;

	private int viewId = -1;
	
	private ColorStateList  textColor;

	private float textSize = -1;
	

	public float getTextSize() {
		return textSize;
	}

	public TitleItem(Context context) {
		this.context = context;
	}

	public TitleItem(Context context, String itemStr, Drawable itemDrawable,
			OnClickListener listener) {
		super();
		this.itemStr = itemStr;
		this.itemDrawable = itemDrawable;
		this.listener = listener;
	}

	public TitleItem(Context context, String itemStr, int itemDrawableRes,
			OnClickListener listener) {
		super();
		this.itemStr = itemStr;
		this.itemDrawableRes = itemDrawableRes;
		this.listener = listener;
	}

	public TitleItem(Context context, int itemStrRes, int itemDrawableRes,
			OnClickListener listener) {
		super();
		this.itemStrRes = itemStrRes;
		this.itemDrawableRes = itemDrawableRes;
		this.listener = listener;
	}

	public TitleItem(Context context, int itemStrRes, Drawable itemDrawable,
			OnClickListener listener) {
		super();
		this.itemDrawable = itemDrawable;
		this.itemStrRes = itemStrRes;
		this.listener = listener;
	}

	public String getItemStr() {
		return itemStr;
	}

	public TitleItem setItemStr(String itemStr) {
		if (mItemView != null && mItemView instanceof TextView) {
			((TextView) mItemView).setText(itemStr);
		}
		this.itemStr = itemStr;
		return this;
	}

	public Drawable getItemDrawable() {
		return itemDrawable;
	}

	public TitleItem setItemDrawable(Drawable itemDrawable) {
		if (mItemView != null && mItemView instanceof ImageView) {
			((ImageView) mItemView).setImageDrawable(itemDrawable);
		}
		this.itemDrawable = itemDrawable;
		return this;
	}

	public int getItemStrRes() {
		return itemStrRes;
	}

	public TitleItem setItemStrRes(int itemStrRes) {
		if (mItemView != null && mItemView instanceof TextView) {
			((TextView) mItemView).setText(itemStrRes);
		}
		this.itemStrRes = itemStrRes;
		return this;
	}

	public int getItemDrawableRes() {
		return itemDrawableRes;
	}

	public TitleItem setItemDrawableRes(int itemDrawableRes) {
		if (mItemView != null && mItemView instanceof ImageView) {
			((ImageView) mItemView).setImageResource(itemDrawableRes);
		}
		this.itemDrawableRes = itemDrawableRes;
		return this;
	}

	public OnClickListener getListener() {
		return listener;
	}

	public TitleItem setListener(OnClickListener listener) {
		this.listener = listener;
		return this;
	}

	public View getItemView() {
		if (mItemView == null) {
			if (itemDrawableRes != -1 || itemDrawable != null) {
				ImageView mImageView = new ImageView(context);
				if (itemDrawable != null) {
					mImageView.setImageDrawable(itemDrawable);
				} else {
					mImageView.setImageResource(itemDrawableRes);
				}
				mItemView = mImageView;
			} else {
				TextView mTextView = new TextView(context);
				if (itemStrRes != -1) {
					mTextView.setText(itemStrRes);
				} else if(!TextUtils.isEmpty(itemStr)){
					mTextView.setText(itemStr);
				}
				mTextView.setTextColor(textColor!=null?textColor:ColorStateList.valueOf(0xFFFFFF));
				if (textSize!=-1) {
					mTextView.setTextSize(textSize);
				}
				mItemView = mTextView;
			}
			if (viewId != -1) {
				mItemView.setId(viewId);
			}
			if (listener!=null) {
				mItemView.setOnClickListener(listener);
			}
		}
		return mItemView;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public TitleItem setItemType(ItemType itemType) {
		this.itemType = itemType;
		return this;
	}

	public GravityType getGravityType() {
		return gravityType;
	}

	public TitleItem setGravityType(GravityType gravityType) {
		this.gravityType = gravityType;
		return this;
	}

	public int getViewId() {
		return viewId;
	}

	public TitleItem setViewId(int viewId) {
		this.viewId = viewId;
		return this;
	}

	public ColorStateList  getTextColor() {
		return textColor;
	}

	public TitleItem setTextColor(ColorStateList  color) {
		if (mItemView != null && mItemView instanceof TextView) {
			((TextView) mItemView).setTextColor(color);
		}
		this.textColor = color;
		return this;
	}
	
	public TitleItem setTextSize(float size) {
		if (mItemView != null && mItemView instanceof TextView) {
			((TextView) mItemView).setTextSize(size);
		}
		this.textSize = size;
		return this;
	}
	
	public enum ItemType {
		SINGLE, MULTIPLE
	}

	public enum GravityType {
		LEFT, RIGHT
	}

}
