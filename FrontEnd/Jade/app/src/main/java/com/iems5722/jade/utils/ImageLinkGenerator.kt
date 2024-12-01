package com.iems5722.jade.utils

object ImageLinkGenerator {

    // 定义10个图片链接模板
    private val imageLinks = listOf(
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181327978.png", // 图片 0
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181328123.png", // 图片 1
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181328456.png", // 图片 2
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181328789.png", // 图片 3
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181329012.png", // 图片 4
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181329345.png", // 图片 5
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181329678.png", // 图片 6
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181330012.png", // 图片 7
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181330345.png", // 图片 8
        "https://cdn.jsdelivr.net/gh/MonsterXia/Piclibrary/Pic202411181330678.png"  // 图片 9
    )

    /**
     * 根据用户ID生成对应的一张图片链接
     * @param userId 用户ID，输入参数
     * @return 返回与用户ID对应的图片链接
     */
    fun getUserImage(userId: Int): String {
        // 通过 userId 对应的数字进行取余
        val index = userId % 10 // 计算 userId 的哈希值并取余 10

        // 根据取余的值返回对应的图片链接
        return imageLinks[index]
    }
}
