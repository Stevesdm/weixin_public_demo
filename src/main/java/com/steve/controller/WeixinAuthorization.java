package com.steve.controller;

import com.steve.common.Utils.MessageUtil;
import com.steve.common.Utils.WeixinSignUtil;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by SteveJobson on 2017/5/6.
 */
@RestController
@RequestMapping("/wechat")
public class WeixinAuthorization {

    private static Logger logger = LoggerFactory.getLogger(WeixinAuthorization.class);

    @RequestMapping(value = "security", method = RequestMethod.GET)
    public void doGet(
            HttpServletResponse response,
            @RequestParam(value = "signature", required = true) String signature,
            @RequestParam(value = "timestamp", required = true) String timestamp,
            @RequestParam(value = "nonce", required = true) String nonce,
            @RequestParam(value = "echostr", required = true) String echostr) {
        try {
            if (WeixinSignUtil.checkSignature(signature, timestamp, nonce)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                out.close();
            } else {
                logger.info("请求非法！");
            }
        } catch (Exception e) {
            logger.error("微信验证出现异常：{}", e);
        }
    }

    @RequestMapping(value = "security", method = RequestMethod.POST)
    // post 方法用于接收微信服务端消息
    public void DoPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        System.out.println("这是 post 方法！");
        try{
            Map<String, String> map= MessageUtil.parseXml(request);
            System.out.println("============================="+map.get("Content"));
        }catch(Exception e){
            logger.error("接受消息发生异常：{}",e);
        }
    }
}
