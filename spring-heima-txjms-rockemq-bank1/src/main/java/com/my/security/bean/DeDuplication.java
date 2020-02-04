package com.my.security.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * 本地事务幂等判断
 * @author liuwei
 *
 */
@Data
public class DeDuplication implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6769872732076513516L;

	private String txNo; // 事务id
	
	private Date createTime; // 
	
}
