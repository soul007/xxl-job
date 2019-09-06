/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: ExecutorCheckSaveJobInfoThread
 * Author:   Liubing
 * Date:     2018/10/8 10:47
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.xxl.job.core.thread;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Liubing
 * @create 2018/10/8
 * @since 1.0.0
 */
public class ExecutorCheckSaveJobInfoThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(ExecutorCheckSaveJobInfoThread.class);

    private static ExecutorCheckSaveJobInfoThread instance = new ExecutorCheckSaveJobInfoThread();

    public static ExecutorCheckSaveJobInfoThread getInstance(){
        return instance;
    }

    private Thread jobInfoThread;

    public void start(final String appName){
        if(StringUtils.isEmpty(appName)){
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
        if(!CollectionUtils.isEmpty(XxlJobExecutor.jobHandlerMap())){
            jobInfoThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        AdminBiz adminBiz = XxlJobExecutor.getAdminBizList().get(0);
                        Set<String> executorHandlers = XxlJobExecutor.jobHandlerMap().keySet();
                        for(String executorHandler:executorHandlers){
                            ReturnT<String> jobInfoResult = adminBiz.initJobInfo(appName,executorHandler);
                            if (jobInfoResult != null && ReturnT.SUCCESS_CODE == jobInfoResult.getCode()) {
                                jobInfoResult = ReturnT.SUCCESS;
                                logger.info(">>>>>>>>>>> xxl-job initJobInfo success, registryParam:{}, registryResult:{}", new Object[]{executorHandler, jobInfoResult});
                            } else {
                                logger.info(">>>>>>>>>>> xxl-job initJobInfo fail, registryParam:{}, registryResult:{}", new Object[]{executorHandler, jobInfoResult});
                            }
                        }
                    }catch (Exception e){
                        logger.error(e.getMessage(), e);
                    }
                }
            });
            jobInfoThread.start();
        }
    }
}
