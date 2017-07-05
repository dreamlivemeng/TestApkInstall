package com.junze.testapkupdate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

/**
 * 这儿需要注意的如果用到getExternalStorageDirectory()必须有sd的权限
 */
public class MainActivity extends AppCompatActivity {

    Button updateBtn;
    TextView versionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        versionTv = (TextView) findViewById(R.id.version_tv);
        updateBtn = (Button) findViewById(R.id.update);
        versionTv.setText(getVersion(MainActivity.this));
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path1 = getExternalFilesDir("logs").getAbsoluteFile() + "/aa.apk";//这个路径是存放在Android/data/包名/log文件夹下

                /**
                 * appTest必须对应xml/file_paths.xml下的
                 <external-path
                 name="appTest"/>
                 path="" />
                 */
                String path2 = Environment.getExternalStorageDirectory() + "/appTest/aa.apk";
                install1(path2, MainActivity.this);
            }
        });
    }

    /**
     * 安装的方法
     *
     * @param path apk存放的路径
     * @param mContext 上下文
     */
    private void install1(String path, Context mContext) {
        File file = new File(path);
        //getContext().getPackageName() + ".fileprovider" "com.junze.testapkupdate.fileprovider"
        //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        mContext.startActivity(intent);
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
