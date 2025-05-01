package solitour_backend.solitour.config.routing;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class DataSourceAspect {

    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void beforeTransactional(JoinPoint joinPoint) {
        Transactional transactional = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(Transactional.class);
        if (transactional.readOnly()) {
            DataSourceContextHolder.setReplica();
        } else {
            DataSourceContextHolder.setMaster();
        }
    }

    @After("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void afterTransactional() {
        DataSourceContextHolder.clear();
    }
}
