package com.iandna.project.config.aop;

import com.iandna.project.config.model.MapData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CustomAspect {
	private static final Logger logger = LoggerFactory.getLogger(CustomAspect.class);
	
	// execution() : joinpoint에 함수를 실행한다.
	// 제어자 : public
	// 반환타입 : ProductVO
	// 패키지 : com.victolee.aoptest
	// 클래스 : ProductServcie
	// 메서드 : find
	// 예외 던지기 생략 가능
//	@Before("execution(public ProductVO com.victolee.aoptest.ProductService.find(..) )") // joinpoint 지정
	public void beforeAdvice() {
		logger.debug("beforeAdvice() called");
	}
	
	// 접근제어자 생략 가능
	// 반환타입 : 모든 타입
	// 패키지 : com.victolee.aoptest
	// 클래스 : 패키지 내의 모든 클래스
	// 메서드 : find
//	@After("execution(* com.victolee.aoptest.*.find(..) )")
	public void afterAdvice() {
		logger.debug("afterAdvice() called");
	}
	
	// 모든 패키지 내 ProductServcie 클래스의 모든 메서드
//	@AfterReturning("execution(* *..ProductService.*(..))")
	public void afterReturningAdvice() {
		logger.debug("afterReturningAdvice() called");
	}
	
	// throw new Exception() 했을때 실행됨
//	@AfterThrowing(value="execution(* com.onetwocm.svc.view.*.*.*(..))", throwing="ex")
	public void afterThrowingAdvice(Exception ex) throws Exception {
		logger.error("error db insert..");
		MapData error_param = new MapData();
		
		String error_code = ex.getMessage();
		StringBuffer stack_trace = new StringBuffer();
		int index = 0;
		for(StackTraceElement s : ex.getStackTrace()) {
			stack_trace.append(s);
			stack_trace.append("\n");
			index++;
			if(index > 5) {
				break;
			}
		}
		stack_trace.append(ex.getStackTrace()[0]);
		logger.error("## stack_trace : " + stack_trace);
		error_param.set("app_id", "com.ivory30");
		error_param.set("app_ver", "");
		error_param.set("bld_code", "");
		error_param.set("dev_model", "");
		error_param.set("err_dev_type", "S");
		error_param.set("err_gbn1", "server");
		error_param.set("err_gbn2", "API");
		error_param.set("err_msg", error_code);
		error_param.set("err_path", stack_trace.toString());
		error_param.set("err_tgt", "");
		error_param.set("err_prior", "0");
		
		
		//this.errDao.insertErrorHis(error_param);
		ex.printStackTrace();
	}
	
	@AfterThrowing(value="execution(* com.ivory.album.config.interceptor.*.*.*(..))", throwing="ex")
	public void afterInterceptorException(Exception ex) throws Exception {
		
	}
	
	// pjp를 통해 "핵심 모듈의 메서드"의 실행이 가능
	// aoptest 패키지의 모든 클래스 모든 메서드 ( execution은 일반적으로 이렇게 쓰인다. )
//	@Around("execution(* *..aoptest.*.*(..))")
	public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
		// before advice
		logger.debug("aroundAdvice(): before");
		
		// proceed() 메서드 호출 => 실제 비즈니스
		// 비즈니스가 리턴하는 객체가 있을 수 있으므로 Obejct로 받아준다.
		Object result = pjp.proceed();
		
		// after advice
		logger.debug("aroundAdvice(): after");
		
		return result;
	}
}
