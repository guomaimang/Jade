@file:Suppress("DEPRECATION")

package com.iems5722.jade.utils

import android.content.Context
import android.preference.PreferenceManager

object JwtUtils {

    private const val JWT_TOKEN_KEY = "jwt_token"

    // 存储 JWT Token
    fun storeJwtToken(context: Context, jwt: String) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().putString(JWT_TOKEN_KEY, jwt).apply()
    }

    // 获取 JWT Token
    fun getJwtToken(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(JWT_TOKEN_KEY, null)
    }

    // 清除 JWT Token
    fun clearJwtToken(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.edit().remove(JWT_TOKEN_KEY).apply()
    }

    // 检查 JWT 是否存在
    fun hasJwtToken(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.contains(JWT_TOKEN_KEY)
    }
}
