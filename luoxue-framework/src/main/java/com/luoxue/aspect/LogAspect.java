package com.luoxue.aspect;

import com.alibaba.fastjson.JSON;
import com.luoxue.annotation.SystemLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
@Slf4j
public class LogAspect {
    @Pointcut("@annotation(com.luoxue.annotation.SystemLog)")
    public void pt() {

    }

    @Around("pt()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {

        Object ret;
        try {
            handerBefor(joinPoint);
            ret = joinPoint.proceed();
            handerAfter(ret);
        } finally {
            // 结束后换行
            log.info("=======End=======" + System.lineSeparator());
        }
        return ret;
    }

    private void handerAfter(Object ret) {
        // 打印出参
        log.info("Response : {}",JSON.toJSONString(ret)  );
    }

    private void handerBefor(ProceedingJoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        SystemLog systemLog = getSystemLog(joinPoint);
        log.info("=======Start=======");
        // 打印请求 URL
        log.info("URL : {}", request.getRequestURL());
        // 打印描述信息
        log.info("BusinessName : {}", systemLog.businessName());
        // 打印 Http method
        log.info("HTTP Method : {}",request.getMethod() );
        // 打印调用 controller 的全路径以及执行方法
        log.info("Class Method : {}.{}",joinPoint.getSignature().getDeclaringTypeName(),joinPoint.getSignature().getName() );
        // 打印请求的 IP
        log.info("IP : {}",request.getRemoteHost() );
        // 打印请求入参
        log.info("Request Args : {}", JSON.toJSONString( joinPoint.getArgs()) );

    }

    private SystemLog getSystemLog(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        SystemLog systemLog = signature.getMethod().getAnnotation(SystemLog.class);
        return systemLog;
    }

}
