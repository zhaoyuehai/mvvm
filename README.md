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

![1624525489](https://github.com/zhaoyuehai/mvvm/blob/master/img/1624525489.png)
![1624525525](https://github.com/zhaoyuehai/mvvm/blob/master/img/1624525525.png)


-------

- 例子

```
//Kotlin MVVM Activity Sample

@AndroidEntryPoint
class SampleActivity : BaseVMActivity<ActivitySampleBinding, SampleViewModel>(
    R.layout.activity_sample,
    BR.sampleVM,
    SampleViewModel::class
)

@HiltViewModel
class SampleViewModel @Inject constructor(private val repository: DataRepository) : BaseViewModel() {
    val title = "普通MVVM页面"
    val test = MutableLiveData("test")
    
     override fun initData() {
        super.initData()
        showLoading("加载中...")
        repository.loadData(viewModelScope){
            dismissLoading()
            if (it.isOK()) {
                test.value = it.data().toString()
                ...
            }
        }
    }
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


------



### Hilt



- 依赖注入库会自动释放不再使用的对象，减少资源的过度使用。
- 在配置 scopes 范围内，可重用依赖项和创建的实例，提高代码的可重用性，减少了很多模板代码。
- 代码变得更具可读性。
- 易于构建对象。
- 编写低耦合代码，更容易测试。



#### 引入依赖

project/build.gradle

```
buildscript {
 	ext.kotlin_version = '1.5.10'
    ext.hilt_version = "2.37"
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:4.2.1"
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        classpath "com.google.dagger:hilt-android-gradle-plugin:$hilt_version"
    }
}
...
```

app/build.gradle

```
plugins {
    id 'com.android.application'
    id 'kotlin-android'
    id 'kotlin-kapt'
    id 'dagger.hilt.android.plugin'
}

...

dependencies {

   	...

    // di
    implementation "com.google.dagger:hilt-android:$hilt_version"
    kapt "com.google.dagger:hilt-compiler:$hilt_version"
}
```



#### 代码



```
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        ...

        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApiService(okHttpClient: OkHttpClient): ApiService = Retrofit

       	...

       	.create(ApiService::class.java)
}
```

Application

```
@HiltAndroidApp
class MyApplication: Application()
```

Activity / Fragment

```
@AndroidEntryPoint
class MainActivity : BaseActivity() //仅仅支持 ComponentActivity 的子类例如 FragmentActivity、AppCompatActivity

@AndroidEntryPoint
class Demo1Fragment  : BaseFragment() //仅仅支持继承 androidx.Fragment 的 Fragment

//Java   Fragment?
@WithFragmentBindings
@AndroidEntryPoint
public class Demo2Fragment extends BaseFragment() -> Fragment
```

ViewModel

```
@HiltViewModel
class MainViewModel @Inject constructor(): ViewModel()

@HiltViewModel
class Demo1ViewModel @Inject constructor(private val repository: DataRepository) : ViewModel()
```

Repository

```
@Singleton
class DataRepository @Inject constructor(private val apiService: ApiService)
```