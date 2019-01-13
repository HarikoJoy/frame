package com.frame.hariko.web;

import com.alibaba.fastjson.JSONObject;
import com.frame.hariko.service.WxSupportService;
import com.frame.hariko.web.base.views.ResponseVo;
import com.frame.hariko.wx.WechatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private WxSupportService wxSupportService;

    @RequestMapping("/index")
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
       /* // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        System.out.println(">>>>>>>>>>>" + echostr);
        PrintWriter out = response.getWriter();
        out.print(echostr);  */

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try
        {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null)
            {
                sb.append(str);
            }

            System.out.println(">>>>>>>>>>>>>>>>>>" + sb.toString());
            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != br)
            {
                try
                {
                    br.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        //response.setContentType("application/xml; charset=UTF-8"); // 设置响应内容的类型和编码字符集
        //InputStream in = request.getInputStream();
        //PrintWriter out = response.getWriter();
        //handleMsgAndEvent(in, out);
    }

    @PostMapping("/getOrCodeUrl")
    @ResponseBody
    public ResponseVo<String> getOrCodeUrl(){
        JSONObject jsonObj = new JSONObject();
        return new ResponseVo<String>(wxSupportService.getQrCodeUrl("pushOrderNo", jsonObj));
    }

    public void handleMsgAndEvent(InputStream in, PrintWriter out) throws Exception
    {

        WechatSession session = new WechatSession(in, out);
        session.process(); // 处理微信消息或者事件
        session.close(); // 关闭Session
    }
}
