package com.cafe24.mysite.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class MeasureExcecutionTimeAspect {
	
	//controller에 있는 모든 클래스의 모든 메소드
	@Around("execution(* *..repository.*.*(..))")
	public Object aroundAspect(ProceedingJoinPoint pjp) throws Throwable {
	
		StopWatch sw = new StopWatch();
		sw.start();
		
		Object result = pjp.proceed();
		
		sw.stop();
		Long totalTime = sw.getTotalTimeMillis();
		
		String className = pjp.getTarget().getClass().getName();
		String methodName = pjp.getSignature().getName();
		String taskName = className + "." + methodName;
		
		System.out.println("[Execution Time][" + taskName + "] " + totalTime);
		
		return result;
	}
}
