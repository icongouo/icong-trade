package com.shangan.trade.coupon;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.coupon.db.dao.TaskDao;
import com.shangan.trade.coupon.db.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {

    @Autowired
    private TaskDao  taskDao;

    @Test
    public void insertTaskTest(){
        Task task = new Task();
        task.setStatus(0);
        task.setTryCount(0);
        task.setBizType("send_coupon");
        task.setBizId(UUID.randomUUID().toString());
        task.setBizParam("userId:123,batchId:456");
        task.setModifiedTime(new Date());
        task.setCreateTime(new Date());
        taskDao.insertTask(task);

    }

    @Test
    public void queryTaskTest(){
        Task task = taskDao.queryTaskById(1L);
        task.setTryCount(1);
        taskDao.update(task);
    }

    @Test
    public void queryFailedTest() {
        List<Task> tasks = taskDao.queryFailedTasks();
        for (Task task : tasks) {
            System.out.println(JSON.toJSONString(task));
        }
    }
}
