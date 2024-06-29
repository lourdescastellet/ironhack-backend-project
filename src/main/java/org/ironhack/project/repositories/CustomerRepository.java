package org.ironhack.project.repositories;

import org.ironhack.project.models.classes.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByEmail(String email);

    List<Customer> findByPaymentMethod(String paymentMethod);

    List<Customer> findByCustomerAddress(String customerAddress);

}
