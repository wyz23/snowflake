package com.example.snowflake.service;

import com.example.snowflake.util.IdGeneratorSnowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class OrderService {

    @Autowired
    private IdGeneratorSnowflake idGeneratorSnowflake;

    public String getIDBySnowFlake() {
        //用多线程方式实验,5个线程
        //就像同时有20个人去办理业务，只有五个柜台
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        for (int i = 1; i <= 20; i++) {
            threadPool.submit(() -> {
                System.out.println(idGeneratorSnowflake.snowflakeId());
            });
        }
        threadPool.shutdown();
        return "hello, snowflake";
    }
}
