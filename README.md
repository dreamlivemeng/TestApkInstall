# TestApkInstall
适配Android7.0的apk在线安装


## 第一步
在AndroidMainfest.xml的application标签里面添加一下代码
其中com.junze.testapkupdate指的是应用包名
```
  <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="com.junze.testapkupdate.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />

```
## 第二步
在res文件下面增加xml文件夹,定义一个为file_paths的文件
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!--name：一个引用字符串。
    path：文件夹“相对路径”，完整路径取决于当前的标签类型。-->
    <!--
    <files-path name="name" path="path" /> 相当于Context.getFilesDir() + /path/。

    <cache-path name="name" path="path" />物理路径相当于Context.getCacheDir() + /path/。

    <external-path name="name" path="path" />物理路径相当于Environment.getExternalStorageDirectory() + /path/。

    <external-files-path name="name" path="path" />物理路径相当于Context.getExternalFilesDir(String) + /path/。

    <external-cache-path name="name" path="path" />
    物理路径相当于Context.getExternalCacheDir() + /path/

    需要注意的是，你apk放到哪儿，里这儿就配置那个路径就ok
    -->
    <paths>
        <external-path
            name="Download"
            path="" />
        <external-path name="appTest" />
        path="" />
        <external-path
            name="files_root"
            path="Android/data/com.junze.testapkupdate/" />
        <external-path
            name="external_storage_root"
            path="." />
    </paths>

</resources>
```
## 第三步
调用安装的方法
```
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
```

