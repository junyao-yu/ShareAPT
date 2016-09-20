package com.shareaptfinder;

import java.util.HashMap;
import java.util.Map;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 15:22
 * Email：yujunyao@yonglibao.com
 */
public class InjectFinder {

    private static final Map<String, Finder> FINDER_MAP = new HashMap<>();

    public static void inject(Object host) {
        String className = host.getClass().getName();

        try {
            Finder finder = FINDER_MAP.get(className);
            if(finder == null) {
                Class<?> finderClass = Class.forName(className + "$$Finder");
                finder = (Finder) finderClass.newInstance();
                FINDER_MAP.put(className, finder);
            }
            finder.inject(host);
        }catch (Exception e){}

    }

}
