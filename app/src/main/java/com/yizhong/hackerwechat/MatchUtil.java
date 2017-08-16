package com.yizhong.hackerwechat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lunger on 2017/8/16.
 */

public class MatchUtil {

    public static boolean match4number(String str){

        String regEx = "^\\d{4}$";

        // 编译正则表达式

        Pattern pattern = Pattern.compile(regEx);

        Matcher matcher = pattern.matcher(str);

        // 字符串是否与正则表达式相匹配

        return matcher.matches();

    }
}
