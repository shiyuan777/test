//package com.jixunkeji.pay;
//
//import lombok.extern.log4j.Log4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//
//
///**
// * @author lianghzuohao
// */
//
//@Log4j
//@RestController
//@RequestMapping("/api/v1/worker/wx/pay")
//public class WxPayController {
//
//    @Value("${server.port}")
//    private int port;
//
//    @Autowired
//    private WxService wxService;
//
//    /**
//     * 统一下单,组装生成支付所需参数对象
//     */
//    @PostMapping("")
//    public <T> T createOrder(@RequestBody OrderPayVo orderPayVo,
//                             HttpServletRequest httpServletRequest) throws Exception {
//        return wxService.createOrder(orderPayVo,httpServletRequest);
//    }
//
//    /**
//     * 支付回调
//     * @param request
//     * @param response
//     * @throws Exception
//     */
//    @PostMapping("/service/order/callback")
//    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        wxService.wxNotify(request,response);
//    }
//
//    /**
//     * 微信退款
//     */
//    @PostMapping("/refund")
//    public HashMap refund(@RequestBody OrderPayVo orderPayVo) throws Exception {
//        return wxService.refund(orderPayVo);
//    }
//
//    /**
//     * 退款回调
//     * @param request
//     * @param response
//     * @throws Exception
//     */
//    @PostMapping("/refund/call/back")
//    public void updateRefundStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        wxService.wxRefundNotify(request,response);
//    }
//
//
//}
//
