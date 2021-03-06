/*
 * # DON'T BE A DICK PUBLIC LICENSE
 *
 * > Version 1.1, December 2016
 *
 * > Copyright (C) 2016 Adam Prakash Lewis
 *
 *  Everyone is permitted to copy and distribute verbatim or modified
 *  copies of this license document.
 *
 * > DON'T BE A DICK PUBLIC LICENSE
 * > TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION
 *
 *  1. Do whatever you like with the original work, just don't be a dick.
 *
 *      Being a dick includes - but is not limited to - the following instances:
 *
 * 	 1a. Outright copyright infringement - Don't just copy this and change the name.
 * 	 1b. Selling the unmodified original with no work done what-so-ever, that's REALLY being a dick.
 * 	 1c. Modifying the original work to contain hidden harmful content. That would make you a PROPER dick.
 *
 *  2. If you become rich through modifications, related works/services, or supporting the original work,
 *  share the love. Only a dick would make loads off this work and not buy the original work's
 *  creator(s) a pint.
 *
 *  3. Code is provided with no warranty. Using somebody else's code and bitching when it goes wrong makes
 *  you a DONKEY dick. Fix the problem yourself. A non-dick would submit the fix back.
 */
 
 
 
 package extensions

import spark.Request
import spark.Response
import utils.Config
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

/**
 * Created by alewis on 15/11/2016.
 */


fun StringBuilder.delete(string: String) {
    val i = this.indexOf(string)
    if (i != -1) {
        this.delete(i, i + string.length)
    }
}

fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
    return this.bufferedReader(charset).use { it.readText() }
}

fun File.doesNotExist(): Boolean {
    return !this.exists()
}

fun Request.forwardedIP(): String {
    var forwardedIP = ""
    try {
        forwardedIP = this.raw().getHeader("X-Forwarded-For")
    } catch (e: Exception) { return forwardedIP }
    return forwardedIP
}

fun Response.managedRedirect(request: Request, urlSuffix: String) {
    if (Config.getProperty("using_ssl_on_proxy").toBoolean()) {
        httpsRedirect(request, urlSuffix)
    } else {
        redirect(urlSuffix)
    }
}

fun Response.httpsRedirect(request: Request, urlSuffix: String) {
    redirect(request.url().replace(request.uri(), "").replace("http", "https") + urlSuffix)
}

fun String.leftPad(padding: String): String {
    return padding+this
}