package com.dd.mianshikun.aop;

import com.dd.mianshikun.annotation.HotKeyCheck;
import com.dd.mianshikun.common.BaseResponse;
import com.dd.mianshikun.common.ErrorCode;
import com.dd.mianshikun.common.ResultUtils;
import com.dd.mianshikun.constant.ClassConstant;
import com.dd.mianshikun.exception.ThrowUtils;
import com.dd.mianshikun.model.dto.questionBank.QuestionBankQueryRequest;
import com.dd.mianshikun.model.enums.ClassPrefixEnum;
import com.dd.mianshikun.model.vo.QuestionBankVO;
import com.dd.mianshikun.model.vo.QuestionVO;
import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import net.bytebuddy.implementation.bytecode.Throw;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
public class HotKeyAop {

    /**
     * 对标注了HotKeyCheck注解的方法进行热key检测
     * 如果获取到的数据是热key 则存入本地缓存
     * @param joinPoint
     */
    @Around("@annotation(com.dd.mianshikun.annotation.HotKeyCheck)")
    public BaseResponse<?> hotKeyCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取标注注解方法的参数
        Object[] args = joinPoint.getArgs();
        //题目和题库方法都需要进行热key探测
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HotKeyCheck annotation = method.getAnnotation(HotKeyCheck.class);
        String classPrefix = annotation.value();

        String prefix = ClassPrefixEnum.getPrefixByKey(classPrefix);
        ThrowUtils.throwIf(prefix == null, ErrorCode.PARAMS_ERROR);
        Long id = null;
        if (ClassConstant.QUESTION.equals(classPrefix)) {
            id = (Long) args[0];
        } else if (ClassConstant.QUESTIONBANK.equals(classPrefix)) {
            QuestionBankQueryRequest request = (QuestionBankQueryRequest) args[0];
            id = request.getId();
        }
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        //利用京东hotkey判断是否为热点数据 是的话就从本地缓存中取值
        String key = prefix + id;
        BaseResponse<?> response = null;
        if (JdHotKeyStore.isHotKey(key)) {
            Object object = JdHotKeyStore.get(key);
            if (Objects.isNull(object)) {
                /**
                 * 这里需要判断缓存中的值是否为空
                 * 因为当该数据第一次成为热key时，需要将值设置进本地缓存中，以便后续aop中判断热key可以直接从缓存中取值
                 * 相当于5秒10次规则 第11次是来设置值的 第十二次开始才从缓存取值
                 */
                Object result = joinPoint.proceed();
                response = ClassConstant.QUESTION.equals(classPrefix)
                        ? (BaseResponse<QuestionVO>) result
                        : (BaseResponse<QuestionBankVO>) result;
            } else {
                response = ClassConstant.QUESTION.equals(classPrefix)
                        ? ResultUtils.success((QuestionVO) object)
                        : ResultUtils.success((QuestionBankVO) object);
            }
        } else {
            //不是热key 执行原有业务逻辑
            Object result = joinPoint.proceed();
            response = ClassConstant.QUESTION.equals(classPrefix)
                    ? (BaseResponse<QuestionVO>) result
                    : (BaseResponse<QuestionBankVO>) result;
        }
        //只有key为热key的时候才会设置到本地缓存中去
        JdHotKeyStore.smartSet(key, response.getData());
        return response;
    }
}
