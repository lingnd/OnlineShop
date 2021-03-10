package onlineShop.service;

import onlineShop.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import onlineShop.dao.CustomerDao;
import onlineShop.model.Customer;

@Service //意思 这里class与business logic有关，也是相当于@component  @Repository
//这里是让Spring创建对象
public class CustomerService {

    @Autowired  //spring把创建好的对象inject进来
    private CustomerDao customerDao; //spring自动创建

    //
    public void addCustomer(Customer customer) { //controller传进来的
        customer.getUser().setEnabled(true); //这样才能使用户登陆成功

        Cart cart = new Cart();
        customer.setCart(cart);

        customerDao.addCustomer(customer);
    }

    public Customer getCustomerByUserName(String userName) {
        return customerDao.getCustomerByUserName(userName);
    }
}
