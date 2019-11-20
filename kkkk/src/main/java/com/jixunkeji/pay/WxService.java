package com.jixunkeji.pay;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author liangzhuohao
 */
public interface WxService {

    <T> T createOrder(OrderPayVo orderPayVo, HttpServletRequest httpServletRequest)  throws Exception;

    void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception;

    HashMap refund(OrderPayVo orderPayVo)  throws Exception;

    void wxRefundNotify(HttpServletRequest request, HttpServletResponse response) throws Exception ;
}
