package com.menu.viewmenu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.addmenu.activity.MenuAddStepsActivity;
import com.menu.addmenu.entity.Step;
import com.menu.dao.MenuDao;
import com.menu.model.Menu;
import com.menu.reptile.Reptile;
import com.menu.util.Util;
import com.menu.viewmenu.adapter.IngredientAdapter;
import com.menu.viewmenu.adapter.StepAdapter;
import com.menu.viewmenu.view.MyProgressDialog;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import in.srain.cube.image.CubeImageView;
import in.srain.cube.image.ImageLoader;
import in.srain.cube.image.ImageLoaderFactory;
public class ViewMenuActivity extends Activity {

    public static final String TAG = "ViewMenuActivity";
    //区分是保存还是修改按钮
    public static final int VIEW_NET = 1;
    public static final int VIEW_LOCAL = 2;

    //
    public static final int SAVE = 3;
    public static final int TIME_OUT = 4;
    public static final int FAIL = 5;

    private ListView mLvMainIngredient;
    private ListView mLvAssistIngredient;
    private ListView mLvSteps;

    private ImageView mIvBack;

    private CubeImageView mIvFace;

    private TextView mTvMenuName;
    private ImageLoader imageLoader;

    //显示的数据信息
    private Menu mMenu;
    private Button mOperButton;

    private ProgressBar mProgressBar;
    private View mContentView;

    private int netOrLocal;

    private  BaseHandler mHandler;

    private MyProgressDialog saveDialog;

    /**
        记录保存的图片
     */
    private Bitmap[] bitmaps;

    private void initData() {
        mMenu = (Menu) getIntent().getSerializableExtra("menu");

        netOrLocal = getIntent().getIntExtra("difference", 0);

        mHandler = new BaseHandler(this);

        new MyTask().execute(mMenu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_memu);
        mContentView = findViewById(R.id.layout_content);
        mContentView.setVisibility(View.INVISIBLE);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.loading_progress); // 加载动画
        mProgressBar.startAnimation(animation);

        imageLoader = ImageLoaderFactory.create(this);

        initData();

        mIvBack = (ImageView) findViewById(R.id.iv_activity_view_menu_back);
        mIvFace = (CubeImageView) findViewById(R.id.iv_activity_view_menu_face_photo);

        mLvMainIngredient = (ListView) findViewById(R.id.lv_activity_view_menu_main_ingredient);
        mLvAssistIngredient = (ListView) findViewById(R.id.lv_activity_view_menu_assistant_ingredient);
        mLvSteps = (ListView) findViewById(R.id.lv_activity_view_menu_steps);

//        mLvMainIngredient.setAdapter(new IngredientAdapter(this, mMenu.getMainIngredient()));
//        mLvAssistIngredient.setAdapter(new IngredientAdapter(this, mMenu.getAssistantIngredient()));
//        mLvSetps.setAdapter(new StepAdapter(this, mMenu.getSteps()));

        mTvMenuName = (TextView) findViewById(R.id.tv_activity_view_menu_name);
//        tvMenuName.setText(mMenu.getName());
//
//        mIvFace.setImageBitmap(BitmapFactory.decodeFile(mMenu.getFaceUrl()));

        mOperButton = (Button) findViewById(R.id.btn_activity_view_menu_update_menu);

        mIvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mOperButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入到修改页面
                if (netOrLocal == VIEW_LOCAL) {
                    Intent i = new Intent(ViewMenuActivity.this, MenuAddStepsActivity.class);

                    i.putExtra("menu", mMenu);

                    startActivity(i);
                    finish();
                }
                //将菜谱保存到本地
                else {
                    saveDialog = new MyProgressDialog(ViewMenuActivity.this,R.style.loading_dialog);
                    saveDialog.show();
                    new SaveThread().start();
                }
            }
        });

    }

    Runnable networkTask = new Runnable() {

        @Override
        public void run() {

            Reptile reptile = new Reptile();
            mMenu = reptile.getMenuAllInfo(mMenu);

        }
    };

    class MyTask extends AsyncTask<Menu, Void, Menu> {

        @Override
        protected Menu doInBackground(Menu... params) {

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (netOrLocal == VIEW_NET) {
                Reptile reptile = new Reptile();
                return reptile.getMenuAllInfo(params[0]);
            } else if (netOrLocal == VIEW_LOCAL) {
                String menuId = params[0].getId();
                Menu menu = params[0];
                MenuDao dao = new MenuDao(ViewMenuActivity.this);
                menu.setSteps(dao.getSteps(menuId));
                menu.setAssistantIngredient(dao.getIngredient(menuId, MenuDao.ASSISTANT_INGREDIENT_TYPE));
                menu.setMainIngredient(dao.getIngredient(menuId, MenuDao.MAIN_INGREDIENT_TYPE));

                return menu;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Menu menu) {

            mLvMainIngredient.setAdapter(new IngredientAdapter(ViewMenuActivity.this, mMenu.getMainIngredient()));
            mLvAssistIngredient.setAdapter(new IngredientAdapter(ViewMenuActivity.this, mMenu.getAssistantIngredient()));
            mLvSteps.setAdapter(new StepAdapter(ViewMenuActivity.this, mMenu.getSteps()));

            mTvMenuName.setText(mMenu.getName());

            if (netOrLocal == VIEW_LOCAL) {
                mIvFace.setImageBitmap(BitmapFactory.decodeFile(mMenu.getFaceUrl()));
                mOperButton.setText("修改这道菜");
            } else if (netOrLocal == VIEW_NET) {
                mIvFace.loadImage(imageLoader, mMenu.getFaceUrl());
                mOperButton.setText("保存这道菜");
            }
            //先清除动画
            mProgressBar.clearAnimation();
            mProgressBar.setVisibility(View.GONE);
            mContentView.setVisibility(View.VISIBLE);

        }
    }

    private void saveMenuFromNetwork() {
        StepAdapter stepAdapter = (StepAdapter) mLvSteps.getAdapter();

        int stepCount = stepAdapter.getCount();
        for (int i = 0; i < stepCount; i++) {

            Bitmap bitmap = bitmaps[i];
            String path = saveImage(bitmap, "images/stepImage", UUID.randomUUID().toString());
            mMenu.getSteps().get(i).setUrl(path);
        }

        String faceUrl = mMenu.getFaceUrl();
        String name = faceUrl.substring(faceUrl.lastIndexOf("/") + 1, faceUrl.lastIndexOf("."));

        mIvFace.setDrawingCacheEnabled(true);
        Bitmap bitmap = mIvFace.getDrawingCache();
        String path = saveImage(bitmap, "images/faceImage", name);
        mMenu.setFaceUrl(path);
        MenuDao dao = new MenuDao(this);
        dao.addMenu(mMenu);
    }

    private String saveImage(Bitmap bmp, String folder, String name) {
        File imageDir = new File(getFilesDir(), folder);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        File imageFile = null;

        imageFile = new File(imageDir, name + ".png");
        if (imageFile.exists()) {
            imageFile.delete();
            try {
                imageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Util.bitmap2File(bmp, imageFile);
        return imageFile.getAbsolutePath();
    }

    public Bitmap getBitmap(String imageUrl) {
        Bitmap mBitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream is = conn.getInputStream();
            mBitmap = BitmapFactory.decodeStream(is);

        } catch (Exception e) {
            return null;
        }
        return mBitmap;
    }

    class SaveThread extends Thread {
        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            long endTime;
            Message message = new Message();

            StepAdapter stepAdapter = (StepAdapter) mLvSteps.getAdapter();
            int stepCount = stepAdapter.getCount();
            bitmaps = new Bitmap[stepCount];

            for(int i = 0; i < bitmaps.length; i++){

                endTime = System.currentTimeMillis();

                if (endTime - startTime >= 10 * 1000) {
                    message.what = TIME_OUT;
                }

                Step step = stepAdapter.getItem(i);
                String url = step.getUrl();
                Bitmap bmp = getBitmap(url);
                if(null == bmp){
                    message.what = FAIL;
                    mHandler.sendMessage(message);
                    return;
                }
                bitmaps[i] = bmp;
            }

            message.what = SAVE;
            mHandler.sendMessage(message);
        }
    }

    class BaseHandler extends Handler{

        protected WeakReference<Activity> activityReference;
        public BaseHandler(Activity activity) {
            activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            final Activity activity = activityReference.get();

            if (activity == null) {
               return;
            }
            switch (msg.what) {
                case SAVE:
                    saveMenuFromNetwork();
                    saveDialog.dismiss();
                    Util.showToast(ViewMenuActivity.this, "保存成功");
                    finish();
                    break;
                case TIME_OUT:
                    Util.showToast(ViewMenuActivity.this, "保存失败");
                    finish();
                    break;
                default:
                    finish();
                    break;
            }
        }
    }
}









