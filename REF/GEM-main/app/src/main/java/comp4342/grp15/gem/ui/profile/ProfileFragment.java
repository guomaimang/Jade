package comp4342.grp15.gem.ui.profile;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import comp4342.grp15.gem.MainActivity;
import comp4342.grp15.gem.databinding.FragmentProfileBinding;
import comp4342.grp15.gem.model.ResponseMessage;
import comp4342.grp15.gem.model.UserInfo;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private MainActivity mainActivity;

    Button loginButton;
    Button registerButton;
    TextView userNameText;
    TextView userLoginTime;
    EditText editUserName;
    EditText editPassword;

    ResponseMessage responseMessage;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 绑定
        mainActivity = (MainActivity) getActivity();
        loginButton = binding.profileLoginButton;
        registerButton = binding.profileSigninButton;
        userNameText = binding.profileUsernameText;
        userLoginTime = binding.profileLoginTime;
        editUserName = binding.profileEdittextUsername;
        editPassword = binding.profileEdittextPassword;

        // 初始化: 设置和展示用户状态
        resetStatus();

        // 登入/注销按钮
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!profileViewModel.getIsLogin()) {
                    // 按钮功能为登入
                    doLogin(editUserName.getText().toString(), editPassword.getText().toString());
                } else {
                    // 按钮表示注销
                    doLogout();
                }
            }
        });

        // 注册按钮功能
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister(editUserName.getText().toString(), editPassword.getText().toString());
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void resetStatus() {
        Cursor cursor = mainActivity.getReadableDatabase().rawQuery("select * from user where id == 1", null);
        while (cursor.moveToNext()) {
            String username = cursor.getString(1);
            if (!username.equals("null")) {
                // 已经登入
                profileViewModel.setUserName(username);
                profileViewModel.setLogin(true);
            } else {
                profileViewModel.setUserName("null");
                profileViewModel.setLogin(false);
            }
        }
        cursor.close();

        if (!profileViewModel.getIsLogin()) {
            // 用户没有登入
            userNameText.setText("Not Login.");
            userLoginTime.setText("Not Login.");
            loginButton.setText("LOGIN");
            registerButton.setVisibility(View.VISIBLE);
            editUserName.setVisibility(View.VISIBLE);
            editPassword.setVisibility(View.VISIBLE);
        } else {
            // 用户登入了
            userNameText.setText(profileViewModel.getUsername());

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            userLoginTime.setText(formatter.format(date));
            loginButton.setText("LOGOUT");
            registerButton.setVisibility(View.GONE);
            editUserName.setVisibility(View.GONE);
            editPassword.setVisibility(View.GONE);
        }
    }

    private void doLogin (String username, String password){
        if (username.equals("null") || username.equals("") || password.equals("")) {
            Toast.makeText(getContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
            return;
        }

        // 加载动画
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.show();

        // get json String from server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://comp4342.hjm.red/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        responseMessage = gson.fromJson(s, ResponseMessage.class);
                        if (responseMessage.getStatus().equals("Success")) {
                            // 登入成功
                            ContentValues values = new ContentValues();
                            values.put("username", editUserName.getText().toString());
                            values.put("password", editPassword.getText().toString());
                            values.put("identifier", responseMessage.getMessage());
                            mainActivity.getWritableDatabase().update("user", values, "id = 1", null);
                            resetStatus();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                        } else {
                            // 登入失败
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Wrong Username or Password", Toast.LENGTH_SHORT).show();
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
                UserInfo userInfo = new UserInfo(editUserName.getText().toString(), editPassword.getText().toString());
                String json = gson.toJson(userInfo);
                return json.getBytes(StandardCharsets.UTF_8);
            }
        };
        mainActivity.getRequestQueue().add(stringRequest);
    }

    private void doLogout () {
        ContentValues values = new ContentValues();
        values.put("username", "null");
        values.put("password", "null");
        values.put("identifier", "null");
        mainActivity.getWritableDatabase().update("user", values, "id = 1", null);
        resetStatus();
    }

    private void doRegister (String username, String password){
        if (username.equals("null") || username.equals("") || password.equals("") || username.length() <= 3 || password.length() <= 6) {
            Toast.makeText(getContext(), "Illegal texts", Toast.LENGTH_SHORT).show();
            return;
        }

        // 加载动画
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Doing Registering...");
        progressDialog.show();

        // get json String from server
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://comp4342.hjm.red/register",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        responseMessage = gson.fromJson(s, ResponseMessage.class);
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), responseMessage.getMessage(), Toast.LENGTH_SHORT).show();
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
                UserInfo userInfo = new UserInfo(editUserName.getText().toString(), editPassword.getText().toString());
                String json = gson.toJson(userInfo);
                return json.getBytes(StandardCharsets.UTF_8);
            }
        };
        mainActivity.getRequestQueue().add(stringRequest);
    }

}