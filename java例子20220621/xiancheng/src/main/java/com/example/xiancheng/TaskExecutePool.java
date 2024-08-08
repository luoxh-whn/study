package com.example.xiancheng;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Auther: zxh
 * @Date: 2022/3/20 09:17
 * @Description: 配置线程池
 */
@Configuration
@EnableAsync
public class TaskExecutePool {
    //核心线程数
    private static final Integer CORE_POOL_SIZE = 20;
    //最大线程数
    private static final Integer MAX_POOL_SIZE = 20;
    //缓存队列容量
    private static final Integer QUEUE_CAPACITY = 200;
    //线程活跃时间（秒）
    private static final Integer KEEP_ALIVE = 60;
    //默认线程名称前缀
    private static final String THREAD_NAME_PREFIX = "MyExecutor-";


    @Bean("MyAsyncTaskExecutor")
    public Executor myTaskAsyncPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAX_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE);
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}