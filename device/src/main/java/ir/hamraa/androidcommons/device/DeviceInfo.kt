@file:Suppress("unused", "DEPRECATION", "MemberVisibilityCanBePrivate")

package ir.hamraa.androidcommons.device

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.pm.FeatureInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.hardware.Camera
import android.hardware.camera2.CameraManager
import android.location.LocationManager
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.webkit.WebSettings
import ir.hamraa.androidcommons.device.resolvers.DeviceIdResolver
import ir.hamraa.androidcommons.device.resolvers.DeviceTypeResolver
import java.math.BigInteger
import java.net.NetworkInterface
import java.util.*


object DeviceInfo {

    /**
     * Gets the ANDROID_ID. See API documentation for appropriate use.
     * sample: "dd96dec43fb81c97"
     */
    @SuppressLint("HardwareIds")
    fun androidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * Gets the API level.
     * sample: 25
     */
    fun apiLevel(): Int {
        return Build.VERSION.SDK_INT
    }

    /**
     * Gets the application name.
     * sample: "AwesomeApp"
     */
    fun applicationName(context: Context): String {
        return context.applicationInfo.loadLabel(context.packageManager).toString()
    }

    /**
     * Returns an object of location providers.
     * sample: { gps: true, network: true, passive: true }
     */
    fun availableLocationProviders(context: Context): List<String> {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return try {
            locationManager.getProviders(true)
        } catch (e: Exception) {
            emptyList()
        }
    }

    /**
     * Gets the battery level of the device as a float comprised between 0 and 1.
     * sample: 0.759999
     */
    fun batteryLevel(): Double {
        TODO()
    }

    /**
     * The system bootloader version number.
     * sample: "mw8998-002.0069.00"
     */
    fun bootLoader(): String {
        return Build.BOOTLOADER
    }

    /**
     * Gets the device brand.
     * sample: "xiaomi"
     */
    fun brand(): String {
        return Build.BRAND
    }

    /**
     * Gets build number of the operating system.
     * sample: "13D15"
     */
    fun buildId(): String {
        return Build.ID
    }

    /**
     * Gets the carrier name (network operator).
     * sample: "SOFTBANK"
     */
    fun carrierName(context: Context): String {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.networkOperatorName
    }

    /**
     * The current development codename, or the string "REL" if this is a release build.
     * sample: "REL"
     */
    fun codeName(): String {
        return Build.VERSION.CODENAME
    }

    /**
     * The name of the industrial design.
     * sample: "walleye"
     */
    fun device(): String {
        return Build.DEVICE
    }

    /**
     * Gets the device height, in pixels.
     * sample: 1080
     */
    fun deviceHeight(context: Context): Int {
        val displaymetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.heightPixels
    }

    /**
     * Gets the device ID.
     * sample: "goldfish"
     */
    fun deviceId(): String {
        return Build.BOARD
    }

    /**
     * Gets the device name.
     * sample: ?
     */
    fun deviceName(context: Context): String {
        try {
            val bluetoothName = Settings.Secure.getString(context.contentResolver, "bluetooth_name")
            if (bluetoothName != null) {
                return bluetoothName
            }
            if (Build.VERSION.SDK_INT >= 25) {
                val deviceName =
                    Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
                if (deviceName != null) {
                    return deviceName
                }
            }
        } catch (e: Exception) {
            // same as default unknown return
        }
        return "unknown"
    }

    /**
     * Returns the device's type as a string, which will be one of:
     * - handset
     * - tablet
     * - tv
     * - unknown
     */
    fun deviceType(context: Context): String {
        return DeviceTypeResolver(context).deviceType.value
    }

