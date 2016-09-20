package com.shareapt.compiler;

import com.shareapt.compiler.model.BindViewField;
import com.shareapt.compiler.model.OnClickMethod;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 11:37
 * Email：yujunyao@yonglibao.com
 */
public class AnnotatedClass {

    /**
     - VariableElement //代表成员变量
     - ExecutableElement //代表类中的方法
     - TypeElement //代表类
     - PackageElement //代表Package
     */
    private TypeElement mClassElement = null;
    private Elements mElementUtils = null;
    private List<BindViewField> mFieldList = null;
    private List<OnClickMethod> mMethodList = null;

    public AnnotatedClass(TypeElement mClassElement, Elements elements) {
        this.mClassElement = mClassElement;
        this.mElementUtils = elements;
        this.mFieldList = new ArrayList<>();
        this.mMethodList = new ArrayList<>();
    }

    public void addField(BindViewField field) {
        mFieldList.add(field);
    }

    public void addMethod(OnClickMethod method) {
        mMethodList.add(method);
    }

    /**
     * https://github.com/square/javapoet
     */
    public JavaFile generateFinder() {
        MethodSpec.Builder injectMethodBuilder = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mClassElement.asType()), "host", Modifier.FINAL);

        for(BindViewField bindViewField : mFieldList) {
            injectMethodBuilder.addStatement("host.$N = ($T)(host.findViewById($L))", bindViewField.getFieldName(),
                    ClassName.get(bindViewField.getFieldType()), bindViewField.getResID());
        }

        if(mMethodList.size() > 0) {
            injectMethodBuilder.addStatement("$T listener", TypeUtil.ANDROID_ON_CLICK_LISTENER);

            for(OnClickMethod onClickMethod : mMethodList) {

                TypeSpec listener = TypeSpec.anonymousClassBuilder("")
                        .addSuperinterface(TypeUtil.ANDROID_ON_CLICK_LISTENER)
                        .addMethod(MethodSpec.methodBuilder("onClick")
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.VOID)
                                .addParameter(TypeUtil.ANDROID_VIEW, "view")
                                .addStatement("host.$N()", onClickMethod.getMethodName())
                                .build())
                        .build();
                injectMethodBuilder.addStatement("listener = $L ", listener);

                for(int id : onClickMethod.getMethodArray()) {
                    injectMethodBuilder.addStatement("host.findViewById($L).setOnClickListener(listener)", id);
                }
            }
        }

        TypeSpec finderClass = TypeSpec.classBuilder(mClassElement.getSimpleName() + "$$Finder")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.FINDER, TypeName.get(mClassElement.asType())))
                .addMethod(injectMethodBuilder.build())
                .build();
        String packageName = mElementUtils.getPackageOf(mClassElement).getQualifiedName().toString();

        return JavaFile.builder(packageName, finderClass).build();
    }

}
