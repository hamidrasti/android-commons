[![](https://jitpack.io/v/hamraa/android-commons.svg)](https://jitpack.io/#hamraa/android-commons)

# Android Commons
Common packages written in kotlin and used in most my android projects

## Usage
Add jitpack to project level build.gradle

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

The library is split up into:

 - [core](#core)
 - [device](#device)
 - [utils](#utils)
 
You can add this dependencies to your build.gradle based on your need.
or add all dependencies at once:

```gradle
implementation 'com.github.hamraa:android-commons:latestRelease'
```

## Core
Core module contains useful classes such as `Anims`, `Linking`, `Singleton`, `TinyDB`, ... for
common usage

```gradle
implementation 'com.github.hamraa.android-commons:core:latestRelease'
```

## Device
Device module contains some useful information about the device. this module is based on 
[react-native-device-info](https://github.com/react-native-community/react-native-device-info)

```gradle
implementation 'com.github.hamraa.android-commons:device:latestRelease'
```

## Time
Time module contains java.time package for android using coreLibraryDesugaring. this module is
kotlin version of [persian-date-time](https://github.com/mfathi91/persian-date-time) built with Android in mind.

```gradle
implementation 'com.github.hamraa.android-commons:time:latestRelease'
```

## Utils
This module contains useful utility classes

```gradle
implementation 'com.github.hamraa.android-commons:utils:latestRelease'
```