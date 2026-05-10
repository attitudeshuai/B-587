package com.fruit.warehouse.service;

import com.fruit.warehouse.entity.Customer;
import com.fruit.warehouse.repository.CustomerRepository;
import com.fruit.warehouse.repository.StockOutRecordRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    
    private final CustomerRepository customerRepository;
    private final StockOutRecordRepository stockOutRecordRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findByLevel(String level) {
        return customerRepository.findByLevel(level);
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Customer save(Customer customer) {
        logger.info("创建/更新客户: {}", customer.getName());
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        logger.info("删除客户: {}", id);
        // 检查是否存在关联的出库记录
        if (stockOutRecordRepository.existsByCustomerId(id)) {
            throw new IllegalArgumentException("删除失败：该客户存在出库记录，无法删除。如需删除，请先删除相关出库记录");
        }
        customerRepository.deleteById(id);
    }

    public long count() {
        return customerRepository.count();
    }
}
