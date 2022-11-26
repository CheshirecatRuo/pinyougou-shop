import com.pinyougou.pojo.TbItem;
import com.pinyougou.solr.SolrUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SolrImportTest {

    private SolrUtil solrUtil;
    @Before
    public void init()
    {
        ApplicationContext act = new ClassPathXmlApplicationContext("classpath:spring/spring-solr.xml");
        solrUtil = act.getBean(SolrUtil.class);
    }

    @Test
    public void testAdd()
    {
        solrUtil.batchAddDynamic();
    }

    @Test
    public void deleteAll()
    {
        solrUtil.deleteAll();
    }

    @Test
    public void testGetBySpec()
    {
        solrUtil.queryByCondition("机身内存", "16G");
    }

    @Test
    public void testMap()
    {
        ArrayList<User> users = new ArrayList<>();
        HashMap<String, String> map1 = new HashMap();
        HashMap<String, String> map2 = new HashMap();

        map1.put("zhangsan", "zhansgsansan");
        map2.put("lisi", "lisisi");
        users.add(new User("zhangsan", map1));
        users.add(new User("lisi", map2));
        System.out.println(users);

        for (User user : users)
        {
            user.setPa(map2);
        }

        System.out.println(users);
    }

    class User
    {
        private String name;
        private Map<String, String> pa;

        public User(String name, Map<String, String> pa) {
            this.name = name;
            this.pa = pa;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getPa() {
            return pa;
        }

        public void setPa(Map<String, String> pa) {
            this.pa = pa;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", pa=" + pa +
                    '}';
        }
    }
}
