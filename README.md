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

 - [core](##core)
 - [device](##device)
 - [utils](##utils)
 
You can add this dependencies to your build.gradle based on your need.
or add all dependencies at once:

```gradle
implementation 'com.github.hamraa:android-commons:Tag'
```

## Core
Core module contains useful classes such as `Singleton`, `TinyDB`, ... for
common usage

```gradle
implementation 'com.github.hamraa.android-commons:core:Tag'
```

## Device

## Utils