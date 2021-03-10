package onlineShop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import onlineShop.model.Customer;
import onlineShop.service.CustomerService;

@Controller   //这里只能用controller，不能用component
public class RegistrationController {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/customer/registration", method = RequestMethod.GET)
    public ModelAndView getRegistrationForm() {
        Customer customer = new Customer();
        return new ModelAndView("register", "customer", customer);
        //返还一个页面， 根据viewName找到jsp文件，register.jsp(写好的网页文件)，将表单内容绑定到这个customer
    }

    @RequestMapping(value = "/customer/registration", method = RequestMethod.POST)
    public ModelAndView registerCustomer(@ModelAttribute(value = "customer") Customer customer,
                                         BindingResult result) {
        //把用户信息放进result这个对象
        ModelAndView modelAndView = new ModelAndView();
        //check 用户信息有没有出错
        if (result.hasErrors()) {
            modelAndView.setViewName("register");
            return modelAndView;
        }

        //如果用户信息没有出错，把他加进DB表单存储
        customerService.addCustomer(customer);
        modelAndView.setViewName("login");
        modelAndView.addObject("registrationSuccess", "Registered Successfully. Login using username and password");
        return modelAndView;
    }
}
