package com.luv2code.aopdemo.aspect;

import java.util.List;
import java.util.logging.Logger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.aopdemo.Account;

@Aspect
@Component
@Order(2)
public class MyDemoLogginAspect {

	private Logger myLogger = Logger.getLogger(getClass().getName());
	
	@Around("execution(* com.luv2code.aopdemo.service.*.getFortune(..))")
	public Object aroundGetFortune(
			ProceedingJoinPoint theProceedingJoinPoint) throws Throwable {
		
		// print out method we are advisiting on
		String method = theProceedingJoinPoint.getSignature().toShortString();
		myLogger.info("\n======>>>> executing @Around on method :" + method);
		
		// get begin timestamp
		long begin = System.currentTimeMillis();
		
		// now, let's execute themethod
		Object result = null;
		
		// Option 1 : traitement de l'exception 
//		try {
//			result = theProceedingJoinPoint.proceed();
//		} catch (Exception e) {
//			// log the exception 
//			myLogger.warning(e.getMessage());
//			
//			// give user a custom message
//			result = "Major accident ! But no worries, "
//					+ "your private AOP helicopter is on the way !";
//			
//		}
		
		// Option 2 : transmettre l'exception 
		try {
			result = theProceedingJoinPoint.proceed();
		} catch (Exception e) {
			// log the exception 
			myLogger.warning(e.getMessage());
			
			// rethrow an exception
			throw e;
			
			
		}

		
		// get end timestamp
		long end = System.currentTimeMillis();
		
		// compute duration and display it
		long duration = end - begin;
		myLogger.info("\n=======> Duration : " + duration / 1000.0 + " seconds");
		
		return result;
		
	}
	
	
	// add a new advice for @AfterReturning on the findaccounts method
	//Ne pas oublier les parenthèses traitresses et l'execution
	@AfterReturning(
			pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccount(..))",
			returning="result")
	public void afterReturningFindAccountsAdvice(
			JoinPoint theJoinPoint, List<Account> result) {
				
		// print out wich method we are advising on
		String method = theJoinPoint.getSignature().toShortString();
		myLogger.info("\n======>>>> executing @AfterReturning on method :" + method);
		
		// print out the results of the method call 
		myLogger.info("\n======>>>> executing @AfterReturning on method :" + result);
		
		// let's post process the data ...let's modify it
		
		
		// convert the account names to uppercase
		convertAccountNamesToUpperCase(result);
		
		myLogger.info("\n======>>>> executing @AfterReturning on method :" + result);
		
	}

	private void convertAccountNamesToUpperCase(List<Account> result) {

		// loop through accounts
		
		for(Account tempAccount : result) {
		
			// get uppercase version of name
			String theUpperName = tempAccount.getName().toUpperCase();
			
			// update the name on the account
			tempAccount.setName(theUpperName);
			
				}
			}
	
	
}
