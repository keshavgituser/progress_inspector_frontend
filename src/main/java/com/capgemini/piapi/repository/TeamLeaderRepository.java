package com.capgemini.piapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.capgemini.piapi.domain.TeamLeader;


/**
 * This TeamLeaderRepository Interface will be Responsible for performing 
 * various CRUD Operations on TeamLeader
 * 
 * @author Mantu
 *
 */
@Repository
public interface TeamLeaderRepository  extends JpaRepository<TeamLeader,Long>{

	public TeamLeader findTeamLeaderByLoginNameAndPwd(String loginName,String pwd);
	public TeamLeader findTeamLeaderById(Long id);
}
