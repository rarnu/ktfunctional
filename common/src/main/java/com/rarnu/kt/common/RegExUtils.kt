package com.rarnu.kt.common

import java.util.regex.Pattern

object RegExUtils {

    private fun regexMatch(str: String, regex: String) = Pattern.matches(regex, str)

    private val regNumbers = arrayOf(
            "^[0-9]*$",                                     // 数字
            "^([1-9][0-9]*)+(.[0-9]{1,2})?$",               // 非零开头的最多带两位小数的数字
            "^(\\-)?\\d+(\\.\\d{1,2})?$",                   // 带1-2位小数的正数或负数
            "^(\\-|\\+)?\\d+(\\.\\d+)?$",                   // 正数、负数、和小数
            "^[0-9]+(.[0-9]{2})?$",                         // 有两位小数的正实数
            "^[0-9]+(.[0-9]{1,3})?$",                       // 有1~3位小数的正实数
            "^[1-9]\\d*$",                                  // 非零的正整数
            "^-[1-9]\\d*$",                                 // 非零的负整数
            "^\\d+$",                                       // 非负整数
            "^-[1-9]\\d*|0$",                               // 非正整数
            "^\\d+(\\.\\d+)?$",                             // 非负浮点数
            "^((-\\d+(\\.\\d+)?)|(0+(\\.0+)?))$",           // 非正浮点数
            "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$",         // 正浮点数
            "^-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$",      // 负浮点数
            "^(-?\\d+)(\\.\\d+)?$"                          // 浮点数
    )

    private val regStrings = arrayOf(
            "^[\\u4e00-\\u9fa5]{0,}$",                  // 汉字
            "^[A-Za-z0-9]+$",                           // 英文和数字
            "^[A-Za-z]+$",                              // 由26个英文字母组成的字符串
            "^[A-Z]+$",                                 // 由26个大写英文字母组成的字符串
            "^[a-z]+$",                                 // 由26个小写英文字母组成的字符串
            "^[a-z0-9A-Z_]+$",                          // 由数字、26个英文字母或者下划线组成的字符串
            "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$",           // 中文、英文、数字包括下划线
            "^[\\u4E00-\\u9FA5A-Za-z0-9]+$",            // 中文、英文、数字但不包括下划线等符号
            "[^%&',;=?$\\x22]+",                        // 可以输入含有^%&',;=?$\"等字符
            "[^~\\x22]+"                                // 含有~的字符
    )

    fun isStringReg(str: String, type: Int) = regexMatch(str, regStrings[type])

    fun isNumberReg(str: String, type: Int) = regexMatch(str, regNumbers[type])

    fun isEmail(str: String) = regexMatch(str, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")

    fun isPhoneNumber(str: String) = regexMatch(str, "^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$")

    fun isCellPhoneNumber(str: String) = regexMatch(str, "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$")

    fun isChinesePhoneNumber(str: String) = regexMatch(str, "\\d{3}-\\d{8}|\\d{4}-\\d{7}")

    fun isIdCardNumber(str: String) = regexMatch(str, "^\\d{15}|\\d{18}$")

    fun isShortIdCardNumber(str: String) = regexMatch(str, "^([0-9]){7,18}(x|X)?$")

    fun isUrl(str: String) = regexMatch(str, "[a-zA-z]+://[^\\s]*")

    fun isDomain(str: String) = regexMatch(str, "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(/.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+/.?")

    fun isValidAccount(str: String) = regexMatch(str, "^[a-zA-Z][a-zA-Z0-9_]{5,31}$")

    fun isValidPassword(str: String) = regexMatch(str, "^[a-zA-Z]\\w{5,31}$")

    fun isStrongPassword(str: String) = regexMatch(str, "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,10}$")

    fun isDate(str: String) = regexMatch(str, "^\\d{4}-\\d{1,2}-\\d{1,2}")

    fun isValidXml(str: String) = regexMatch(str, "^([a-zA-Z]+-?)+[a-zA-Z0-9]+\\\\.[x|X][m|M][l|L]$")

    fun isBlankLine(str: String) = regexMatch(str, "\\n\\s*\\r")

    fun isValidHtml(str: String) = regexMatch(str, "<(\\S*?)[^>]*>.*?</\\1>|<.*? />")

    fun isValidQQNumber(str: String) = regexMatch(str, "[1-9][0-9]{4,}")

    fun isValidPostCode(str: String) = regexMatch(str, "[1-9]\\d{5}(?!\\d)")

    fun isValidIPAddress(str: String) = regexMatch(str, "((?:(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d)\\\\.){3}(?:25[0-5]|2[0-4]\\\\d|[01]?\\\\d?\\\\d))")

}