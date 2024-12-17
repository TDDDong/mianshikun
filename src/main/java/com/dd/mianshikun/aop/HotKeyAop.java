package com.dd.mianshikun.aop;

import com.dd.mianshikun.common.BaseResponse;
import com.dd.mianshikun.common.ErrorCode;
import com.dd.mianshikun.common.ResultUtils;
import com.dd.mianshikun.exception.ThrowUtils;
import com.dd.mianshikun.model.dto.questionBank.QuestionBankQueryRequest;
import com.dd.mianshikun.model.vo.QuestionBankVO;
import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

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
        QuestionBankQueryRequest request = (QuestionBankQueryRequest) args[0];

        Long questionBankId = request.getId();
        ThrowUtils.throwIf(questionBankId <= 0, ErrorCode.PARAMS_ERROR);
        //利用京东hotkey判断是否为热点数据 是的话就从本地缓存中取值
        String key = "bank_detail_" + questionBankId;
        BaseResponse<QuestionBankVO> response = null;
        if (JdHotKeyStore.isHotKey(key)) {
            QuestionBankVO questionBankVO = (QuestionBankVO) JdHotKeyStore.get(key);
            if (questionBankVO == null) {
                /**
                 * 这里需要判断缓存中的值是否为空
                 * 因为当该数据第一次成为热key时，需要将值设置进本地缓存中，以便后续aop中判断热key可以直接从缓存中取值
                 * 相当于5秒10次规则 第11次是来设置值的 第十二次开始才从缓存取值
                 */
                response = (BaseResponse<QuestionBankVO>) joinPoint.proceed();
            } else {
                response = ResultUtils.success(questionBankVO);
            }
        } else {
            response = (BaseResponse<QuestionBankVO>) joinPoint.proceed();
        }
        QuestionBankVO questionBankVO = response.getData();
        JdHotKeyStore.smartSet(key, questionBankVO);
        return response;
    }
}
