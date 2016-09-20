package com.shareapt.compiler.model;

import com.shareapt.annotation.BindView;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 11:41
 * Email：yujunyao@yonglibao.com
 */
public class BindViewField {

    private VariableElement mVariableElement = null;
    private int resID;

    public BindViewField(Element element) throws IllegalArgumentException{
        if(element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException("not valid");
        }

        mVariableElement = (VariableElement) element;
        BindView bindView = mVariableElement.getAnnotation(BindView.class);
        resID = bindView.value();

        if(resID < 0) {
            throw new IllegalArgumentException("not valid");
        }

    }

    public int getResID() {
        return resID;
    }

    public TypeMirror getFieldType() {
        return mVariableElement.asType();
    }

    public Name getFieldName() {
        return mVariableElement.getSimpleName();
    }
}
