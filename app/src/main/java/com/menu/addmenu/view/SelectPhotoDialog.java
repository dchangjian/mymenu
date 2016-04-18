package com.menu.addmenu.view;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.util.Util;

/**
 * 选择照片的对话框
 * 
 * @author Administrator
 * 
 */
public class SelectPhotoDialog extends Dialog implements
		android.view.View.OnClickListener {

	/**
	 * 对外的选择借口，当一个item被选选中时被调用
	 */
	private SelectListener mListener;

	private TextView tvTakePhoto;
	private TextView tvSelectPhoto;
	
	private Uri imageUri;

	public SelectPhotoDialog(Context context, int theme) {
		super(context, theme);
		config();
	}

	public void config() {
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		
		//dialogWindow.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
			//	WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		// 这句话意思是不要全屏
		dialogWindow.setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		dialogWindow.setGravity(Gravity.CENTER);
		// lp.alpha = 1f; // 透明度
		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// dialog.onWindowAttributesChanged(lp);
		dialogWindow.setAttributes(lp);
		this.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_photo_dialog);

		tvSelectPhoto = (TextView) findViewById(R.id.tv_select_photo_dialog_from_album);
		tvTakePhoto = (TextView) findViewById(R.id.tv_select_photo_dialog_take_photo);
		tvSelectPhoto.setOnClickListener(this);
		tvTakePhoto.setOnClickListener(this);
	}

	public void setSelectedListener(SelectListener listener) {
		this.mListener = listener;
	}

	public interface SelectListener {
		public void itemSelected(String text);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.tv_select_photo_dialog_take_photo:
			Util.showToast(getContext(), "aa");
			mListener.itemSelected("相机");
			break;
		case R.id.tv_select_photo_dialog_from_album:
			mListener.itemSelected("从相册选择");
			break;
		}
		this.dismiss();
	}
}
