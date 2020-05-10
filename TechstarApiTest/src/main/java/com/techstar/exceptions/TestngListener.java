package com.techstar.exceptions;

//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.Set;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * 用例级别的监控  参考如下
 * https://www.wandouip.com/t5i353696/
 * https://www.jianshu.com/p/f40ad9086af8
 * https://www.cnblogs.com/lixiaowei395659729/p/8745051.html
 * @author techstar
 *
 */
//用例级别的监控
public class TestngListener extends TestListenerAdapter {
 private static Logger logger = Logger.getLogger(TestngListener.class);
 @Override
 public void onTestFailure(ITestResult tr) {
     super.onTestFailure(tr);
     // 对于dataProvider的用例，每次成功后，重置Retry次数
     FailedRetry retry = (FailedRetry) tr.getMethod().getRetryAnalyzer();
     retry.reset();
     logger.info(tr.getName() + " Failure");
 }

 @Override
 public void onTestSkipped(ITestResult tr) {
     super.onTestSkipped(tr);
     logger.info(tr.getName() + " Skipped");
 }

 @Override
 public void onTestSuccess(ITestResult tr) {
     super.onTestSuccess(tr);
     // 对于dataProvider的用例，每次成功后，重置Retry次数
     FailedRetry retry = (FailedRetry) tr.getMethod().getRetryAnalyzer();
     retry.reset();
     logger.info(tr.getName() + " Success");
 }

 // 统计当前已经执行了多少个用例 及状态
 @Override
 public void onTestStart(ITestResult tr) {
     super.onTestStart(tr);
     logger.info(tr.getName() + " Start");
     ITestContext context = tr.getTestContext();
     int passedCount = context.getPassedTests().size();
     int faileCount = context.getFailedTests().size();
     int skipCount = context.getSkippedTests().size();
     int totalCount = passedCount + faileCount + skipCount;
     System.out.println("成功个数：" + passedCount);
     System.out.println("失败个数：" + faileCount);
     System.out.println("跳过个数：" + skipCount);
     System.out.println("当前执行总个数：" + totalCount);
 }
 @Override
 public void onFinish(ITestContext testContext) {
     super.onFinish(testContext);
     Iterator<ITestResult> listOfFailedTests = testContext.getFailedTests().getAllResults().iterator();
     while (listOfFailedTests.hasNext()) {
         ITestResult failedTest = (ITestResult) listOfFailedTests.next();
         ITestNGMethod method = failedTest.getMethod();
         if (testContext.getFailedTests().getResults(method).size() > 1) {
             listOfFailedTests.remove();
         } else {
             if (testContext.getPassedTests().getResults(method).size() > 0) {
                 listOfFailedTests.remove();
             }

         }
     }
 }
// // 减去失败重试的次数，即使有失败重试，总用例数保持不变
// @Override
// public void onFinish(ITestContext testContext) {
//     super.onFinish(testContext);
//
//     // List of test results which we will delete later
//     ArrayList<ITestResult> testsToBeRemoved = new ArrayList<ITestResult>();
//     // collect all id's from passed test
//     Set<Integer> passedTestIds = new HashSet<Integer>();
//     for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
//         logger.info("PassedTests = " + passedTest.getName());
//         passedTestIds.add(getId(passedTest));
//     }
//
//     Set<Integer> failedTestIds = new HashSet<Integer>();
//     for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
//         logger.info("failedTest = " + failedTest.getName());
//         // id = class + method + dataprovider
//         int failedTestId = getId(failedTest);
//
//         // if we saw this test as a failed test before we mark as to be deleted
//         // or delete this failed test if there is at least one passed version
//         if (failedTestIds.contains(failedTestId) || passedTestIds.contains(failedTestId)) {
//             testsToBeRemoved.add(failedTest);
//         } else {
//             failedTestIds.add(failedTestId);
//         }
//     }
//
//     // finally delete all tests that are marked
//     for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext();) {
//         ITestResult testResult = iterator.next();
//         if (testsToBeRemoved.contains(testResult)) {
//             logger.info("Remove repeat Fail Test: " + testResult.getName());
//             iterator.remove();
//         }
//     }
// }
//
// private int getId(ITestResult result) {
//     int id = result.getTestClass().getName().hashCode();
//     id = id + result.getMethod().getMethodName().hashCode();
//     id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
//     return id;
// }
}