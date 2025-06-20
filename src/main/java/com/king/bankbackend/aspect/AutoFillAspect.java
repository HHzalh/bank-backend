package com.king.bankbackend.aspect;

import com.king.bankbackend.annotation.AutoFill;
import com.king.bankbackend.constant.AutoFillConstant;
import com.king.bankbackend.constant.OperationType;
import com.king.bankbackend.context.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自用填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.king.bankbackend.mapper.*.*(..))&&@annotation(com.king.bankbackend.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    /**
     * 前置通知，在通知中进行公共字段的赋值
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            //防止空指针
            return;
        }
        //约定实体对象作为方法的第一个参数
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //反射三要素：调用什么对象，形参是什么，放回什么
        if (operationType == OperationType.INSERT) {
            //为4个公共字段赋值
            try {
                //规范、防止字符串拼写出错，将公共字段设置为常量
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {
            //为2个公共字段赋值
            try {
                //规范、防止字符串拼写出错，将公共字段设置为常量
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity, now);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //根据当前不同的操作类型，为对应的属性通过反射赋值

    }
}
