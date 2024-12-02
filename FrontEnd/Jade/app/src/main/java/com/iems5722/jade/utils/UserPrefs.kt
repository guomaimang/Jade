package com.iems5722.jade.utils

import android.content.Context
import android.content.SharedPreferences

object UserPrefs {

    private const val PREF_NAME = "user_prefs"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_EMAIL = "email"
    private const val KEY_NICKNAME = "nickname"
    private const val KEY_JWT = "jwt"
    private const val KEY_AVATAR = "image_url"
    private const val SELECTED_TOPIC = "selected_topic"

    // 默认的用户数据
    private const val DEFAULT_IMAGE_URL =
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181327978.png"
    private const val DEFAULT_USER_ID = "0000"
    private const val DEFAULT_EMAIL = "msc@ie.cuhk.edu.hk"
    private const val DEFAULT_NICKNAME = "Guest"
    private const val DEFAULT_JWT = ""

    // 获取 SharedPreferences 实例
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // 初始化用户数据，保证首次启动时有默认值
    fun initializeUserData(context: Context) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()

        // 检查是否有用户数据，如果没有，则设置默认值
        if (prefs.getString(KEY_USER_ID, null) == null) {
            editor.putString(KEY_USER_ID, DEFAULT_USER_ID)
            editor.putString(KEY_EMAIL, DEFAULT_EMAIL)
            editor.putString(KEY_NICKNAME, DEFAULT_NICKNAME)
            editor.putString(KEY_JWT, DEFAULT_JWT)
            editor.putString(KEY_AVATAR, DEFAULT_IMAGE_URL)
            editor.apply()
        }
    }


    // 存储用户数据
    fun saveUserData(
        context: Context,
        userId: String,
        email: String,
        nickname: String,
        avatar: String = "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181327978.png",
        jwt: String
    ) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_NICKNAME, nickname)
        editor.putString(KEY_JWT, jwt)
        editor.putString(KEY_AVATAR, avatar)
        editor.apply()
    }

    fun setSelectedTopic(context: Context, topicId: Int) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putInt(SELECTED_TOPIC, topicId).apply()
    }

    fun getSelectedTopic(context: Context): Int {
        val prefs = getSharedPreferences(context)
        return prefs.getInt(SELECTED_TOPIC, -1)
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

    // 获取用户头像
    fun getAvatar(context: Context): String? {
        val prefs = getSharedPreferences(context)
        return prefs.getString(KEY_AVATAR, null)
    }

    // 重置用户token (登录过的用户)
    fun resetJWT(context: Context) {
        val prefs = getSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_JWT, DEFAULT_JWT)
        editor.apply()
    }
}
