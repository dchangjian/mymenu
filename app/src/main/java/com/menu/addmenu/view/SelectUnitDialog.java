package com.menu.addmenu.view;

import java.util.zip.Inflater;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.menu.activity.R;
import com.menu.util.Util;
/**
 * 选择单位的对话框
 * @author Administrator
 *
 */
public class SelectUnitDialog extends Dialog{
	
	private String mTitle;
	private ListView mLvUnit;
	private Context mContext;
	
	private boolean noTitle;
	/**
	 * 对外的选择借口，当一个item被选选中时被调用
	 */
	private SelectListener mListener;

	//持有对话框对应的那个item布局的引用
	//设置view重用对话框,包含用料单位和制作时间
	private View mView;
	
	private String[] items;
	public SelectUnitDialog(Context context,int theme,float width,float height,String[] items) {
		super(context,theme); 
		this.mContext = context;
		this.items = items;
		config( width, height);
	}
	/**
	 * 对外方法，设置标题栏是否显示
	 * @param val
	 */
	public  void setNoTitle(boolean val){
		this.noTitle = val;
	}
	public void config(float width, float height){
		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高
		// lp.x = 100; // 新位置X坐标
		// lp.y = 100; // 新位置Y坐标
		lp.width = (int) (d.widthPixels * width); // 宽度
		
	    lp.height =(int) (d.heightPixels * height); // 高度
		
		//lp.alpha = 1f; // 透明度
		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// dialog.onWindowAttributesChanged(lp);
		dialogWindow.setAttributes(lp);
		//dialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_dialog);
		
		mLvUnit = (ListView) findViewById(R.id.lv_add_menu_unit);
		
		mLvUnit.setAdapter(new ArrayAdapter<>(mContext, R.layout.alert_dialog_item,items));
	
		if(noTitle){
			TextView tv = (TextView) findViewById(R.id.tv_select_unit_dialog_title);
			View divideLine = findViewById(R.id.view_select_unit_dialog_divide_line);
			tv.setVisibility(View.GONE);
			divideLine.setVisibility(View.GONE);
		}
		
		mLvUnit.setOnItemClickListener(new OnItemClickListener() {
		
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//在这里变化单位
				String text = items[position];
				mListener.itemSelected(text);
				dismiss();
			}
		});
	
		
		if(!noTitle){
			
			TextView tv = (TextView) findViewById(R.id.tv_select_unit_dialog_title);
			View divideLine = findViewById(R.id.view_select_unit_dialog_divide_line);
//			tv.measure(0, 0);
//			divideLine.measure(0, 0);
			
		}
		
		//config(0.8f, totalHeight);
	}
	public void setSelectedListener(SelectListener listener){
		this.mListener = listener;
	}
	
	public interface SelectListener{
		public void itemSelected(String text);
	}
	
	class MyAdapter extends ArrayAdapter<String>{

		public MyAdapter(Context context, int resource,String[] items) {
			super(context,0);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(mContext).inflate(R.layout.alert_dialog_item, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.tv_add_menu_unit);
			tv.setText(items[position]);
			return convertView;
		}
	}
}


