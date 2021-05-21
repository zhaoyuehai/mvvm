package com.yuehai.mvvm.util

import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


/**
 * Created by zhaoyuehai 2021/5/6
 */
object StringUtil {
    /**
     * ^ 匹配输入字符串开始的位置
     * \d 匹配一个或多个数字，其中 \ 要转义，所以是 \\d
     * $ 匹配输入字符串结尾的位置
     */
    private val HK_PATTERN = Pattern.compile("^([5689])\\d{7}$")
    private val CHINA_PATTERN =
        Pattern.compile("^((13[0-9])|(14[0,14-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$")

    /**
     * 大陆号码或香港号码均可
     */
    @Throws(PatternSyntaxException::class)
    fun isPhoneLegal(str: String): Boolean {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str)
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 145,147,149
     * 15+除4的任意数(不要写^4，这样的话字母也会被认为是正确的)
     * 166
     * 17+3,5,6,7,8
     * 18+任意数
     * 198,199
     */
    @Throws(PatternSyntaxException::class)
    fun isChinaPhoneLegal(str: String): Boolean {
        val m: Matcher = CHINA_PATTERN.matcher(str)
        return m.matches()
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    @Throws(PatternSyntaxException::class)
    fun isHKPhoneLegal(str: String): Boolean {
        val m: Matcher = HK_PATTERN.matcher(str)
        return m.matches()
    }

    fun formatJson(jsonStr: String?): String {
        if (null == jsonStr || "" == jsonStr) return ""
        val sb = StringBuilder("\t\n")
        var last: Char
        var current = '\u0000'
        var indent = 0
        for (element in jsonStr) {
            last = current
            current = element
            when (current) {
                '{', '[' -> {
                    sb.append(current)
                    sb.append('\n')
                    indent++
                    addIndentBlank(sb, indent)
                }
                '}', ']' -> {
                    sb.append('\n')
                    indent--
                    addIndentBlank(sb, indent)
                    sb.append(current)
                }
                ',' -> {
                    sb.append(current)
                    if (last != '\\') {
                        sb.append('\n')
                        addIndentBlank(sb, indent)
                    }
                }
                else -> sb.append(current)
            }
        }
        return sb.toString()
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private fun addIndentBlank(sb: StringBuilder, indent: Int) {
        for (i in 0 until indent) {
            sb.append('\t')
        }
    }

    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    fun decodeUnicode(theString: String): String {
        var aChar: Char
        val len = theString.length
        val outBuffer = StringBuffer(len)
        var x = 0
        while (x < len) {
            aChar = theString[x++]
            if (aChar == '\\') {
                aChar = theString[x++]
                if (aChar == 'u') {
                    var value = 0
                    for (i in 0..3) {
                        aChar = theString[x++]
                        value = when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> (value shl 4) + aChar.toInt() - '0'.toInt()
                            'a', 'b', 'c', 'd', 'e', 'f' -> (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                            'A', 'B', 'C', 'D', 'E', 'F' -> (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                            else -> throw IllegalArgumentException(
                                "Malformed   \\uxxxx   encoding."
                            )
                        }
                    }
                    outBuffer.append(value.toChar())
                } else {
                    when (aChar) {
                        't' -> aChar = '\t'
                        'r' -> aChar = '\r'
                        'n' -> aChar = '\n'
//                        'f' -> aChar = '\f'
                    }
                    outBuffer.append(aChar)
                }
            } else outBuffer.append(aChar)
        }
        return outBuffer.toString()
    }
}