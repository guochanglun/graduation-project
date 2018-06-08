package com.gcl.news.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLClassLoader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadImageActivity extends AppCompatActivity {

    private ImageView iv_img;
    private Button btnUpload;
    private Button btnImage;
    private static final int PHOTO_REQUEST_GALLERY = 1;// 从图库获取图片

    // 存储当前选择的图片
    private Bitmap bitmap = null;

    // client
    private OkHttpClient client = RuntimeObject.client;

    private static final String TAG = "UploadImageActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("上传图片");

        iv_img = findViewById(R.id.iv_img);
        btnUpload = findViewById(R.id.bt_upload);
        btnImage = findViewById(R.id.bt_xiangce);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnImage.setEnabled(false);
                btnUpload.setEnabled(false);
                MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                if (bitmap != null) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    builder.addFormDataPart("file", "img", RequestBody.create(MEDIA_TYPE_PNG, byteArray))
                            .addFormDataPart("imgDesc", "这是图片描述");
                    MultipartBody requestBody = builder.build();
                    //构建请求
                    Request request = new Request.Builder()
                            .addHeader("X-UA", "android")
                            .url(Properties.BASE_IMG_URL)
                            .post(requestBody)
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            UploadImageActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(
                                            UploadImageActivity.this,
                                            "上传失败",
                                            Toast.LENGTH_SHORT).show();
                                    btnImage.setEnabled(true);
                                    btnUpload.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            UploadImageActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(
                                            UploadImageActivity.this,
                                            "上传成功",
                                            Toast.LENGTH_SHORT).show();
                                    btnImage.setEnabled(true);
                                    btnUpload.setEnabled(true);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ContentResolver resolver = getContentResolver();
        if (data != null) {
            Uri originalUri = data.getData(); // 获得图片的uri
            try {
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                iv_img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
