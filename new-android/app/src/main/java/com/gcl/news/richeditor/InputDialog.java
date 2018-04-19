package com.gcl.news.richeditor;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gcl.news.R;

import org.jsoup.helper.StringUtil;

/**
 * Created by 游 on 2018/1/9.
 */

public class InputDialog {
    private ContentType type;//类型
    private Context context;
    private AlertDialog dialog;
    private TextView tvOk;
    private TextView tvCancle;
    private EditText etContent;
    private TextView tvTitle;

    public InputDialog(Context context) {
        this.context = context;
        initView();
    }

    public InputDialog(Context context, ContentType type) {
        this.type = type;
        this.context = context;
        initView();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_editor, null);
        tvCancle = view.findViewById(R.id.tv_dialog_editor_cancel);
        tvOk =  view.findViewById(R.id.tv_dialog_editor_ok);
        etContent = view.findViewById(R.id.et_dialog_editor_content);
        tvTitle = view.findViewById(R.id.et_dialog_editor_title);

        builder.setView(view);
        dialog = builder.create();
    }

    /**
     * 设置确定按钮
     *
     * @param ok
     * @param onClickListener
     */
    public void setPositiveButton(CharSequence ok, View.OnClickListener onClickListener) {
        tvOk.setText(ok);
        tvOk.setOnClickListener(onClickListener);
    }

    /**
     * 设置取消按钮
     *
     * @param cancle
     * @param onClickListener
     */
    public void setNegativeButton(String cancle, View.OnClickListener onClickListener) {
        tvCancle.setText(cancle);
        tvCancle.setOnClickListener(onClickListener);
    }

    /**
     * 获取输入的内容
     *
     * @return
     */
    public String getContent() {
        return etContent.getText().toString();
    }

    /**
     * 设置输入框的提示内容
     *
     * @param hint
     */
    public void setHint(CharSequence hint) {
        etContent.setHint(hint);
    }

    /**
     * 设置输入框的内容
     *
     * @param text
     */
    public void setText(CharSequence text) {
        etContent.setText(text);
        etContent.setSelection(text.length());
    }

    /**
     * 设置输入框显示的行数
     *
     * @param lines
     */
    public void setLines(int lines) {
        etContent.setLines(lines);
    }

    /**
     * 清空输入框的内容
     */
    public void clearText() {
        etContent.setText("");
    }

    /**
     * 显示对话框
     */
    public void show(ContentType type) {
        this.type = type;
        setting(type);
        dialog.show();
    }

    /**
     *  显示对话框，自带默认内容
     */
    public void show(ContentType type, String content) {
        this.type = type;
        setting(type);
        if(!StringUtil.isBlank(content)){
            etContent.setText(content);
        }
        dialog.show();
    }

    public ContentType getType() {
        return type;
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        dialog.dismiss();
    }

    private void setting(ContentType type){
        if(type == ContentType.TITLE){
            tvTitle.setText("插入小标题");
            this.setLines(3);
        }else if(type == ContentType.IMG){
            tvTitle.setText("请填入正确的图像链接");
            this.setLines(3);
        }else if(type == ContentType.CONTENT){
            this.setLines(10);
            tvTitle.setText("插入段落");
        }else if(type == ContentType.ARTICLE_TITLE){
            this.setLines(3);
            tvTitle.setText("填写文章标题");
        }else if(type == ContentType.ARTICLE_ABSTRACT){
            this.setLines(10);
            tvTitle.setText("填写文章摘要");
        }
    }
}
