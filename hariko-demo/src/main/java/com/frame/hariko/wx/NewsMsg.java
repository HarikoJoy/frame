package com.frame.hariko.wx;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.PrintWriter;

@Data
@EqualsAndHashCode(callSuper = false)
public class NewsMsg extends AbstractMsg {
    private String Title;//图文标题
    private String Description;//图文描述
    private String PicUrl;//图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
    private String Url;//点击图文消息跳转链接

    private String content;
    private String msgId;

    @Override
    protected void readBody(Document document) {
        content = document.getElementsByTagName("Content").item(0).getTextContent();
        msgId = document.getElementsByTagName("MsgId").item(0).getTextContent();
    }

    @Override
    protected void writeBody(Element root, Document document) {
        Element contentElement = document.createElement("Content");
        CDATASection contentCData = document.createCDATASection(this.content);
        contentElement.appendChild(contentCData);
        root.appendChild(contentElement);
    }

    public void onTextMsg(PrintWriter out)
    {
        System.out.println("此处添加处理普通文本消息的业务逻辑，此处简单回复：你好 某某某");
        TextMsg msg = new TextMsg();
        msg.setFromUserName(this.toUserName);
        msg.setToUserName(this.fromUserName);
        msg.setCreateTime(this.createTime);
        msg.setMsgType("text");
        msg.setContent("你好 " + this.fromUserName);
        out.print(msg.write());
    }
}
