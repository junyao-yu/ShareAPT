package com.shareapt.compiler;

import com.squareup.javapoet.ClassName;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 15:20
 * Email：yujunyao@yonglibao.com
 */
public class TypeUtil {

    public static final ClassName FINDER = ClassName.get("com.shareaptfinder", "Finder");
    public static final ClassName ANDROID_ON_CLICK_LISTENER = ClassName.get("android.view", "View", "OnClickListener");
    public static final ClassName ANDROID_VIEW = ClassName.get("android.view", "View");

}
