# FunkyHeader
Provide Funky cuts on image (The Perfect Header for Profile UI) 

[![API](https://img.shields.io/badge/API-9%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=9)  [![](https://jitpack.io/v/IntruderShanky/FunkyHeader.svg)](https://jitpack.io/#IntruderShanky/FunkyHeader)

[Demo App - IS Library](https://play.google.com/store/apps/details?id=com.intrusoft.islibrarydemo)

<a href='https://play.google.com/store/apps/details?id=com.intrusoft.islibrarydemo&utm_source=global_co&utm_small=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width="193" height="75"/></a>

# Preview
![Screenshot](screenshot.png)

# Usage
Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
Step 2. Add the dependency
```groovy
dependencies {
  compile 'com.github.IntruderShanky:FunkyHeader:1.2.1'
 }
 ```
# Implementation
###XML Implementation:
```xml
 <com.intrusoft.library.FunkyHeader
        android:id="@+id/wave_head"
        android:layout_width="match_parent"
        android:layout_height="350dp"
        funky:scaleType="centerCrop"
        funky:src="@drawable/intruder_shanky"
        funky:tint="#88880E4F" />
```
###Attributes
####Image ScaleType
```xml
funky:scaleType="centerCrop"
funky:scaleType="fitXY"
```
####Image Drawable Resource
```xml
funky:src="@drawable/your_image"
```
####Background Tint Color (Color Shold have some alpha value, default value 55)
```xml
squint:tint="@color/your_color"
```
###Java Implementation:
```java
FunkyHeader funkyHeader = (FunkyHeader) findViewById(R.id.funky_header);

// to set image from resources        
funkyHeader.setImageSource(R.drawable.your_image);

// to set bitmap
funkyHeader.setBitmap(bitmap);

// to set the background color (color should have some alpha val)
funkyHeader.setColorTint(Color.GREEN);
```
#Licence
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

