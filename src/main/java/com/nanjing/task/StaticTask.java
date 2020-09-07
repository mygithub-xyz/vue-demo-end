package com.nanjing.task;

import com.nanjing.dao.PeopleDao;
import com.nanjing.pojo.People;
import com.nanjing.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@Configuration
@EnableScheduling
public class StaticTask {
    @Autowired
    private PeopleDao peopleDao;
    People people = new People();
    private int i=1;
        @Scheduled(cron = "0/5 * * * * ?")
        @Scheduled(fixedRate = 5000)
        private  void configureTasks() {
            i++;
            if (i ==100) {
                try {
                    people.setPname("小李" + (i++));
                    people.setCountryid("" + i);
                    people.setCreatetime(DateUtils.getCurrentDate());
                    peopleDao.save(people);
                    System.out.println("success" + i);
                } catch (Exception e) {
                    System.out.println("false" + i);
            }
            }
    }
}
