package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@JobHandler
@Component
public class DemoNalaJob extends IJobHandler {

    @Resource
    private HandlerDao handlerDao;

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        System.out.println("hellow word nala");
        return ReturnT.SUCCESS;
    }
}
