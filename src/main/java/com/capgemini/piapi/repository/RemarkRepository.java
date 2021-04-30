package com.capgemini.piapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.Remark;
/**
 * This RemarkRepository will be responsible for performing Reamark related CRUD operations 
 * on Remark
 * @author Harsh Joshi
 *
 */
@Repository
public interface RemarkRepository extends JpaRepository<Remark, Long>{

}
