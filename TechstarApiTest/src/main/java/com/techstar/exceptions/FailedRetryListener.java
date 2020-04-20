package com.techstar.exceptions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.annotations.ITestAnnotation;


/*
 *实现IAnnotationTransformer接口，修改@Test的retryAnalyzer属性
 */
public class FailedRetryListener implements IAnnotationTransformer {
	@Override
    public void transform(ITestAnnotation iTestAnnotation, Class aClass, Constructor constructor, Method method) {
        {
            IRetryAnalyzer retry = iTestAnnotation.getRetryAnalyzer();
            if (retry == null) {
                iTestAnnotation.setRetryAnalyzer(FailedRetry.class);
            }
        }
    }
}
