package com.movie.central.MovieCentral.resource;

import com.movie.central.MovieCentral.enums.AuthType;
import com.movie.central.MovieCentral.exceptions.Error;
import com.movie.central.MovieCentral.exceptions.MovieCentralException;
import com.movie.central.MovieCentral.model.Customer;
import com.movie.central.MovieCentral.model.CustomerRating;
import com.movie.central.MovieCentral.response.Response;
import com.movie.central.MovieCentral.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/customer")
@RestController
public class CustomerResource {

    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody Map<String, String> input, HttpSession session) throws Exception{

        try{
            String name = input.get("name");
            String email = input.get("email");
            String screenName = input.get("screenName");
            String password = input.get("password");
            AuthType authType = AuthType.getByName(input.get("authType"));
            Customer customer = Customer.builder().name(name).email(email).screenName(screenName)
                    .password(password).authType(authType).build();
            customerService.register(customer);
            Response response = new Response("Customer Registered Successfully", HttpStatus.CREATED);
            return new ResponseEntity(response, response.getStatus());
        }catch(DataIntegrityViolationException ex){
            throw new MovieCentralException(Error.DUPLICATE_USER);
        }

    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    public ResponseEntity<?> subscribe(@RequestBody Map<String,String> input, HttpSession session) throws Exception{
        Long customerId = Long.valueOf(input.get("customerId"));
        Integer noOfMonths = Integer.valueOf(input.get("months"));
        if(noOfMonths < 1)
            throw new MovieCentralException(Error.INVALID_SUBSCRIPTION_MONTHS);
        Double price = Double.valueOf(input.get("price"));
        customerService.subscribe(customerId, noOfMonths,price);
        Response response = new Response("Subscription was successful", HttpStatus.OK);
        return new ResponseEntity(response, response.getStatus());
    }

    @RequestMapping(value = "/subscription-status", method = RequestMethod.GET)
    public ResponseEntity<?> getBillingStatus(@RequestParam Long customerId, HttpSession session) throws Exception{
        LocalDateTime subEndTime = customerService.getBillingStatus(customerId);
        Map<String,String> response = new HashMap<>();
        response.put("result" , subEndTime==null ? "Not Subscribed" : subEndTime.toString());
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/searchAll", method = RequestMethod.GET)
    public ResponseEntity<?> findAllCustomers(HttpSession session) throws Exception{
        Map<String,List<Customer>> response = new HashMap<>();
        List<Customer> customers = customerService.findAllCustomers();
        response.put("result" , customers);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/reviews", method = RequestMethod.GET)
    public ResponseEntity<?> getAllReviews(@RequestParam Long customerId, HttpSession session) throws Exception{
        List<CustomerRating> ratings = customerService.getAllCustomerRatings(customerId);
        Map<String, List<CustomerRating>> response = new HashMap<>();
        response.put("result", ratings);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

}
