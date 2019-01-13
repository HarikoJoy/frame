package com.frame.hariko.sao;

import com.frame.hariko.sao.req.GetQrTicketReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "cgi", url = "${application.weixin.cgi.bin.url}")
public interface WxQrCodeSAO {
    String APP_ID = "appId";
    String APP_SECRET = "appSecret";
    String ACCESS_TOKEN = "accessToken";
    public static final String GET_ACCESS_TOKEN_URL = "token?grant_type=client_credential&appid={appId}&secret={appSecret}";

    @RequestMapping(value = GET_ACCESS_TOKEN_URL, method = RequestMethod.POST)
    public String getAccessToken(@PathVariable(APP_ID) String appId, @PathVariable(APP_SECRET) String appSecret);


    public static final String GET_QR_TICKET_URL = "qrcode/create?access_token={accessToken}";

    @RequestMapping(value = GET_QR_TICKET_URL, method = RequestMethod.POST)
    public String getQrTicket(@PathVariable(ACCESS_TOKEN) String accessToken, @RequestBody GetQrTicketReq getQrTicketReq);

}
