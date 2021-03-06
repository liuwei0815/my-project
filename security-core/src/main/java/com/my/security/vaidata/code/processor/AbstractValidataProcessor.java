package com.my.security.vaidata.code.processor;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.security.vaidata.code.ImageCodeException;
import com.my.security.vaidata.code.ValidataCode;
import com.my.security.vaidata.code.ValidataCodeGennerator;
import com.my.security.vaidata.code.enums.CodeValidataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractValidataProcessor<C extends ValidataCode> implements ValidataProcessor {

	@Autowired
	Map<String, ValidataCodeGennerator> gennerators;

	// private SessionStrategy strategy = new HttpSessionSessionStrategy();
	
	private ObjectMapper mapper = new ObjectMapper();
	

	@Autowired
	private SessionResposityStrategy sessionResposityStrategy;

	@Override
	public void create(ServletWebRequest request) throws ServletRequestBindingException, IOException {
		C code = gennerator(request);
		save(request, code);
		send(request, code);
	}

	protected abstract void send(ServletWebRequest request, C code) throws ServletRequestBindingException, IOException;

	private void save(ServletWebRequest request, C code) {
		ValidataCode vcode = new ValidataCode(code.getCode(), code.getLocalDateTime());
		// strategy.setAttribute(request, getSessionKey(request), vcode);
		try {
			log.info("set Session Key:{}",mapper.writeValueAsString(vcode));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sessionResposityStrategy.save(request, vcode, getFormNameKey(request));
	}

//	private String getSessionKey(ServletWebRequest request) {
//		return SESSION_KEY_FIX+getType(request).toUpperCase();
//	}

	@SuppressWarnings("unchecked")
	protected C gennerator(ServletWebRequest request) throws ServletRequestBindingException {
		ValidataCodeGennerator gennertor = getGennerTypeByType(getType(request));
		return (C) gennertor.createImageCode(request);
	}

	public String getType(ServletWebRequest request) {
		return StringUtils.substringBefore(getClass().getSimpleName(), "ValidataProcessor");
	}

	public ValidataCodeGennerator getGennerTypeByType(String type) {
		String beanName = type.trim().toLowerCase() + ValidataCodeGennerator.class.getSimpleName();
		ValidataCodeGennerator bean = gennerators.get(beanName);
		if (bean == null) {
			log.error("无法找到对应的bean:{}", beanName);
			throw new ImageCodeException("无法找到对应的bean");
		}
		return bean;
	}

	/**
	 * 校验短信和iamge code
	 * 
	 * @param request
	 * @throws ServletRequestBindingException
	 */
	@Override
	public void validataSmsCode(ServletWebRequest request) throws ServletRequestBindingException {

		// ValidataCode imageCode = (ValidataCode)
		// strategy.getAttribute(request,getSessionKey(request));
		ValidataCode imageCode = sessionResposityStrategy.get(request, getFormNameKey(request));
		try {
			log.info("get Session Key:{}",mapper.writeValueAsString(imageCode));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (imageCode == null)
			throw new ImageCodeException("验证码不存在");

		String code = ServletRequestUtils.getStringParameter(request.getRequest(), getFormNameKey(request));

		if (StringUtils.isBlank(code))
			throw new ImageCodeException("请输入验证码");

		// 是否已过期
		if (imageCode.isExpire()) {
			// strategy.removeAttribute(request, getSessionKey(request));
			sessionResposityStrategy.remove(request, getFormNameKey(request));
			throw new ImageCodeException("该验证码已过期");
		}

		if (!StringUtils.equals(imageCode.getCode(), code))
			throw new ImageCodeException("请输入正确的验证码");

		// strategy.removeAttribute(request,getSessionKey(request));
		sessionResposityStrategy.remove(request, getFormNameKey(request));
	}

	private String getFormNameKey(ServletWebRequest request) {
		return CodeValidataType.valueOf(getType(request).toUpperCase()).getRequestParamName();
	}

}
