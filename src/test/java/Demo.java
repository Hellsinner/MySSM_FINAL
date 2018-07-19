import com.myssm.common.User;
import com.myssm.common.UserMapper;
import com.myssm.mybatis.MySqlSession;
import com.myssm.utils.ClassUtils;
import com.myssm.utils.ProUtils;
import org.junit.Test;

import java.nio.file.NoSuchFileException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo {
    @Test
    public void run() throws NoSuchFileException {
        ProUtils proUtils = new ProUtils("pro.properties");
        String base_package = proUtils.getString("BASE_PACKAGE");

        ClassUtils.doScanner(base_package);
        ClassUtils.doClass();

        System.out.println(ClassUtils.getClassNameList());
        System.out.println(ClassUtils.getClassSet());
    }

    @Test
    public void run1(){
        MySqlSession sqlSession = new MySqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setEmp_no("admin");
        user.setEmp_pass("admin");
        User user1 = mapper.findUser(user);
        System.out.println(user1);
    }

    @Test
    public void run2(){
        MySqlSession sqlSession = new MySqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.findByNo("admin");
        System.out.println(user);
    }

    @Test
    public void run4(){
        MySqlSession sqlSession = new MySqlSession();

        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        System.out.println(mapper == null);
        List<User> byType = mapper.findByType(0);
        System.out.println(byType);
    }

    @Test
    public void run3() throws IllegalAccessException, InstantiationException {
        Class clazz = List.class;

        boolean assignableFrom = Collection.class.isAssignableFrom(clazz);

        System.out.println(assignableFrom);
    }

    @Test
    public void  run5(){
        String reg = "(?<=\\$\\{).*(?=})";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher("${name}");
        if(!matcher.find())
            throw new RuntimeException();
        System.out.println(matcher.group());
    }

}
