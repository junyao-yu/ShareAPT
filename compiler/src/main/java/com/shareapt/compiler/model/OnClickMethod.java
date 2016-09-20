package com.shareapt.compiler.model;

import com.shareapt.annotation.OnClick;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 14:14
 * Email：yujunyao@yonglibao.com
 */
public class OnClickMethod {

    ExecutableElement mExecutableElement = null;
    private int[] methodArray;

    public OnClickMethod(Element element) throws IllegalArgumentException{
        if(element.getKind() != ElementKind.METHOD) {
            throw new IllegalArgumentException("not valid");
        }

        mExecutableElement = (ExecutableElement) element;
        OnClick onClick = mExecutableElement.getAnnotation(OnClick.class);
        methodArray = onClick.value();

        if(methodArray != null) {
            for(int id : methodArray) {
                if(id < 0) {
                    throw new IllegalArgumentException("not valid");
                }
            }
        }

    }

    public int[] getMethodArray() {
        return methodArray;
    }

    public Name getMethodName() {
        return mExecutableElement.getSimpleName();
    }

}
