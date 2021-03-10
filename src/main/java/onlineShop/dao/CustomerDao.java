package onlineShop.dao;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Authorities;
import onlineShop.model.Customer;
import onlineShop.model.User;

@Repository  //spring 提供的annotation，使用的时候，这个class spring会自动创建
//也可以用@Component
public class CustomerDao {

    @Autowired
    private SessionFactory sessionFactory; //在ApplicationConfig里已经定义好里

    //保存用户注册信息
    public void addCustomer(Customer customer) {
        //给用户分配一个权限
        Authorities authorities = new Authorities();
        authorities.setAuthorities("ROLE_USER");
        authorities.setEmailId(customer.getUser().getEmailId());
        Session session = null;

        try {
            session = sessionFactory.openSession(); //拿到session对象
            session.beginTransaction();  //开启事务，保证不同表格的相关数据都会保存成功或者都不成功，不会有的成功，有的失败
            session.save(authorities);
            session.save(customer);      //两个insert功能定义好
            session.getTransaction().commit(); //commit之后才会真正执行上面的操作
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();    //如果上面两个插入操作出现异常，就自动回到最初的状态。保证数据一致性。
        } finally {
            if (session != null) {
                session.close();      //要close session，让给其他线程使用，如果是null就不用close了
            }
        }
    }

    //登陆时check用户信息
    public Customer getCustomerByUserName(String userName) {
        //这里userName就是email
        User user = null;
        try (Session session = sessionFactory.openSession()) {
            //搜索条件，hibernte提供的接口
            Criteria criteria = session.createCriteria(User.class);
            //if "emailId" == email, 在User表的emialId里查找是否有对应email
            //.uniqueResult() 拿到User对像
            //拿回来时是Object，强制转换成（User）
            user = (User) criteria.add(Restrictions.eq("emailId", userName)).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user != null) {
            return user.getCustomer();
        }
        return null;  //为啥不用close session
    }
}
