package com.iems5722.jade.utils

object ImageLinkGenerator {

    // 定义10个适合用作用户头像的图片链接模板
    private val imageLinks = listOf(
        "https://www.w3schools.com/w3images/avatar2.png", // 图片 0
        "https://www.w3schools.com/w3images/avatar3.png", // 图片 1
        "https://www.w3schools.com/w3images/avatar5.png", // 图片 2
        "https://www.w3schools.com/w3images/avatar6.png", // 图片 3
        "https://www.w3schools.com/w3images/avatar4.png", // 图片 4
        "https://www.w3schools.com/w3images/avatar1.png", // 图片 5
        "https://upload.wikimedia.org/wikipedia/commons/7/7c/Profile_avatar_placeholder_large.png", // 图片 6
        "https://upload.wikimedia.org/wikipedia/commons/4/46/Default-avatar.png", // 图片 7
        "https://upload.wikimedia.org/wikipedia/commons/a/a5/Placeholder_for_avatar_image.png", // 图片 8
        "https://upload.wikimedia.org/wikipedia/commons/8/88/Portrait_placeholder.png"  // 图片 9
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
