package com.iems5722.jade.utils

import android.content.Context
import android.content.SharedPreferences

object UserPrefs {

    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "email"
    private const val KEY_NICKNAME = "nickname"
    private const val KEY_JWT = "jwt"

    // 获取 SharedPreferences 实例
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 存储用户数据
    fun saveUserData(
        context: Context,
        userId: String,
        email: String,
        nickname: String,
        jwt: String
    ) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NICKNAME, nickname)
        editor.putString(KEY_JWT, jwt)
        editor.apply()
    }

    // 获取用户ID
    fun getUserId(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_USER_ID, null)
    }

    // 获取用户邮箱
    fun getEmail(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_EMAIL, null)
    }

    // 获取用户昵称
    fun getNickname(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_NICKNAME, null)
    }

    // 获取用户JWT
    fun getJwt(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_JWT, null)
    }

    // 清除所有用户数据
    fun clearUserData(context: Context) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
