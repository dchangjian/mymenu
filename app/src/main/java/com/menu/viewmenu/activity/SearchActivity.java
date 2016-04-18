package com.menu.viewmenu.activity;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.menu.activity.R;

/**
 * Created by Administrator on 2016/4/1 0001.
 */
public class SearchActivity extends Activity {

    private EditText mTxt;
    private ImageView mIvClear;
    private TextView mTvSearch;

    public SearchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
    }

    private void initViews(){
        mTxt = (EditText) findViewById(R.id.txt_activity_search);
        mTvSearch = (TextView) findViewById(R.id.tv_search);
        mIvClear = (ImageView) findViewById(R.id.search_iv_delete);

        mIvClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTxt.setText("");
                mIvClear.setVisibility(View.GONE);
            }
        });

        mTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ("".equals(s.toString())) {
                    mIvClear.setVisibility(View.INVISIBLE);
                } else {
                    mIvClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mTxt.getText().toString();
            }
        });

    }




}
