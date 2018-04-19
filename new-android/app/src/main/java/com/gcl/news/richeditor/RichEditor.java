package com.gcl.news.richeditor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.activity.ImageSelectActivity;
import com.gcl.news.bean.NewsDraft;
import com.gcl.news.db.DatabaseHelper;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;
import com.j256.ormlite.dao.Dao;
import com.squareup.picasso.Picasso;

import org.jsoup.helper.StringUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by 游 on 2018/1/9.
 */

public class RichEditor extends RelativeLayout {
    private static final String TAG = "MyRichEditor";//tag
    private static int contentSize = 20;//内容字体大小
    private static int contentColor = R.color.contentColor;//内容字体颜色
    private static int titleSize = 23;//标题字体大小
    private static int titleColor = R.color.titleColor;//标题字体颜色
    private ImageView tvInsertImg;//插入图片按钮
    private ImageView tvInsertContent;//插入内容按钮
    private ImageView tvInsertTitle;//插入标题按钮
    private ImageView tvInsertLine;//插入分割线
    private ImageView tvSave;//保存按钮
    private LinearLayout editor;//编辑器面板-----思想就是添加addview子控件
    private Context context;//上下文对象
    private InputDialog dialog;//内容、标题输入框
    private StringBuilder html;//生成的html文件
    private String htmlTitle = "";//html的标题
    private List<EditorBean> editorList = new CopyOnWriteArrayList<>();//内容列表[并发容器 防止异常(用arraylist可能会异常)]
    private String titleTypeStr = "h4";//标题类型的字符串

    private int SELECT_IMAGE_CODE = 0;

    private Dao<NewsDraft, Integer> newsDraftDao; // news dao 用来保存html

    private String articleTitle; // 文章标题
    private String articleAbstract; // 文章摘要

    private int currentArticleId = -1; // 当前编辑的文章id，如果为-1，表示文章没有保持在数据库里
    private boolean isModify = false; // 是否修改过

    public RichEditor(Context context) {
        this(context, null);
    }

    public RichEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        newsDraftDao = DatabaseHelper.getHelper(context).getNewsDraftsDao();

