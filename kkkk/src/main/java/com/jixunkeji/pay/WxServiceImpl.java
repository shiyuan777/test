//package com.jixunkeji.pay;
//
//import com.adong.worker.config.WxMpProperties;
//import com.adong.worker.config.WxPayProperties;
//import com.adong.worker.constant.DevEnum;
//import com.adong.worker.service.WxService;
//import com.adong.worker.utils.IpUtils;
//import com.adong.worker.utils.wx.AESUtil;
//import com.adong.worker.utils.wx.DateFormatUtils;
//import com.adong.worker.utils.wx.PayUtil;
//import com.adong.worker.utils.wx.WxPayUtil;
//import com.adong.worker.vo.wx.OrderPayVo;
//import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
//import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
//import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
//import com.github.binarywang.wxpay.exception.WxPayException;
//import com.github.binarywang.wxpay.service.WxPayService;
//import com.jixunkeji.utils.utils.IpUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.util.EntityUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.stereotype.Service;
//
//import javax.net.ssl.SSLContext;
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.security.KeyStore;
//import java.text.DecimalFormat;
//import java.util.*;
//
///**
// * @author liangzhuohao
// */
//@Slf4j
//@Service
//public class WxServiceImpl implements WxService {
//
//    @Value("${server.port}")
//    private int port;
//
////    @Resource
////    private ServiceOrderMapper serviceOrderMapper;
//
////    @Autowired
////    WxMpProperties wxMpProperties;
////
////    @Autowired
////    WxPayProperties wxPayProperties;
//
//    private WxPayService wxPayService;
//
//    //    @Autowired
////    public WxServiceImpl(WxService wxPayService) {
////        this.WxService = wxPayService;
////    }
//    @Autowired
//    private WxService wxService;
//
//    /**
//     * 连接超时时间，默认10秒
//     */
//    private int socketTimeout = 10000;
//
//    /**
//     * 传输超时时间，默认30秒
//     */
//    private int connectTimeout = 30000;
//
//    /**
//     * 请求器的配置
//     */
//    @Autowired
//    private static RequestConfig requestConfig;
//
//    /**
//     * HTTP请求器
//     */
//    @Autowired
//    private static CloseableHttpClient httpClient;
//
//    @Override
//    public <T> T createOrder(OrderPayVo orderPayVo, HttpServletRequest httpServletRequest) throws Exception {
//        log.info("orderPayVo:" + orderPayVo);
//
//        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
//
////        AdongOrderRelation relationByOrderNo = serviceOrderMapper.findRelationByOrderNo(orderPayVo.getOrderNo());
////        request.setBody(relationByOrderNo.getClassifyName());
//        //商户号
//        request.setMchId(wxPayProperties.getMchId());
//        //订单号
//        request.setOutTradeNo(orderPayVo.getOrderNo());
//        String attach = orderPayVo.getOrderNo();
//        Double price = 0.0;
////        if (relationByOrderNo != null) {
////            price = relationByOrderNo.getWxTotalPrices();
////            attach = relationByOrderNo.getClassifyName();
////        }
//
//        Double fenPrice = price * 100;
//        //四舍五入
//        Integer pprice = Integer.parseInt(new DecimalFormat("0").format(fenPrice));
//
//        request.setTotalFee(pprice);
//        request.setNotifyUrl(wxMpProperties.getHost() + "/api/v1/wx/callback");
//        request.setTradeType("JSAPI");
//        String ipAddr = IpUtil.getRealIp(httpServletRequest);
//        request.setSpbillCreateIp(ipAddr);
//        request.setOpenid(orderPayVo.getOpenId());
//        request.setAttach(attach);
//        return this.wxPayService.createOrder(request);
//    }
//
//    @Override
//    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
//        String line = null;
//        StringBuilder sb = new StringBuilder();
//        while ((line = br.readLine()) != null) {
//            sb.append(line);
//        }
//        br.close();
//        //sb为微信返回的xml
//        String notityXml = sb.toString();
//        String resXml = "";
//        System.out.println("接收到的报文：" + notityXml);
//
//        // 转换成map
//        Map<String, String> notifyMap = WxPayUtil.xmlToMap(notityXml);
//
//        String returnCode = notifyMap.get("return_code");
//        if ("SUCCESS".equals(returnCode)) {
//            // 签名正确
//            // 进行处理。
//            // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
//
//            //获取openId
//            String openId = notifyMap.get("openid");
//            //获取订单号
//            String orderNo = notifyMap.get("out_trade_no");
//            //获取自定义标识
//            String attach = notifyMap.get("attach");
//            //获取支付完成时间
//            String payTime = notifyMap.get("time_end");
//
//            WxPayOrderQueryResult wxPayOrderQueryResult = this.wxPayService.queryOrder(null, orderNo);
//            String payTradeNo = wxPayOrderQueryResult.getTransactionId();
//            log.info("订单:" + orderNo + ",信息为：" + wxPayOrderQueryResult);
//
////            Date payDate = DateFormatUtils.parse(DateFormatUtils.YYYYMMDDHHMMSS, payTime);
//
////            List<AdongOrder> adongOrders = serviceOrderMapper.findOrderByOrderNo(orderNo);
//
////            if (adongOrders.size() > 0) {
////                AdongOrderRelation adongOrderRelation = serviceOrderMapper.findRelationByOrderNo(orderNo);
////                adongOrderRelation.setStatus(0);
////            }
//
//            // 通知微信服务器已经支付成功
//            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
//                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
//        } else {
//            // 签名错误，如果数据里没有sign字段，也认为是签名错误
//            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
//                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
//        }
//
//        System.out.println(resXml);
//        System.out.println("微信支付回调数据结束");
//
//        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
//        out.write(resXml.getBytes());
//        out.flush();
//        out.close();
//    }
//
////    @Override
////    public HashMap refund(OrderPayVo orderPayVo) throws Exception {
////        String code = DevEnum.STATUS_SUCCESS.getCode().toString();
////        String msg = DevEnum.STATUS_SUCCESS.getMsg();
////        Map<String, String> data = new HashMap<>();
////
//////        try {
////            if (StringUtils.isEmpty(orderPayVo.getOrderNo())) {
////                code = DevEnum.STATUS_FAIL.getCode().toString();
////                msg = DevEnum.STATUS_FAIL.getMsg();
////            } else {
//////                AdongOrderRelation relationByOrderNo = serviceOrderMapper.findRelationByOrderNo(orderPayVo.getOrderNo());
////                //退款到用户微信
////                String nonceStr = getRandomStringByLength(32);
////                data.put("appid", wxPayProperties.getAppId());
////                data.put("mch_id", wxPayProperties.getMchId());
////                data.put("nonce_str", nonceStr);
////                data.put("sign_type", "MD5");
////                //商户订单号
////                data.put("out_trade_no", orderPayVo.getOrderNo());
////                //商户退款单号
////                data.put("out_refund_no", UUID.randomUUID().toString().replaceAll("-", ""));
////                //支付金额，微信支付提交的金额是不能带小数点的，且是以分为单位,这边需要转成字符串类型，否则后面的签名会失败
//////                data.put("total_fee", String.valueOf(Math.round(relationByOrderNo.getWxTotalPrices() * 100)));
////                //退款总金额,订单总金额,单位为分,只能为整数
//////                data.put("refund_fee", String.valueOf(Math.round(relationByOrderNo.getWxTotalPrices() * 100)));
////                //退款成功后的回调地址
////                data.put("notify_url", wxMpProperties.getHost() + "/api/v1/worker/wx/pay/refund/call/back");
////                // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
////                String preStr = PayUtil.createLinkString(data);
////                //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
////                String mySign = PayUtil.sign(preStr, wxPayProperties.getMchKey(), "utf-8").toUpperCase();
////                data.put("sign", mySign);
////
////                //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
////                //支付结果通知的xml格式数据
////                String xmlStr = postData(wxPayProperties.getRefundPath(), PayUtil.getMapToXML(data));
////                log.info(xmlStr);
////                Map notifyMap = PayUtil.doXMLParse(xmlStr);
////                if ("SUCCESS".equals(notifyMap.get("return_code"))) {
////                    if ("SUCCESS".equals(notifyMap.get("result_code"))) {
////                        //退款成功的操作
////                        // 返回的预付单信息
////                        String prepayId = (String) notifyMap.get("prepay_id");
////                        log.info("prepayId:" + prepayId);
////                        Long timeStamp = System.currentTimeMillis() / 1000;
////                        //拼接签名需要的参数
////                        String stringSignTemp = "appId=" + wxPayProperties.getAppId() + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepayId + "&signType=MD5&timeStamp=" + timeStamp;
////                        //签名算法生成签名
////                        String paySign = PayUtil.sign(stringSignTemp, wxPayProperties.getMchKey(), "utf-8").toUpperCase();
////                        data.put("package", "prepay_id=" + prepayId);
////                        data.put("timeStamp", String.valueOf(timeStamp));
////                        data.put("paySign", paySign);
////                    } else {
////                        log.info("退款失败:原因" + notifyMap.get("err_code_des"));
////                        code = DevEnum.STATUS_FAIL.getCode().toString();
////                        msg = (String) notifyMap.get("err_code_des");
////                    }
////                } else {
////                    log.info("退款失败:原因" + notifyMap.get("err_code_des"));
////                    code = DevEnum.STATUS_FAIL.getCode().toString();
////                    msg = (String) notifyMap.get("err_code_des");
////                }
////            }
//////        } catch (Exception e) {
//////            log.error(e.getMessage());
//////        }
////
////        HashMap<String, Object> jsonResult = new HashMap<>();
////        jsonResult.put("code", code);
////        jsonResult.put("msg", msg);
////        jsonResult.put("data", data);
////
////        return jsonResult;
////
////    }
//
////    @Override
////    public void wxRefundNotify(HttpServletRequest request, HttpServletResponse response) throws Exception  {
////        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
////        String line = null;
////        StringBuilder sb = new StringBuilder();
////        while ((line = br.readLine()) != null) {
////            sb.append(line);
////        }
////        br.close();
////        //sb为微信返回的xml
////        String notityXml = sb.toString();
////        String resXml = "";
////        log.info("微信退款接收到的报文：" + notityXml);
////
////        // 转换成map
////        Map<String, String> notifyMap = WxPayUtil.xmlToMap(notityXml);
////
////        String returnCode = notifyMap.get("return_code");
////        if ("SUCCESS".equals(returnCode)) {
////            try {
////                // 签名正确
////                // 进行处理。
////                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
////                String reqInfo = notifyMap.get("req_info");
////                String reqInfoXml = AESUtil.decryptData(reqInfo);
////                Map<String, String> reqInfoMap = WxPayUtil.xmlToMap(reqInfoXml);
////
//////                //获取订单号
////                String orderNo = reqInfoMap.get("out_trade_no");
//////                //获取退款成功时间
////                String successTime = reqInfoMap.get("success_time");
////                // 微信订单号
////                String transactionId = reqInfoMap.get("transaction_id");
////                // 商户退款单号
////                String outRefundNo = reqInfoMap.get("out_refund_no");
////                // 微信退款单号
////                String refundId = reqInfoMap.get("refund_id");
////                String refundFee = reqInfoMap.get("refund_fee");
////                double v = Double.parseDouble(refundFee);
////                log.info("订单号["+orderNo+"]微信退款返回信息："+reqInfoMap);
////                WxPayRefundQueryResult wxPayRefundQueryResult = this.wxPayService.refundQuery(null, orderNo, null, null);
////                log.info("订单[" + orderNo + "]的信息为：" + wxPayRefundQueryResult);
////            } catch (WxPayException e) {
////                log.error(e.getCustomErrorMsg());
////            }
////
////            // 通知微信服务器已经支付成功
////            resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
////                    + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
////        } else {
////            // 签名错误，如果数据里没有sign字段，也认为是签名错误
////            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
////                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
////        }
////
////        log.info(resXml);
////        log.info("微信退款回调数据结束");
////
////        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
////        out.write(resXml.getBytes());
////        out.flush();
////        out.close();
////    }
//
//    /**
//     * 加载证书
//     */
//    private static void initCert() throws Exception {
//        // 证书密码，默认为商户ID
//        String key = "1528649351";
//
//        // 指定读取证书格式为PKCS12
//        KeyStore keyStore = KeyStore.getInstance("PKCS12");
//
//        ClassPathResource resource = new ClassPathResource("cert/apiclient_cert.p12");
//        InputStream instream = resource.getInputStream();
//        // 读取本机存放的PKCS12证书文件
////        File file = ResourceUtils.getFile("classpath:cert/apiclient_cert.p12");
////        FileInputStream instream = new FileInputStream(file);
//        try {
//            // 指定PKCS12的密码(商户ID)
//            keyStore.load(instream, key.toCharArray());
//        } finally {
//            instream.close();
//        }
//
//        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, key.toCharArray()).build();
//
//        // 指定TLS版本
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null, SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
//        // 设置httpclient的SSLSocketFactory
//        httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//    }
//
//
//    /**
//     * 通过Https往API post xml数据
//     *
//     * @param url    API地址
//     * @param xmlObj 要提交的XML数据对象
//     * @return
//     */
//    public String postData(String url, String xmlObj) throws Exception {
//        // 加载证书
//        try {
//            initCert();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        String result = null;
//        HttpPost httpPost = new HttpPost(url);
//        // 得指明使用UTF-8编码，否则到API服务器XML的中文不能被成功识别
//        StringEntity postEntity = new StringEntity(xmlObj, "UTF-8");
//        httpPost.addHeader("Content-Type", "text/xml");
//        httpPost.setEntity(postEntity);
//        // 根据默认超时限制初始化requestConfig
//        requestConfig = RequestConfig.custom()
//                .setSocketTimeout(socketTimeout)
//                .setConnectTimeout(connectTimeout)
//                .build();
//        // 设置请求器的配置
//        httpPost.setConfig(requestConfig);
//        try {
//            HttpResponse response = null;
//            try {
//                response = httpClient.execute(httpPost);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            HttpEntity entity = response.getEntity();
//            try {
//                result = EntityUtils.toString(entity, "UTF-8");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            httpPost.abort();
//        }
//        return result;
//    }
//
//    private String getRandomStringByLength(int length) {
//        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
//        Random random = new Random();
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < length; i++) {
//            int number = random.nextInt(base.length());
//            sb.append(base.charAt(number));
//        }
//        return sb.toString();
//    }
//}
