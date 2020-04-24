/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Original reference: https://gist.github.com/AkshayChordiya/15cfe7ca1842d6b959e77c04a073a98f
 */

package com.sromanuk.ipvigilante.repository.utils

import android.util.Log
import androidx.annotation.Nullable
import androidx.collection.ArrayMap
import retrofit2.Response
import java.io.IOException
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


internal const val UNKNOWN_CODE = -1

class ApiResponse<T> {
    val code: Int

    @Nullable
    val body: T?

    @Nullable
    val errorMessage: String?
    val links: MutableMap<String, String>

    constructor(error: Throwable) {
        code = 500
        body = null
        errorMessage = error.message
        links = Collections.emptyMap()
    }

    constructor(response: Response<T>) {
        code = response.code()
        if (response.isSuccessful) {
            body = response.body()
            errorMessage = null
        } else {
            var message: String? = null
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody()!!.string()
                } catch (ignored: IOException) {
                    Log.e("APIResponse", "error while parsing response")
                    ignored.printStackTrace()
                }
            }
            if (message == null || message.trim { it <= ' ' }.length == 0) {
                message = response.message()
            }
            errorMessage = message
            body = null
        }
        val linkHeader = response.headers()["link"]
        if (linkHeader == null) {
            links = Collections.emptyMap()
        } else {
            links = ArrayMap()
            val matcher: Matcher = LINK_PATTERN.matcher(linkHeader)
            while (matcher.find()) {
                val count: Int = matcher.groupCount()
                if (count == 2) {
                    links[matcher.group(2)] = matcher.group(1)
                }
            }
        }
    }

    val isSuccessful: Boolean
        get() = code >= 200 && code < 300

    val nextPage: Int?
        get() {
            val next = links[NEXT_LINK] ?: return null
            val matcher: Matcher = PAGE_PATTERN.matcher(next)
            return if (!matcher.find() || matcher.groupCount() !== 1) {
                null
            } else try {
                matcher.group(1).toInt()
            } catch (ex: NumberFormatException) {
                Log.w("APIResponse","cannot parse next page from ${next}")
                null
            }
        }

    companion object {
        private val LINK_PATTERN: Pattern = Pattern
            .compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"")
        private val PAGE_PATTERN: Pattern = Pattern.compile("page=(\\d)+")
        private const val NEXT_LINK = "next"
    }
}