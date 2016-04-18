package com.menu.addmenu.activity;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.addmenu.entity.Step;
import com.menu.addmenu.view.SelectPhotoDialog;
import com.menu.addmenu.view.SelectPhotoDialog.SelectListener;
import com.menu.util.Util;

public class MenuAddStep extends Activity {

	public static final int TAKE_PHOTO = 1;
	public static final int SELECT_ALBUM = 2;
	private ImageView mShowPhoto;
	private Uri imageUri;
	// 当前Acvivity的图片url
	private String url;
	// 持有当前Avtivity的信息
	private Step step;

	private SelectPhotoDialog dialog;
	/**
	 * 填写说明信息
	 */
	private EditText mTxtDesc;
	/**
	 * 确定
	 */
	private TextView mTvOk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_menu_step);
		initData();
		initViews();
	}

	private void initData() {

		Intent i = getIntent();
		step = (Step) i.getSerializableExtra("step");
		if (step != null) {
			url = step.getUrl();
		}
	}

	private void initViews() {
		mTxtDesc = (EditText) findViewById(R.id.txt_add_menu_step_desc);
		mShowPhoto = (ImageView) findViewById(R.id.iv_add_menu_steps_take_photo);
		mTvOk = (TextView) findViewById(R.id.tv_add_menu_setp_ok);
		mShowPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectPhotoDialog dialog = new SelectPhotoDialog(
						MenuAddStep.this, R.style.mydialogstyle);
				MenuAddStep.this.dialog = dialog;
				registerDialog();
				dialog.show();
			}
		});

		mTvOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTvOk.getText().equals("")) {
					Util.showToast(MenuAddStep.this, "描述不能为空");
					return;
				}
				if (step == null) {
					step = new Step();
				}
				Intent i = new Intent();
				step.setUrl(url);
				step.setDesc(mTxtDesc.getText().toString());
				i.putExtra("step", step);
				setResult(RESULT_OK, i);
				finish();
			}
		});
		
		if(url != null){
			mShowPhoto.setImageBitmap(loadImage());
		}
		if(step != null)
		mTxtDesc.setText(step.getDesc());
	}

	// 注册对话框，完成回调
	protected void registerDialog() {
		if (dialog != null) {
			dialog.setSelectedListener(new SelectListener() {
				@Override
				public void itemSelected(String text) {
					if (text.equals("相机")) {
						openCamera();
					} else if (text.equals("从相册选择")) {
						selectAlbum();
					}
				}
			});
		}
	}

	private void openCamera() {
		File outputImage = new File(Environment.getExternalStorageDirectory(),
				"output_image.jpg");
		try {
			if (outputImage.exists()) {
				outputImage.delete();
			}
			outputImage.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		imageUri = Uri.fromFile(outputImage);
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		startActivityForResult(intent, TAKE_PHOTO); // 启动相机程序
	}

	private void selectAlbum() {
		// 创建File 对象，用于存储选择的照片

		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.putExtra("return-data", true);
		intent.setType("image/*");
		intent.putExtra("scale", true);
		startActivityForResult(intent, SELECT_ALBUM);
	}

	// 这块困扰了好久
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			Bitmap bitmap = null;
			switch (requestCode) {
			case TAKE_PHOTO:
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(imageUri));

					// 压缩图片
					bitmap = Util.reduce(bitmap, mShowPhoto.getWidth(),
							mShowPhoto.getHeight(), true);
					// 将图片写入文件中
					saveFaceImage(bitmap);
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				mShowPhoto.setImageBitmap(bitmap);
				break;
			case SELECT_ALBUM:
				Uri uri = data.getData();
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uri));
					bitmap = Util.reduce(bitmap, mShowPhoto.getWidth(),
							mShowPhoto.getHeight(), true);
					// 将图片写入文件中
					saveFaceImage(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mShowPhoto.setImageBitmap(bitmap);
				break;
			}
		}
	}

	// 保存步骤照片
	private void saveFaceImage(Bitmap bitmap) throws Exception {
		File imageDir = new File(getFilesDir(), "images/stepImage");
		if (!imageDir.exists()) {
			imageDir.mkdirs();
		}
		File imageFile = null;
		if (url == null) {
			String name = UUID.randomUUID().toString();
			imageFile = new File(imageDir, name + ".png");
			url = imageFile.getAbsolutePath();
		} else {
			imageFile = new File(url);
			if (imageFile.exists()) {
				imageFile.delete();
				imageFile.createNewFile();
			}
		}
		Util.bitmap2File(bitmap, imageFile);
	}
	
	private Bitmap loadImage(){
		return BitmapFactory.decodeFile(url);
	}

}