    /**
     * Gets the device width, in pixels.
     * sample: 720
     */
    fun deviceWidth(context: Context): Int {
        val displaymetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displaymetrics)
        return displaymetrics.widthPixels
    }

    /**
     * A build ID string meant for displaying to the user.
     * sample: "OPM2.171026.006.G1"
     */
    fun display(): String {
        return Build.DISPLAY
    }

    /**
     * Gets the time at which the app was first installed, in milliseconds.
     * sample: 1517681764528
     */
    fun firstInstallTime(context: Context): Long {
        return getPackageInfo(context)?.firstInstallTime ?: -1L
    }

    /**
     * A string that uniquely identifies this build.
     * sample: "google/walleye/walleye:8.1.0/OPM2.171026.006.G1/4820017:user/release-keys"
     */
    fun fingerprint(): String {
        return Build.FINGERPRINT
    }

    /**
     * Gets the device font scale.
     * The font scale is the ratio of the current system font to the "normal" font size,
     * so if normal text is 10pt and the system font is currently 15pt, the font scale would be 1.5
     * This can be used to determine if accessability settings has been changed for the device;
     * you may want to re-layout certain views if the font scale is significantly larger ( > 2.0 )
     *
     * sample: 1.2
     */
    fun fontScale(context: Context): Float {
        return context.resources.configuration.fontScale
    }

    /**
     * Gets available storage size, in bytes.
     * sample: 17179869184
     */
    fun freeDiskStorage(): Double {
        return try {
            val external = StatFs(Environment.getExternalStorageDirectory().absolutePath)
            val availableBlocks: Long
            val blockSize: Long
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                availableBlocks = external.availableBlocks.toLong()
                blockSize = external.blockSize.toLong()
            } else {
                availableBlocks = external.availableBlocksLong
                blockSize = external.blockSizeLong
            }
            BigInteger.valueOf(availableBlocks).multiply(BigInteger.valueOf(blockSize)).toDouble()
        } catch (e: java.lang.Exception) {
            -1.0
        }
    }

    /**
     * The name of the hardware (from the kernel command line or /proc).
     * sample: "walleye"
     */
    fun hardware(): String {
        return Build.HARDWARE
    }

    /**
     * Tells if the device has a notch.
     * sample: true or false
     */
    fun hasNotch(context: Context): Boolean {
        return false
    }

    /**
     * Tells if the device has a specific system feature.
     * sample: true or false
     */
    fun hasSystemFeature(context: Context, feature: String): Boolean {
        if (feature.isBlank()) {
            return false
        }
        return context.packageManager.hasSystemFeature(feature)
    }

    /**
     * Gets the Hostname.
     * sample: "wprd10.hot.corp.google.com"
     */
    fun host(): String {
        return Build.HOST
    }

    /**
     * The internal value used by the underlying source control to represent this build.
     * sample: "4820017"
     */
    fun incremental(): String {
        return Build.VERSION.INCREMENTAL
    }

    /**
     * The internal value used by the underlying source control to represent this build.
     * sample: "com.android.vending"
     */
    fun installerPackageName(context: Context): String {
        return try {
            context.packageManager.getInstallerPackageName(context.packageName) ?: "unknown"
        } catch (e: IllegalArgumentException) {
            "unknown"
        }
    }

    /**
     * Gets the referrer string upon application installation.
     * sample: "my_install_referrer"
     */
    fun installReferrer() {
        TODO()
    }

    /**
     * Gets the application instance ID. see https://developers.google.com/instance-id
     * sample: ?
     */
    fun instanceId(context: Context): String {
        return DeviceIdResolver(context).instanceId
    }

    /**
     * Tells if the device is in Airplane Mode.
     * sample: false
     */
    fun isAirplaneMode(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Settings.System.getInt(
                context.contentResolver,
                Settings.System.AIRPLANE_MODE_ON,
                0
            ) != 0
        } else {
            Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.AIRPLANE_MODE_ON,
                0
            ) != 0
        }
    }

    /**
     * Tells if the battery is currently charging.
     * sample: true or false
     */
    fun isBatteryCharging(context: Context): Boolean {
        TODO()
    }

    /**
     * Tells if the device have any camera now.
     * sample: true or false
     */
    fun isCameraPresent(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val manager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                manager.cameraIdList.isNotEmpty()
            } catch (e: Exception) {
                false
            }
        } else {
            Camera.getNumberOfCameras() > 0
        }
    }

    /**
     * Tells if the application is running in an emulator.
     * sample: false
     */
    @SuppressLint("HardwareIds")
    fun isEmulator(): Boolean {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase(Locale.US).contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.HARDWARE.contains("vbox86")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
                || Build.BOARD.toLowerCase(Locale.US).contains("nox")
                || Build.BOOTLOADER.toLowerCase(Locale.US).contains("nox")
                || Build.HARDWARE.toLowerCase(Locale.US).contains("nox")
                || Build.PRODUCT.toLowerCase(Locale.US).contains("nox")
                || Build.SERIAL.toLowerCase(Locale.US).contains("nox")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
    }

    /**
     * Tells if the device is connected to wired headset or bluetooth headphones
     * sample: true or false
     */
    fun isHeadphonesConnected(context: Context): Boolean {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        return audioManager.isWiredHeadsetOn || audioManager.isBluetoothA2dpOn
    }

    /**
     * Tells if the device is currently in landscape mode.
     * sample: true or false
     */
    fun isLandscape(context: Context): Boolean {
        TODO()
    }

    /**
     * Tells if the device has location services turned off at the device-level (NOT related to app-specific permissions)
     * sample: true or false
     */
    fun isLocationEnabled(context: Context): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.P -> {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                try {
                    locationManager.isLocationEnabled
                } catch (e: Exception) {
                    System.err.println("Unable to determine if location enabled. LocationManager was null")
                    return false
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> {
                val locationMode = Settings.Secure.getInt(
                    context.contentResolver,
                    Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF
                )
                locationMode != Settings.Secure.LOCATION_MODE_OFF
            }
            else -> {
                val locationProviders: String = Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED
                )
                locationProviders.isNotEmpty()
            }
        }
    }

    /**
     * Tells if a PIN number or a fingerprint was set for the device.
     * sample: true or false
     */
    fun isPinOrFingerprintSet(context: Context): Boolean {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isKeyguardSecure
    }

    /**
     * Tells if the device is a tablet.
     * sample: true
     */
    fun isTablet(context: Context): Boolean {
        return DeviceTypeResolver(context).isTablet
    }

    /**
     * Gets the time at which the app was last updated, in milliseconds.
     * sample: 1517681764992
     */
    fun lastUpdateTime(context: Context): Long {
        return getPackageInfo(context)?.lastUpdateTime ?: -1L
    }

    /**
     * Gets the network adapter MAC address.
     * sample: "E5:12:D8:E5:69:97"
     */
    @SuppressLint("MissingPermission", "HardwareIds")
    fun mac(context: Context): String {
        val manager =
            context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = manager.connectionInfo

        var macAddress = ""
        if (wifiInfo != null) {
            macAddress = wifiInfo.macAddress
        }

        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            try {
                for (networkInterface in NetworkInterface.getNetworkInterfaces()) {
                    if (networkInterface.name.equals("wlan0", true).not()) continue
                    val macBytes: ByteArray = networkInterface.hardwareAddress
                    macAddress = run {
                        val builder = StringBuilder()
                        macBytes.forEach { builder.append(String.format("%02X:", it)) }
                        if (builder.isNotEmpty()) builder.deleteCharAt(builder.length - 1)
                        builder.toString()
                    }
                }
            } catch (ex: Exception) {
                // do nothing
            }
        }

        return macAddress
    }

    /**
     * Gets the device manufacturer.
     * sample: "Google"
     */
    fun manufacturer(): String {
        return Build.MANUFACTURER
    }

    /**
     * Gets the maximum amount of memory that the VM will attempt to use, in bytes.
     * sample: 402653183
     */
    fun maxMemory(): Double {
        return Runtime.getRuntime().maxMemory().toDouble()
    }

    /**
     * Gets the device model.
     * sample: "Google"
     */
    fun model(): String {
        return Build.MODEL
    }

    /**
     * Gets the device OS version.
     * sample: "7.1.1"
     */
    fun osVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * Gets the application package name.
     * sample: "com.example.AwesomeApp"
     */
    fun packageName(context: Context): String {
        return context.packageName
    }

    /**
     * Gets the device phone number.
     * sample: [ "+15555215558" ]
     * permissions: android.permission.READ_PHONE_STATE
     */
    @SuppressLint("HardwareIds", "MissingPermission")
    fun phoneNumber(context: Context): String {
        if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ||
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkCallingOrSelfPermission(
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED) ||
            (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && context.checkCallingOrSelfPermission(
                Manifest.permission.READ_PHONE_NUMBERS
            ) == PackageManager.PERMISSION_GRANTED)
        ) {
            return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).line1Number
        }
        return "unknown"
    }

    /**
     * Gets the power state of the device.
     * sample: { batteryLevel: 0.759999, batteryState: 'unplugged', lowPowerMode: false }
     */
    fun powerState(context: Context): String {
        TODO()
    }

    /**
     * The name of the overall product.
     * sample: "walleye"
     */
    fun product(): String {
        return Build.PRODUCT
    }

    /**
     * The developer preview revision of a prerelease SDK.
     * sample: 0
     */
    fun previewSdkInt(): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Build.VERSION.PREVIEW_SDK_INT
        }
        return -1
    }

    /**
     * Gets the application human readable version (same as versionName() + '.' + versionCode())
     * sample: "1.0.1.234"
     */
    fun readableVersion(context: Context): String {
        return "${versionName(context)}.${versionCode(context)}"
    }

    /**
     * Gets the device serial number.
     * sample: ? (maybe a serial number, if your app is privileged)
     */
    @SuppressLint("MissingPermission")
    fun serialNumber(context: Context): String {
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                if (context.checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    return Build.getSerial()
                }
            }
        } catch (e: Exception) {
            // This is almost always a PermissionException. We will log it but return unknown
            System.err.println("getSerialNumber failed, it probably should not be used: " + e.message)
        }

        return "unknown"
    }

    /**
     * The user-visible security patch level.
     * sample: "2018-07-05"
     */
    fun securityPatch(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Build.VERSION.SECURITY_PATCH
        }
        return "unknown"
    }

    /**
     * Gets the device serial number.
     * sample: ["android.software.backup", "android.hardware.screen.landscape", "android.hardware.wifi", ...]
     */
    fun systemAvailableFeatures(context: Context): Array<String> {
        val features: Array<FeatureInfo> = context.packageManager.systemAvailableFeatures
        return features.map { it.name }.toTypedArray()
    }

    /**
     * Comma-separated tags describing the build.
     * sample: "release-keys, unsigned, debug"
     */
    fun tags(): String {
        return Build.PRODUCT
    }

    /**
     * The type of build.
     * sample: "user", "eng"
     */
    fun type(): String {
        return Build.TYPE
    }

    /**
     * Gets full disk storage size, in bytes.
     * sample: 17179869184
     */
    fun totalDiskCapacity(): Double {
        return try {
            val root = StatFs(Environment.getRootDirectory().absolutePath)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                BigInteger.valueOf(root.blockCountLong)
                    .multiply(BigInteger.valueOf(root.blockSizeLong)).toDouble()
            } else {
                BigInteger.valueOf(root.blockCount.toLong())
                    .multiply(BigInteger.valueOf(root.blockSize.toLong())).toDouble()
            }
        } catch (e: Exception) {
            -1.0
        }

    }

    /**
     * Gets the device total memory, in bytes.
     * sample: 1995018240
     */
    fun totalMemory(context: Context): Double {
        val actMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        actMgr.getMemoryInfo(memInfo)
        return memInfo.totalMem.toDouble()
    }

    /**
     * Gets the device unique ID. It is currently identical to androidId() in this module.
     * sample: "dd96dec43fb81c97"
     */
    @SuppressLint("HardwareIds")
    fun uniqueId(context: Context): String {
        return androidId(context)
    }

    /**
     * Gets the app memory usage, in bytes.
     * sample: 23452345
     */
    fun usedMemory(): Int {
        val rt = Runtime.getRuntime()
        val usedMemory = rt.totalMemory() - rt.freeMemory()
        return usedMemory.toInt()
    }

    /**
     * Gets the device User Agent.
     * sample: ?
     */
    fun userAgent(context: Context): String? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                WebSettings.getDefaultUserAgent(context)
            } else {
                System.getProperty("http.agent")
            }
        } catch (e: RuntimeException) {
            System.getProperty("http.agent")
        }
    }

    /**
     * Gets the application version.
     * sample: "1.0"
     */
    fun versionName(context: Context): String {
        return getPackageInfo(context)?.versionName ?: "unknown"
    }

    /**
     * Gets the application version code.
     * sample: 4
     */
    fun versionCode(context: Context): Long {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            getPackageInfo(context)?.longVersionCode ?: -1L
        else getPackageInfo(context)?.versionCode?.toLong() ?: -1L
    }

    /**
     * An ordered list of 32 bit ABIs supported by this device.
     * sample: ["armeabi-v7a", "armeabi"]
     */
    fun supported32BitAbis(): Array<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_32_BIT_ABIS
        }
        return emptyArray()
    }

    /**
     * An ordered list of 64 bit ABIs supported by this device.
     * sample: ["arm64-v8a"]
     */
    fun supported64BitAbis(): Array<String> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Build.SUPPORTED_64_BIT_ABIS
        }
        return emptyArray()
    }

    /**
     * Returns a list of supported processor architecture version
     * sample: [ "arm64 v8", "Intel x86-64h Haswell", "arm64-v8a", "armeabi-v7a", "armeabi" ]
     */
    fun supportedAbis(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            arrayOf(Build.CPU_ABI)
        }
    }

    private fun getPackageInfo(context: Context): PackageInfo? {
        return try {
            context.packageManager.getPackageInfo(context.packageName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            null
        }
    }
}