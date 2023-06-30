package org.example;

import org.example.config.AppConfig;
import org.example.entitie.Customer;
import org.example.repository.ConnectionPool;
import org.example.repository.implement.CustomerRepo;
import org.example.service.CustomerService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class Main {


    static  List <String> customerDTO = List.of("1","tozya","zqqqqv","2","fifa","zxdvdvv","3","vasya","zxccvv","4","zaza","jhgggff","5","tyma","ddffgg","6","goga","sdddf","7","woldemar","ffdsss","8","naina","ghjdf");





    public static void main(String[] args) {




	AnnotationConfigApplicationContext context=new AnnotationConfigApplicationContext(AppConfig.class);


	try (context) {

	    Customer customer = context.getBean(CustomerService.class).get(1L);

	    System.out.println("1 " + customer);

	    customer.setPassword("blaоааbla");
	    context.getBean(CustomerService.class).update(customer);
	    System.out.println("2 from entity " + customer);
	Long current=0L;
		    for (Customer c:creareCustomerList()){
			current=current+1;
			customer = context.getBean(CustomerService.class).get(current);
			customer.setLogin(c.getLogin());
			customer.setPassword(c.getPassword());
			context.getBean(CustomerService.class).update(customer);
		    }

	  	    Customer updatedCustomer = context.getBean(CustomerService.class).get(1L);

	    System.out.println("3 from DB" + updatedCustomer);
	}

    }

    private static ArrayList<Customer> creareCustomerList(){
	ArrayList<Customer> customerArrayList=new ArrayList<>();
	for (int i = 0; i < customerDTO.size();) {
	 Customer customer=new Customer();
	customer.setId(Long.valueOf(customerDTO.get(i)));
	 customer.setLogin(customerDTO.get(i+1));
	 customer.setPassword(customerDTO.get(i+2));
	  i=i+3;
	 customerArrayList.add(customer) ;
	}
	return customerArrayList;
    }

}