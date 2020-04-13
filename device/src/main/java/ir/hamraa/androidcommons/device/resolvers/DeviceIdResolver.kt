package ir.hamraa.androidcommons.device.resolvers

import android.content.Context
import java.lang.reflect.InvocationTargetException

/**
 * Instance Id resolver's single purpose is to get the device's Instance Id
 */
class DeviceIdResolver(private val context: Context) {

    val instanceId: String
        get() {
            try {
                return firebaseInstanceId
            } catch (ignored: ClassNotFoundException) {
            } catch (e: NoSuchMethodException) {
                System.err.println("N/A: Unsupported version of com.google.firebase:firebase-iid in your project.")
            } catch (e: SecurityException) {
                System.err.println("N/A: Unsupported version of com.google.firebase:firebase-iid in your project.")
            } catch (e: IllegalAccessException) {
                System.err.println("N/A: Unsupported version of com.google.firebase:firebase-iid in your project.")
            } catch (e: InvocationTargetException) {
                System.err.println("N/A: Unsupported version of com.google.firebase:firebase-iid in your project.")
            }
            try {
                return gmsInstanceId
            } catch (ignored: ClassNotFoundException) {
            } catch (e: NoSuchMethodException) {
                System.err.println("N/A: Unsupported version of com.google.android.gms.iid in your project.")
            } catch (e: SecurityException) {
                System.err.println("N/A: Unsupported version of com.google.android.gms.iid in your project.")
            } catch (e: IllegalAccessException) {
                System.err.println("N/A: Unsupported version of com.google.android.gms.iid in your project.")
            } catch (e: InvocationTargetException) {
                System.err.println("N/A: Unsupported version of com.google.android.gms.iid in your project.")
            }
            System.err.println("Can't generate id. Please add com.google.firebase:firebase-iid to your project.")
            return "unknown"
        }

    @get:Throws(
        ClassNotFoundException::class,
        NoSuchMethodException::class,
        IllegalAccessException::class,
        InvocationTargetException::class
    )
    private val gmsInstanceId: String
        get() {
            val clazz = Class.forName("com.google.android.gms.iid.InstanceID")
            val method = clazz.getDeclaredMethod("getInstance", Context::class.java)
            val obj = method.invoke(null, context)
            val method1 = obj.javaClass.getMethod("getId")
            return method1.invoke(obj) as String
        }

    @get:Throws(
        ClassNotFoundException::class,
        NoSuchMethodException::class,
        IllegalAccessException::class,
        InvocationTargetException::class
    )
    private val firebaseInstanceId: String
        get() {
            val clazz = Class.forName("com.google.firebase.iid.FirebaseInstanceId")
            val method = clazz.getDeclaredMethod("getInstance")
            val obj = method.invoke(null)
            val method1 = obj.javaClass.getMethod("getId")
            return method1.invoke(obj) as String
        }

}