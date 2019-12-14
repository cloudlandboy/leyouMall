package com.leyou.search.client;

import com.leyou.search.LeyouSearchServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LeyouSearchServiceApplication.class)
public class CategoryClientTest {

    @Autowired
    CategoryClient categoryClient;

    @Test
    public void test() throws Exception {
        List<String> categoryNames = categoryClient.queryNamesByIds(Arrays.asList(1L, 2L, 3L));
        System.out.println(categoryNames);
    }
}