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

------

![030122](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030122.png)

![030139](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030139.png)

![030342](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030342.png)

-------

- 例子

```
//Kotlin MVVM Activity Sample

class SampleActivity : BaseVMActivity<ActivitySampleBinding, SampleViewModel>(
    ActivitySampleBinding::inflate,
    BR.sampleVM,
    SampleViewModel::class.java
)

class SampleViewModel : BaseViewModel() {
    val title = "普通MVVM页面"
    private val repository = SampleRepository(viewModelScope)
    val test = MutableLiveData("test")
}
```

```
//layout/activity_sample.xml

<?xml version="1.0" encoding="utf-8"?>
<layout>

    <data>

        <variable
            name="sampleVM"
            type="com.example.vm.SampleViewModel" />
    </data>

    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <com.yuehai.widget.CustomToolbar
            android:layout_width="match_parent"
            android:background="@color/purple_200"
            android:layout_height="?actionBarSize"
            app:onBackClickListener="@{()->sampleVM.finish()}"
            app:title="@{sampleVM.title}" />

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
