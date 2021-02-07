package com.sprout.app.network

import android.util.Log
import okhttp3.*
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import okio.BufferedSource
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException


class LogInter : Interceptor {
    private val TAG = "LogInter"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val url = request.url()
        Log.i(TAG, url.toString())
        val response: Response = chain.proceed(request)
        val responseBody: ResponseBody = response.body()!!
        val contentLength = responseBody.contentLength()

        //注意 >>>>>>>>> okhttp3.4.1这里变成了 !HttpHeader.hasBody(response)
        //if (!HttpEngine.hasBody(response)) {
        if (!HttpHeaders.hasBody(response)) { //HttpHeader -> 改成了 HttpHeaders，看版本进行选择
            //END HTTP
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
        } else {
            val source: BufferedSource = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer: Buffer = source.buffer()
            var charset: Charset = UTF8
            val contentType: MediaType? = responseBody.contentType()
            if (contentType != null) {
                charset = try {
                    contentType.charset(UTF8)!!
                } catch (e: UnsupportedCharsetException) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response
                }
            }
            if (!isPlaintext(buffer)) {
                Log.i(TAG, "<-- END HTTP (binary " + buffer.size.toString() + "-byte body omitted)")
                return response
            }
            if (contentLength != 0L) {
                val result: String = buffer.clone().readString(charset)
                Log.i(TAG, result)
                //获取到response的body的string字符串
                //do something .... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
        }
        return response
    }

    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding: String? = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    companion object {
        private val UTF8: Charset = Charset.forName("UTF-8")

        @Throws(EOFException::class)
        fun isPlaintext(buffer: Buffer): Boolean {
            return try {
                val prefix = Buffer()
                val byteCount = if (buffer.size < 64) buffer.size else 64.toLong()
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint: Int = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                true
            } catch (e: EOFException) {
                false // Truncated UTF-8 sequence.
            }
        }
    }
}