package com.erp.employee.repositories;

import com.erp.employee.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMessageRepository extends JpaRepository<Message, Long> {
}
