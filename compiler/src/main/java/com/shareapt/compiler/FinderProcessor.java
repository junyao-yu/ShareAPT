package com.shareapt.compiler;

import com.google.auto.service.AutoService;
import com.shareapt.annotation.BindView;
import com.shareapt.annotation.OnClick;
import com.shareapt.compiler.model.BindViewField;
import com.shareapt.compiler.model.OnClickMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * Auth：yujunyao
 * Since: 2016/9/19 10:36
 * Email：yujunyao@yonglibao.com
 */
@AutoService(Processor.class)
public class FinderProcessor extends AbstractProcessor{

    private Filer mFiler = null;//文件辅助类
    private Elements mElementsUtils = null;//元素辅助类
    private Messager mMessager = null;//日志辅助类

    private Map<String, AnnotatedClass> mAnnotatedClassMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementsUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {//使用 RoundEnvironment 参数来查询被特定注解标注的元素
        printInfo("start--------------------------------------------");
        mAnnotatedClassMap.clear();

        try {
            processBindView(roundEnv);
            processOnClick(roundEnv);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
            printError(e.getMessage());
        }

        for(AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {

            try {
                annotatedClass.generateFinder().writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
                printError(e.getMessage());
            }

        }
        printInfo("end--------------------------------------------");
        return true;
    }

    private AnnotatedClass getAnnotatedClass(Element element) {
        //element.getEnclosingElement();// get super element
        //element.getEnclosedElements();// get sub element
        TypeElement classElement = (TypeElement) element.getEnclosingElement();
        String fullClassName = classElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullClassName);
        if(annotatedClass == null) {
            annotatedClass = new AnnotatedClass(classElement, mElementsUtils);
            mAnnotatedClassMap.put(fullClassName, annotatedClass);
        }
        return annotatedClass;
    }

    private void processBindView(RoundEnvironment roundEnv) throws IllegalArgumentException{
        for(Element element : roundEnv.getElementsAnnotatedWith(BindView.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            BindViewField bindViewField = new BindViewField(element);
            annotatedClass.addField(bindViewField);
            printInfo("processBindView-----------------------------------------");
        }
    }

    private void processOnClick(RoundEnvironment roundEnv) throws IllegalArgumentException{
        for(Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            OnClickMethod onClickMethod = new OnClickMethod(element);
            annotatedClass.addMethod(onClickMethod);
            printInfo("processOnClick-----------------------------------------");
        }
    }

    private void printError(String error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error);
    }

    private void printInfo(String info) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, info);
    }

}
