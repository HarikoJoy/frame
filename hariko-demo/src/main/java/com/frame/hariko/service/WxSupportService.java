package com.frame.hariko.service;


import com.frame.hariko.cnst.DpbsCnst;
import com.frame.hariko.sao.WxQrCodeSAO;
import com.frame.hariko.sao.req.ActionInfo;
import com.frame.hariko.sao.req.GetQrTicketReq;
import com.frame.hariko.sao.req.Scene;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WxSupportService {
    private static final String OPEN_ID = "openid";
    private static final String ERR_CODE = "errcode";
    private static final String ERR_MSG = "errmsg";
    private static final String SUCCESS_ERR_CODE = "0";

    @Value("${we.chat.qr.app.id}")
    private String qrAppId;
    @Value("${we.chat.qr.app.secret}")
    private String qrAppSecret;
    @Autowired
    private WxQrCodeSAO wxQrCodeSAO;

    public String getQrCodeUrl(String pushOrderNo, JSONObject jsonObj) {
        String rtnStr = wxQrCodeSAO.getAccessToken(qrAppId, qrAppSecret);
        log.info("推送订单号[{}]生成二维码期间获取访问token结果为:{}", pushOrderNo, rtnStr);
        if (StringUtils.isNotEmpty(rtnStr)) {
            JSONObject tokenJsonObj = JSONObject.parseObject(rtnStr);
            String accessToken = tokenJsonObj.getString("access_token");
            GetQrTicketReq getQrTicketReq = new GetQrTicketReq();
            getQrTicketReq.setExpire_seconds(1800l);
            getQrTicketReq.setAction_name("QR_STR_SCENE");
            ActionInfo actionInfo = new ActionInfo();
            Scene scene = new Scene();
            //scene.setScene_id(10000l);
            scene.setScene_str("201801122350");
            actionInfo.setScene(scene);
            getQrTicketReq.setAction_info(actionInfo);
            System.out.println(">>>>>>>>>>" + JSONObject.toJSONString(getQrTicketReq));
            rtnStr = wxQrCodeSAO.getQrTicket(accessToken, getQrTicketReq);
            log.info("推送订单号[{}]生成二维码期间获取访问ticket结果为:{}", pushOrderNo, rtnStr);
            if (StringUtils.isNotEmpty(rtnStr)) {
                JSONObject ticketJsonObj = JSONObject.parseObject(rtnStr);
                return ticketJsonObj.getString("ticket");
            } else {
                jsonObj.put(DpbsCnst.RTN_CODE, DpbsCnst.FAIL_RTN_CODE);
                jsonObj.put(DpbsCnst.RTN_MSG, "微信获取ticket内容有误:" + rtnStr);
                return null;
            }
        } else {
            jsonObj.put(DpbsCnst.RTN_CODE, DpbsCnst.FAIL_RTN_CODE);
            jsonObj.put(DpbsCnst.RTN_MSG, "微信获取token内容有误:" + rtnStr);
            return null;
        }
    }


}
