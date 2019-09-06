/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ExecutorJobGroupThread
 * Author:   Liubing
 * Date:     2018/9/29 14:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xxl.job.core.thread;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.XxlJobGroup;
import com.xxl.job.core.executor.XxlJobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Liubing
 * @create 2018/9/29
 * @since 1.0.0
 */
public class ExecutorJobGroupThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(ExecutorJobGroupThread.class);

    private static ExecutorJobGroupThread instance = new ExecutorJobGroupThread();

    public static ExecutorJobGroupThread getInstance(){
        return instance;
    }

    private Thread jobGroupThread;

    public void start(final String appName) throws InterruptedException {
        // valid
        if (appName==null || appName.trim().length()==0) {
            logger.warn(">>>>>>>>>>> xxl-job, executor initGroup config fail, appName is null.");
            return;
        }
        if(appName.length() < 4){
            logger.warn(">>>>>>>>>>> xxl-job, executor initGroup config fail,jobgroup_field_appName_length");
            return;
        }
        if (XxlJobExecutor.getAdminBizList() == null) {
            logger.warn(">>>>>>>>>>> xxl-job, executor initGroup config fail, adminAddresses is null.");
            return;
        }
        jobGroupThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AdminBiz adminBiz = XxlJobExecutor.getAdminBizList().get(0);
                    XxlJobGroup xxlJobGroup = new XxlJobGroup();
                    xxlJobGroup.setAppName(appName);
                    xxlJobGroup.setOrder(9);
                    xxlJobGroup.setTitle(appName);
                    xxlJobGroup.setAddressType(0);
                    ReturnT<String> jobGroupResult = adminBiz.initGroup(xxlJobGroup);
                    if (jobGroupResult != null && ReturnT.SUCCESS_CODE == jobGroupResult.getCode()) {
                        jobGroupResult = ReturnT.SUCCESS;
                        logger.info(">>>>>>>>>>> xxl-job registry success, registryParam:{}, registryResult:{}", new Object[]{xxlJobGroup, jobGroupResult});
                    } else {
                        logger.info(">>>>>>>>>>> xxl-job registry fail, registryParam:{}, registryResult:{}", new Object[]{xxlJobGroup, jobGroupResult});
                    }
                }catch (Exception e){
                    logger.error(e.getMessage(), e);
                }
            }
        });
        jobGroupThread.start();
        jobGroupThread.join();
    }
}
