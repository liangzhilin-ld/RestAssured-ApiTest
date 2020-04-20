package com.techstar.exceptions;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

//import com.iiaccount.exceptions.ErrorException;

/*
 *案例执行抛异常则重试
 */
public class FailedRetry implements IRetryAnalyzer {
    private int retryCount = 1;
    private static final int maxRetryCount = 2;

    public boolean retry(ITestResult iTestResult) {
        //抛出异常则重跑失败案例
        if (iTestResult.getThrowable() instanceof ErrorException && retryCount % maxRetryCount != 0) {
            retryCount++;
            System.out.println("11111111");
            return true;
        } else {
            retryCount = 1;
            return false;
        }
//        if (retryCount <=maxRetryCount) {
//        	retryCount++;
//            return true;
//        }
//        return false;
    }
    // 用于重置retryCount，针对使用dataProvider的用例结合TestngListener失败重试处理
    public void reset() {
    	retryCount = 1;
    }
}