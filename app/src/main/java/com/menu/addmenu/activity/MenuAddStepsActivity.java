package com.menu.addmenu.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.addmenu.adapter.AddSetpsAdapter;
import com.menu.addmenu.entity.Step;
import com.menu.addmenu.view.SelectPhotoDialog;
import com.menu.addmenu.view.SelectPhotoDialog.SelectListener;
import com.menu.dao.MenuDao;
import com.menu.model.Menu;
import com.menu.util.Util;
/**
 * 添加步骤的activity
 * @author Administrator
 *
 */
public class MenuAddStepsActivity extends Activity {

	public static final int TAKE_PHOTO = 1;
	public static final int SELECT_ALBUM = 2;
	//保存步骤
	public static final int SAVE_STEP = 3;
	//保存修改的菜单用料等
	public static final int SAVE_MENU = 4;
	
	public static final String PHOTO = "pictures";
	
	/**
	 * 显示步骤的GridView
	 */
	private GridView mGvSteps;
	/**
	 * 修改原料信息的layout
	 */
	private RelativeLayout mLayoutModifyMenu;
	/**
	 * 
	 */
	private TextView mTvMenuName;
	/**
	 *显示的对话框Dialog
	 */
	protected SelectPhotoDialog dialog;
	protected ImageView mShowPhoto;
	private Uri imageUri;
	/**
	 * 提示上传照片
	 */
	private TextView mTvPromptGivePhoto;
	
	/**
	 * 当前保存的steps
	 */
	private ArrayList<Step> stepList = new ArrayList<Step>();
	/**
	 * 步骤的适配器
	 */
	private ArrayAdapter<Step> stepAdapter;
	/**
	 * MenuAddActivity传入的Menu
	 */
	
	private TextView mTvSave;
	private String faceUrl;
	private Menu mMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_menu_steps);
		initData();
		initViews();
	}
	
	private void initData(){
		Intent i = getIntent();

		mMenu = (Menu) i.getSerializableExtra("menu");

		if(mMenu != null){
			if(mMenu.getSteps() != null){
				stepList = mMenu.getSteps();
			}
			if(mMenu.getFaceUrl() != null){
				faceUrl = mMenu.getFaceUrl();
			}

		}
	}

	private void initViews() {
		mGvSteps = (GridView) findViewById(R.id.gv_activity_add_menu_steps);
		
		mLayoutModifyMenu = (RelativeLayout) findViewById(R.id.layout_activity_add_menu_steps_edit_ingredient);
		mTvMenuName = (TextView) mLayoutModifyMenu.findViewById(R.id.tv_add_menu_setps_menu_name);
		mTvMenuName.setText(mMenu.getName());

		mShowPhoto = (ImageView) findViewById(R.id.iv_add_menu_steps_take_photo);
		mTvPromptGivePhoto = (TextView) findViewById(R.id.tv_activity_add_menu_steps_prompt_give_photo);

		if(faceUrl != null){
			mShowPhoto.setImageBitmap(BitmapFactory.decodeFile(faceUrl));
			mTvPromptGivePhoto.setVisibility(View.GONE);
		}
		mTvSave = (TextView) findViewById(R.id.tv_add_menu_setps_save);
		
		mLayoutModifyMenu = (RelativeLayout) findViewById(R.id.layout_activity_add_menu_steps_edit_ingredient);
		mShowPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog == null){
					SelectPhotoDialog dialog = new SelectPhotoDialog(
							MenuAddStepsActivity.this, R.style.mydialogstyle);
					MenuAddStepsActivity.this.dialog = dialog;
				}
				registerDialog();
				dialog.show();
			}
		});
		mGvSteps.setSelector(new ColorDrawable(Color.TRANSPARENT));
		stepAdapter = new AddSetpsAdapter(this, 0, stepList);
		mGvSteps.setAdapter(stepAdapter);
		
		//点击时返回菜单添加的Activity
		mLayoutModifyMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MenuAddStepsActivity.this, MenuAddActivity.class);
				i.putExtra("menu", mMenu);
				startActivityForResult(i,SAVE_MENU);
			}
		});
		
		//保存所有数据到数据库
		mTvSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveAllData();
				Util.showToast(MenuAddStepsActivity.this,"保存成功");
				finish();
			}
		});
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
		Intent intent = new Intent("android.intent.action.GET_CONTENT");
		intent.putExtra("return-data", true);
		intent.setType("image/*");
		intent.putExtra("scale", true);
		startActivityForResult(intent, SELECT_ALBUM);
	}
	
	//到这里保存菜谱的所有数据
	private void saveAllData(){
		
		MenuDao dao = new MenuDao(this);
		mMenu.setSteps(stepList);
		dao.save(mMenu);
	}
	
	//这块困扰了好久
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bitmap bitmap = null;
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case TAKE_PHOTO:
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(imageUri));

					//压缩图片
					bitmap = Util.reduce(bitmap,mShowPhoto.getWidth() , mShowPhoto.getHeight(), true);
					//将图片写入文件中
					saveFaceImage(bitmap);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
				mShowPhoto.setImageBitmap(bitmap);
				//照片显示成功后提示语消失
				mTvPromptGivePhoto.setVisibility(View.GONE);
				break;
			case SELECT_ALBUM:
				Uri uri = data.getData();
				try {
					bitmap = BitmapFactory.decodeStream(getContentResolver()
							.openInputStream(uri));
					bitmap = Util.reduce(bitmap,mShowPhoto.getWidth() , mShowPhoto.getHeight(), true);
					//将图片写入文件中
					saveFaceImage(bitmap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				mShowPhoto.setImageBitmap(bitmap);
				//照片显示成功后提示语消失
				mTvPromptGivePhoto.setVisibility(View.GONE);
				break;
				//保存步骤
				//因为序列化的原因，不可能是同一个对象，重写equals方法，会导致数据不一致
			case SAVE_STEP:
				Step step = (Step) data.getSerializableExtra("step");
				int i = 0;
				for( i = 0; i < stepList.size(); i++){
					if(stepList.get(i).equals(step)){
						stepList.set(i, step);
						break;
					}
				}
				if(i == stepList.size()){
					stepList.add(stepList.size() - 1, step);
				}
				stepAdapter.notifyDataSetChanged();
				break;
			//保存菜单
			case SAVE_MENU:
				mMenu = (Menu) data.getSerializableExtra("menu");
				break;
			}
		}
	}
	
	//保存封面照片
	private void saveFaceImage(Bitmap bitmap) throws Exception{
		File imageDir = new File(getFilesDir(),"images/faceImage");
		if(!imageDir.exists()){
			imageDir.mkdirs();
		}
		File imageFile = null;
		if(mMenu.getFaceUrl() == null){
			String name = UUID.randomUUID().toString();
			 imageFile = new File(imageDir, name+".png");
			faceUrl = imageFile.getAbsolutePath();
			mMenu.setFaceUrl(faceUrl);
		}
		else{
			 imageFile = new File(faceUrl);
			if(imageFile.exists()){
				imageFile.delete();
				imageFile.createNewFile();
			}
		}
		Util.bitmap2File(bitmap, imageFile);
		Util.showToast(this, faceUrl);
	}
}
