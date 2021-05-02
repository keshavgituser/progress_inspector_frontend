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
public interface TeamLeaderRepository extends JpaRepository<TeamLeader, Long> {

	/**
	 * This method will find team leader on basis of loginName and pwd
	 * 
	 * @param loginName
	 * @param pwd
	 * @return TeamLeader if teamleader exist in DB
	 */
	public TeamLeader findTeamLeaderByLoginNameAndPwd(String loginName, String pwd);

	/**
	 * This method will find a particular team leader by loginName
	 * 
	 * @param loginName
	 * @return teamleader if loginName exist
	 */
	public TeamLeader findByLoginName(String loginName);

	/**
	 * This method is used to delete team leader on the basis of Login Name
	 * 
	 * @param loginName
	 */
	public void deleteByLoginName(String loginName);

}
