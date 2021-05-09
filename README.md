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
        implementation 'com.github.zhaoyuehai:MVVMDemo:1.0.1'
    }

```

------

![030122](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030122.png)

![030139](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030139.png)

![030342](https://github.com/zhaoyuehai/MVVMDemo/blob/master/img/030342.png)

-------

  笔记
```
//Tips: Class 转 KClass
JvmClassMappingKt.getKotlinClass(XXXViewModel.class);
```
