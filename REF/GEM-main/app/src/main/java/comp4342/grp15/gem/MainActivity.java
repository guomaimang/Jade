package comp4342.grp15.gem;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import comp4342.grp15.gem.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;
    private LocationManager locationManager;

    private DBController dbController;

    private SQLiteDatabase writableDatabase;
    private SQLiteDatabase readableDatabase;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 第一次尝试获取所有所需的权限
        firstRequestPermissions();

        // 创建网络队列
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        //创建数据库  名字可以不带.db，为了增加可读性，最好加上.db
        dbController = new DBController(this, "login.db", null, 1);
        //如果数据库不存在，则创建数据库，然后获取可读可写的数据库对象，如果存在，直接打开；
        writableDatabase = dbController.getWritableDatabase();
        //如果存储空间满了，获取一个只读数据库对象；
        readableDatabase = dbController.getReadableDatabase();

        // 如果用户行不存在则创建用户行
        Cursor cursor = writableDatabase.rawQuery("select COUNT(*) from user",null);
        while (cursor.moveToNext()){
            if(cursor.getInt(0) == 0){
                ContentValues values = new ContentValues();
                values.put("id",1);
                values.put("username","null");
                values.put("password","null");
                values.put("identifier","null");
                values.put("locationX","null");
                values.put("locationY","null");
                writableDatabase.insert("user", null, values);
            }
        }
        cursor.close();

        // 设置 navView
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_trend, R.id.navigation_map, R.id.navigation_upload, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // 第一次更新地理位置，并启动监听
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationUpdate();
    }

    public void firstRequestPermissions() {

        // 第 1 步: 检查是否有相应的权限
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                }
        );

        // 如果这个权限全都拥有, 则可进入APP，可以在这里定义一些操作
        if (isAllGranted) {
            return;
        }

        // 第 2 步: 请求权限 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                },
                MY_PERMISSION_REQUEST_CODE
        );

    }

    /**
     * 第 3 步: 申请权限结果返回处理
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行可执行

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
                openAppDetails();
            }
        }
    }

    /**
     * 检查是否拥有指定的所有权限
     */
    public boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You will need to manually enable the appropriate permissions to keep the app running properly.");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    // 获取地理坐标
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 当GPS定位信息发生改变时，更新定位
            // 使用监听器，使之自动更新
            afterUpdateLocation(location);
        }
    };

    // 位置监听设置
    private void locationUpdate() {
        // 如果没权限，则需要用户授权
        if (checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Please allow location service", Toast.LENGTH_SHORT).show();
            return;
        }
        // 第一次通过网络服务定位
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        afterUpdateLocation(location);
        // 设置间隔 5 秒获得一次 GPS 定位信息
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 8,mLocationListener);
    }

    // Location 更新后
    private void afterUpdateLocation(Location lc) {
        if (lc != null) {
            ContentValues values = new ContentValues();
            values.put("locationX", String.valueOf(lc.getLongitude()));
            values.put("locationY", String.valueOf(lc.getLatitude()));
            writableDatabase.update("user", values, "id = 1", null);
            Toast.makeText(getApplicationContext(), "Refresh location success.", Toast.LENGTH_SHORT).show();
        }
    }

    //Java Beans

    public SQLiteDatabase getWritableDatabase() {
        return writableDatabase;
    }

    public SQLiteDatabase getReadableDatabase() {
        return readableDatabase;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

}