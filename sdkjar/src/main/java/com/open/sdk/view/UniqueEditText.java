package com.open.sdk.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.open.sdk.config.DetailedList;
import com.open.sdk.utlis.Shape;

/**
 * Created by Administrator on 2017/8/28.
 */
public class UniqueEditText extends RelativeLayout implements TextWatcher, OnFocusChangeListener, OnClickListener {
    private String hint = "请您输入";
    private TextView hintTxt;
    private TextView empty;
    private Context mContext;
    private AutoCompleteTextView autoCompleteTextView;
    private int defaultColor = 0xffcdcdcd;
    private int principalColor = 0xffffffff;
    private TextView more;
    public UniqueEditText(Context context) {
        super(context);
        init(context);
    }

    public UniqueEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UniqueEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        setPadding(DetailedList.d10, DetailedList.d2, DetailedList.d2, DetailedList.d4);
        setBackground(Shape.getShapeGray());
    }

    public void show() {
        setMinimumHeight(DetailedList.d40);
        setMinimumWidth(-1);
        initHintTxt();
        initAutoCompleteTextView();
        initEmpty();
    }

    private void initHintTxt() {
        if (hintTxt != null) {
            return;
        }
        hintTxt = new TextView(mContext);
        hintTxt.setTextColor(defaultColor);
        hintTxt.setText(hint);
        hintTxt.setTextSize(10);
        hintTxt.setId(19920101);
        hintTxt.setOnClickListener(this);
        hintTxt.setGravity(Gravity.CENTER_VERTICAL);
        hintTxt.setAlpha(0);
        addView(hintTxt, -1, DetailedList.d14);
    }

    private void initAutoCompleteTextView() {
        if (autoCompleteTextView != null) {
            return;
        }
        LayoutParams autoCompleteTextViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        autoCompleteTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        autoCompleteTextViewLayoutParams.rightMargin = DetailedList.d30;
        autoCompleteTextView = new AutoCompleteTextView(mContext);
        autoCompleteTextView.setPadding(0, 0, 0, 0);
        autoCompleteTextView.setMinHeight(DetailedList.d20);
        autoCompleteTextView.setTextSize(14);
        autoCompleteTextView.setTextColor(Color.BLACK);
        autoCompleteTextView.setBackgroundColor(0);
        autoCompleteTextView.setHint(hint);
        autoCompleteTextView.setHintTextColor(defaultColor);
        autoCompleteTextView.addTextChangedListener(this);
        autoCompleteTextView.setOnFocusChangeListener(this);
        autoCompleteTextView.setLayoutParams(autoCompleteTextViewLayoutParams);
        addView(autoCompleteTextView);
    }

    private void initEmpty() {
        if (empty != null) {
            return;
        }
        empty = new TextView(mContext);
        LayoutParams emptyPream = new LayoutParams(DetailedList.d30, DetailedList.d30);
        emptyPream.addRule(RelativeLayout.CENTER_VERTICAL);
        emptyPream.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        empty.setText("×");
        empty.setId(19920102);
        empty.setTextColor(Color.WHITE);
        empty.setTextSize(12);
        empty.setGravity(Gravity.CENTER);
        empty.setBackground(Shape.getEmptyShapeRed());
        empty.setVisibility(GONE);
        empty.setOnClickListener(this);
        addView(empty, emptyPream);
    }



    private void initMore() {
//        ﹀
        if (more != null) {
            return;
        }
        more = new TextView(mContext);
        LayoutParams emptyPream = new LayoutParams(DetailedList.d30, DetailedList.d30);
        emptyPream.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        emptyPream.addRule(RelativeLayout.CENTER_VERTICAL);
        more.setText("﹀");
        more.setId(19920103);
        more.setTextColor(Color.BLACK);
        more.setTextSize(12);
        more.setGravity(Gravity.CENTER);
        more.setVisibility(GONE);
        more.setOnClickListener(this);
        addView(empty, emptyPream);
    }

    public void setHintTxt(String hint) {
        this.hint = hint;
    }

    public void setHintColor(int defaultColor, int principalColor) {
        this.defaultColor = defaultColor;
        this.principalColor = principalColor;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        handlingTextChanged(charSequence.toString());
    }

    private void handlingTextChanged(String s) {
        if (s.isEmpty()) {
            empty.setVisibility(GONE);
            hintTxt.setAlpha(0);
            LayoutParams autoCompleteTextViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            autoCompleteTextViewLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            autoCompleteTextViewLayoutParams.rightMargin = DetailedList.d60;
            autoCompleteTextView.setLayoutParams(autoCompleteTextViewLayoutParams);
        } else {
            empty.setVisibility(VISIBLE);
            hintTxt.setAlpha(1);
            LayoutParams autoCompleteTextViewLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            autoCompleteTextViewLayoutParams.addRule(3, 19920101);
            autoCompleteTextViewLayoutParams.rightMargin = DetailedList.d60;
            autoCompleteTextView.setLayoutParams(autoCompleteTextViewLayoutParams);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            hintTxt.setTextColor(principalColor);
            autoCompleteTextView.setTextColor(principalColor);
            autoCompleteTextView.setHintTextColor(principalColor);
            setBackground(Shape.getShapeBlue());
        } else {
            hintTxt.setTextColor(defaultColor);
            autoCompleteTextView.setTextColor(defaultColor);
            autoCompleteTextView.setHintTextColor(defaultColor);
            setBackground(Shape.getShapeGray());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 19920101:
                autoCompleteTextView.requestFocus();
                break;
            case 19920102:
                autoCompleteTextView.setText("");
                autoCompleteTextView.requestFocus();
                break;
        }
    }
}
