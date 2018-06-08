package com.gcl.news.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gcl.news.R;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment {

    private CircleImageView avatar;
    private View draft;
    private View addArticle;
    private View imgManager;
    private View feedback;
    private View setting;
    private View upload;
    private TextView username;
    private Button logout;

    public MineFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        avatar = view.findViewById(R.id.user_avatar);
        draft = view.findViewById(R.id.mine_draft);
        addArticle = view.findViewById(R.id.mine_edit);
        imgManager = view.findViewById(R.id.mine_img);
        feedback = view.findViewById(R.id.mine_feedback);
        setting = view.findViewById(R.id.mine_setting);
        upload = view.findViewById(R.id.mine_upload);
        username = view.findViewById(R.id.user_name);
        logout = view.findViewById(R.id.mine_logout);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangeInfoActivity.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request.Builder()
                        .url(Properties.BASE_USER_URL + "/logout")
                        .get().build();
                RuntimeObject.client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        startActivity(new Intent(getContext(), LoginActivity.class));
                        getActivity().finish();
                    }
                });
            }
        });

        username.setText(RuntimeObject.user.getName());
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ChangeInfoActivity.class));
            }
        });

        // 草稿
        draft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewsListActivity.class));
            }
        });

        // 编辑文章
        addArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewsEditorActivity.class));
            }
        });

        // 图片管理
        imgManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ImageManagementActivity.class));
            }
        });

        // 反馈
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FeedbackActivity.class));
            }
        });

        // 设置
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SettingActivity.class));
            }
        });

        // 我的发布
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadActivity.class));
            }
        });

        return view;
    }

}
