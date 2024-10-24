package comp4342.grp15.gem.ui.upload;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import comp4342.grp15.gem.MainActivity;
import comp4342.grp15.gem.databinding.FragmentUploadBinding;
import comp4342.grp15.gem.model.ResponseMessage;
import comp4342.grp15.gem.model.ServerPostMeta;


public class UploadFragment extends Fragment {

    private FragmentUploadBinding binding;
    private ImageView photoView;
    private Uri imgUri;
    private Bitmap bitmap;
    private EditText messageEditText;
    private TextView locationTextView;
    private String location = "null";
    private String username = "null";
    private String identifier = "null";
    private MainActivity mainActivity;

    private RequestQueue requestQueue;
    private ResponseMessage responseMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentUploadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        requestQueue = Volley.newRequestQueue(getContext());

        // 绑定对象
        photoView = binding.uploadPhotoImage;
        photoView.setVisibility(View.VISIBLE);
        messageEditText = binding.uploadMessageMuitedittext;
        locationTextView = binding.uploadLocationText;

        // 获取mainActivity
        mainActivity = (MainActivity) getActivity();

        // 从数据库获取用户信息
        Cursor cursor = mainActivity.getReadableDatabase().rawQuery("select * from user where id == 1", null);
        while (cursor.moveToNext()) {
            if (!cursor.getString(1).equals("null")) {
                // 已经登入
                username = cursor.getString(1);
                identifier = cursor.getString(3);
            } else {
                // 没有登入
                Toast.makeText(getContext(), "Please login first!", Toast.LENGTH_SHORT).show();
            }
        }
        cursor.close();

        // 监听图片按钮
        photoView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainActivity.checkPermissionAllGranted(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                })) {
                    dialog();
                } else {
                    mainActivity.firstRequestPermissions();
                }
            }
        });

        // 监听POST按钮
        Button postButton = binding.uploadPostButton;
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUpload();
            }
        });

        // 设置用户位置
        cursor = ((MainActivity) getActivity()).getReadableDatabase().rawQuery("select * from user where id == 1", null);
        while (cursor.moveToNext()) {
            if (Objects.equals(cursor.getString(4), "null") || Objects.equals(cursor.getString(5), "null")) {
                // 无定位
                Toast.makeText(mainActivity, "Please open location service!", Toast.LENGTH_SHORT).show();
            } else {
                // 有定位
                location = "[" + cursor.getString(4) + "," + cursor.getString(5) + "]";
                locationTextView.setText("Location: " + location);
            }
        }
        cursor.close();
        parseLocation();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void dialog(){
        String[] choices = {"From Album", "From Camera"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Choose")
                .setItems(choices, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            //选择照片按钮
                            toPicture();
                        }else if (which == 1){
                            //已经有权限，打开相机
                            toCamera();
                        }
                    }
                });
        builder.create();
        builder.show();
    }

    // 跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
        intent.setType("image/*");
        startActivityForResult(intent, 200);
        Log.i(TAG, "跳转相册成功");
    }

    // 跳转相机
    private void toCamera() {
        //创建File对象，用于存储拍照后的图片
        File outputImage = new File(getActivity().getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        } else {
            try {
                outputImage.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //判断SDK版本高低，ImageUri方法不同
        if (Build.VERSION.SDK_INT >= 24) {
            imgUri = FileProvider.getUriForFile(getActivity(), "comp4342.grp15.gem.fileprovider", outputImage);
        } else {
            imgUri = Uri.fromFile(outputImage);
        }

        //启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, 100);
    }

    // 获取得到的照片
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imgUri));
                        photoView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 200:
                if (resultCode == RESULT_OK) {
                    try {
                        //将相册的照片显示出来
                        Uri uri_photo = data.getData();
                        bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri_photo));
                        photoView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    // 把filePath地址对应的图片转换成Bitmap，然后再将bitmap转换成Base64字符串String
    private String bitmapToString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    // 上传
    private void doUpload(){
        if (username.equals("null") || identifier.equals("null")){
            Toast.makeText(getContext(), "Please Login first!", Toast.LENGTH_SHORT).show();
            return;
        } else if (messageEditText.getText().toString().length() >= 256 || messageEditText.getText().toString().length() <= 10){
            Toast.makeText(getContext(), "Invalid message length", Toast.LENGTH_SHORT).show();
            return;
        }else if (bitmap == null){
            Toast.makeText(getContext(), "Please select a picture", Toast.LENGTH_SHORT).show();
            return;
        }else if (Objects.equals(location, "") || Objects.equals(location, "null")){
            Toast.makeText(getContext(), "Please open location service!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 加载动画
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        // get json String from server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://comp4342.hjm.red/upload",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        responseMessage = gson.fromJson(s, ResponseMessage.class);
                        if (responseMessage.getStatus().equals("Success")) {
                            // 发送成功
                            messageEditText.setText("");
                            imgUri = null;
                            bitmap = null;
                            photoView.setImageDrawable(Drawable.createFromPath("@drawable/pic_choose_pic"));
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Post Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            // 发送失败
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
                            if(responseMessage.getStatus().equals("Fail")){
                                ContentValues values = new ContentValues();
                                values.put("username", "null");
                                values.put("password", "null");
                                values.put("identifier", "null");
                                mainActivity.getWritableDatabase().update("user", values, "id = 1", null);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Networking Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                Gson gson = new Gson();
                ServerPostMeta serverPostMeta = new ServerPostMeta(username, identifier,location,messageEditText.getText().toString(),bitmapToString());
                String json = gson.toJson(serverPostMeta);
                return json.getBytes(StandardCharsets.UTF_8);
            }
        };
        requestQueue.add(stringRequest);

    }

    // 解析地址
    private void parseLocation(){
        String locationX;
        String locationY;
        String point = location;
        String[] locationArr = point.split(",");
        if(locationArr.length ==2){
            locationX = locationArr[0].substring(1);
            locationY = locationArr[1];
            locationY = locationY.substring(0,locationY.length()-2);
        }else {
            return;
        }

        // get txt String from server
        StringRequest stringRequest = new StringRequest(Request.Method.GET,"https://comp4342.hjm.red/loc?X=" + locationX + "&Y=" + locationY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if(!s.equals("Unknown")){
                            locationTextView.setText("Location: " + s);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(mainActivity.getApplicationContext(), "Networking Error", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);

    }

}





