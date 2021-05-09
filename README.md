# MVVM

- 先让你的项目支持dataBinding

  app/build.gradle
```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
}
android {
    ...
   buildFeatures {
    dataBinding = true
   }
}
```
- 依赖
```
    allprojects {
        repositories {
            google()
            mavenCentral()
            maven { url 'https://jitpack.io' }
        }
    }

    dependencies {
        ...
        implementation 'com.github.zhaoyuehai:mvvm:1.0.0'
    }

```

------

![030122](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030122.png)

![030139](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030139.png)

![030342](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030342.png)

-------

- 例子

```
//Kotlin MVVM Activity Sample

class SampleActivity : BaseVMActivity<ActivitySampleBinding, SampleViewModel>() {
    override val layout = R.layout.activity_sample
    override val variableId = BR.sampleVM
    override val viewModelClass = SampleViewModel::class.java
    override fun getCustomToolbar() = viewDataBinding?.toolbar
}

public class SampleViewModel extends BaseViewModel {
    public MutableLiveData<String> test = new MutableLiveData<>("Test");
}
```

```
//layout/activity_sample.xml

<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="sampleVM"
            type="com.example.mytest.SampleViewModel" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <com.yuehai.widget.CustomToolbar
            android:id="@+id/toolbar"
            android:layout_width="match_parent"
            android:background="@color/purple_200"
            android:layout_height="?actionBarSize" />

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:text="@{sampleVM.test}"
            android:textColor="@color/purple_200"
            android:textSize="24sp" />
    </LinearLayout>
</layout>
```
-------
  笔记
```
//Tips: Class 转 KClass
JvmClassMappingKt.getKotlinClass(XXXViewModel.class);
```