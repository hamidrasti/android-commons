package ir.hamraa.androidcommons.utils

import java.util.Locale

object BrowserUtils {

    fun isSameDomain(url: String, url1: String): Boolean {
        return getRootDomainUrl(
            url.toLowerCase(
                Locale.ROOT
            )
        ) == getRootDomainUrl(
            url1.toLowerCase(Locale.ROOT)
        )
    }

    private fun getRootDomainUrl(url: String): String {
        val domainKeys = url.split("/").toTypedArray()[2].split("\\.").toTypedArray()
        val length = domainKeys.size
        val dummy = if (domainKeys[0] == "www") 1 else 0
        return if (length - dummy == 2) domainKeys[length - 2] + "." + domainKeys[length - 1] else {
            if (domainKeys[length - 1].length == 2) {
                domainKeys[length - 3] + "." + domainKeys[length - 2] + "." + domainKeys[length - 1]
            } else {
                domainKeys[length - 2] + "." + domainKeys[length - 1]
            }
        }
    }
}