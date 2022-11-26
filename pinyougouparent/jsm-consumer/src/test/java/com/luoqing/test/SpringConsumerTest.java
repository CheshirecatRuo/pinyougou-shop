package com.luoqing.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/spring-mq.xml")
public class SpringConsumerTest {

    @Test
    public void testSleep() throws IOException {
        System.in.read();
    }
}
