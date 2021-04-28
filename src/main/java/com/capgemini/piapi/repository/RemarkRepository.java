package com.capgemini.piapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Remark;

@Repository
public interface RemarkRepository extends JpaRepository<Remark, Long>{

}