        initView();//初始化视图
        initInputDialog();//初始化输入对话框
        initListener();//初始化监听器
    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_custom_eidt, this);
        editor = view.findViewById(R.id.et_custom_editor);
        tvInsertContent = view.findViewById(R.id.tv_custom_edit_insert_content);
        tvInsertTitle = view.findViewById(R.id.tv_custom_edit_insert_title);
        tvInsertImg = view.findViewById(R.id.tv_custom_edit_insert_img);
        tvSave = view.findViewById(R.id.tv_custom_edit_save);
        tvInsertLine = view.findViewById(R.id.tv_custom_edit_insert_line);
    }
    private void initListener() {
        tvInsertImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageSelectActivity.class);
                ((Activity)context).startActivityForResult(intent, SELECT_IMAGE_CODE);
            }
        });
        tvInsertContent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.show(ContentType.CONTENT);//弹出输入内容的对话框
            }
        });
        tvInsertTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(ContentType.TITLE);//弹出输入标题的对话框
            }
        });
        tvInsertLine.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 修改
                isModify = true;

                // 直接插入分割线
                View view = new View(context);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT, 4);
                lp2.setMargins(0, 10, 0, 10);
                view.setLayoutParams(lp2);
                view.setBackgroundColor(Color.parseColor("#555555"));

                editor.addView(view);//添加到编辑器视图中

                final long tag = System.currentTimeMillis();
                editorList.add(new EditorBean(ContentType.LINE, null, tag));//添加到编辑器列表中
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("删除");
                        builder.setIcon(R.drawable.delete);
                        builder.setMessage("删除分隔符吗?");
                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                editor.removeView(view);
                                removeEditorBeanByTag(tag);
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                });
            }
        });
        tvSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtil.isBlank(articleTitle) || StringUtil.isBlank(articleAbstract)){
                    Toast.makeText(context, "填写文章标题或摘要", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("保存");
                builder.setIcon(R.drawable.save);
                builder.setMessage("保存为草稿？");
                builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewsDraft newsDraft = new NewsDraft();
                        newsDraft.setTitle(articleTitle);
                        newsDraft.setAbstracts(articleAbstract);
                        newsDraft.setContent(getHtmlStr());
                        newsDraft.setUserId(RuntimeObject.user.getId());
                        try {
                            if(currentArticleId != -1){ // 如果数据库中有则更新新闻
                                newsDraft.setId(currentArticleId);
                                newsDraftDao.update(newsDraft);
                            }else{
                                currentArticleId = newsDraftDao.create(newsDraft);
                            }

                            Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                            isModify = false;
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });
    }
    /**
     * 初始化输入对话框
     */
    private void initInputDialog() {
        dialog = new InputDialog(context);
        dialog.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dialog.getType()) {
                    case CONTENT:
                        insertContent(contentSize, contentColor, ContentType.CONTENT);
                        break;
                    case TITLE:
                        insertContent(titleSize, titleColor, ContentType.TITLE);
                        break;
                }
                dialog.dismiss();
                dialog.clearText();
            }
        });
        dialog.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.clearText();
            }
        });
    }

    /**
     * 创建/拼接html字符串
     */
    public String createHtmlStr() {
        html = new StringBuilder("");//String拼接不出长字符，用可变长度的String
        //根据编辑器列表中的内容拼接相应的标签
        for (EditorBean editorBean : editorList) {
            switch (editorBean.getType()) {
                case CONTENT:
                    html.append("<p>" + editorBean.getContent() + "</p>\n");//拼接内容，自动空两格
                    break;
                case TITLE:
                    html.append("<" + titleTypeStr + ">" + editorBean.getContent().replace("\n", "<br/>") + "</" + titleTypeStr + ">\n");//拼接标题---->默认h3
                    break;
                case IMG:
                    html.append("<center><img width=\"100%\"  src='" + editorBean.getContent().replace("\n", "<br/>") + "' /></center>\n");//插入图片，图片的路径（服务器url）由调用这指定，图片名自动生成
                    break;
                case LINE:
                    html.append("<hr/>");
            }
        }
        return html.toString();
    }

    /**
     * 移除对象---按tag
     *
     * @param tag
     */
    private void removeEditorBeanByTag(long tag) {
        for (EditorBean editorBean : editorList) {
            if (editorBean.getTag() == tag) {
                editorList.remove(editorBean);
                break;
            }
        }
    }

    /**
     * 插入内容,标题
     *
     * @param size
     * @param color
     * @param type
     */
    private void insertContent(int size, final int color, final ContentType type) {
        // 修改
        isModify = true;

        final long tag = System.currentTimeMillis();//使用当前的时间做标记----标记的作用就是要知道哪条是哪条,删除的时候好操作
        final TextView tvContent = new TextView(context);
        /**
         *初始化修改对话框--------之所以写在这里 是因为要对象序列化--局部有效原则----如果弄成全局的,那么tvContent永远是最新一个--删除就会出错哦
         */
        final InputDialog updateDialog = new InputDialog(context, type);
        updateDialog.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
                updateDialog.clearText();
            }
        });
        updateDialog.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = updateDialog.getContent();
                updateDialog.clearText();
                updateDialog.dismiss();
                if (TextUtils.isEmpty(content)) {
                    editor.removeView(tvContent);
                    removeEditorBeanByTag(tag);
                    return;
                }
                tvContent.setText(content);
                for (EditorBean editorBean : editorList) {
                    if (editorBean.getTag() == tag) {
                        editorBean.setContent(content);
                        break;
                    }
                }
            }
        });

        tvContent.setTextSize(size);
        tvContent.setTextColor(color);
        /**
         * 单击就修改
         */
        tvContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.show(type);
                updateDialog.setText(tvContent.getText().toString().replace("    ", ""));
            }
        });
        /**
         * 长按就删除
         */
        tvContent.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("删除这段文字吗?");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.removeView(tvContent);
                        removeEditorBeanByTag(tag);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
                return true;
            }
        });

        if(type == ContentType.TITLE){
            tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        String contentStr = dialog.getContent();
        tvContent.setText(contentStr);
        editor.addView(tvContent);//添加到编辑器视图中
        editorList.add(new EditorBean(type, contentStr, tag));//添加到编辑器列表中
    }

    public void insertImage(String name){
        if (name == null) return;

        final long tag = System.currentTimeMillis();
        final ImageView img = new ImageView(context);
        final String url = Properties.BASE_IMG_URL+"/"+name;
        Picasso.with(context)
                .load(url)
                .placeholder(R.drawable.image)
                .error(R.drawable.ic_launcher_background)
                .into(img);
        /**
         * 长按就删除
         */
        img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除");
                builder.setIcon(R.drawable.delete);
                builder.setMessage("删除这张图片吗?");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.removeView(img);
                        removeEditorBeanByTag(tag);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create().show();
            }
        });
        editor.addView(img);//添加到编辑器视图中
        editorList.add(new EditorBean(ContentType.IMG, url, tag));//添加到编辑器列表中
    }

    /**
     * 获取生成的html内容
     *
     * @return
     */
    public String getHtmlStr() {
        return createHtmlStr();
    }

    /**
     * 设置html的标题
     *
     * @param htmlTitle
     */
    public void setHtmlTitle(String htmlTitle) {
        this.htmlTitle = htmlTitle;
    }

    public void setArticleTitle(String content) {
        this.articleTitle = content;
    }

    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    /**
     * 是否修改
     * @return
     */
    public boolean getModifyState(){
        return isModify;
    }
}
