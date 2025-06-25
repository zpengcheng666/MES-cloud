package com.miyu.module.wms.controller.admin.carrytask.vo;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;

@Data
public class CarryTaskResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer SUCCESS_CODE = 1;

    /**
     * 错误码
     *
     * @see ErrorCode#getCode()
     */
    private Integer Code;
    /**
     * 返回数据
     */
    private T Data;
    /**
     * 错误提示，用户可阅读
     *
     * @see ErrorCode#getMsg() ()
     */
    private String CodeMsg;

    /**
     * 将传入的 result 对象，转换成另外一个泛型结果的对象
     *
     * 因为 A 方法返回的 CarryTaskResult 对象，不满足调用其的 B 方法的返回，所以需要进行转换。
     *
     * @param result 传入的 result 对象
     * @param <T>    返回的泛型
     * @return 新的 CarryTaskResult 对象
     */
    public static <T> CarryTaskResult<T> error(CarryTaskResult<?> result) {
        return error(result.getCode(), result.getCodeMsg());
    }

    public static <T> CarryTaskResult<T> error(Integer code, String message) {
        Assert.isTrue(!GlobalErrorCodeConstants.SUCCESS.getCode().equals(code), "code 必须是错误的！");
        CarryTaskResult<T> result = new CarryTaskResult<>();
        result.Code = code;
        result.CodeMsg = message;
        return result;
    }

    public static <T> CarryTaskResult<T> error(ErrorCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMsg());
    }

    public static <T> CarryTaskResult<T> success(T data) {
        CarryTaskResult<T> result = new CarryTaskResult<>();
        result.Code = SUCCESS_CODE;
        result.Data = data;
        result.CodeMsg = "";
        return result;
    }

    public static boolean isSuccess(Integer code) {
        return Objects.equals(code, SUCCESS_CODE);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isSuccess() {
        return isSuccess(Code);
    }

    @JsonIgnore // 避免 jackson 序列化
    public boolean isError() {
        return !isSuccess();
    }

    // ========= 和 Exception 异常体系集成 =========

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     */
    public void checkError() throws ServiceException {
        if (isSuccess()) {
            return;
        }
        // 业务异常
        throw new ServiceException(Code, CodeMsg);
    }

    /**
     * 判断是否有异常。如果有，则抛出 {@link ServiceException} 异常
     * 如果没有，则返回 {@link #Data} 数据
     */
    @JsonIgnore // 避免 jackson 序列化
    public T getCheckedData() {
        checkError();
        return Data;
    }

    public static <T> CarryTaskResult<T> error(ServiceException serviceException) {
        return error(serviceException.getCode(), serviceException.getMessage());
    }

}
