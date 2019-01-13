package com.frame.hariko.wx;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.PrintWriter;
import java.util.List;

@Setter
@Getter
public class NewsMessage extends AbstractMsg{
    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private int ArticleCount;//图文消息个数，限制为8条以内
    private List<News> Articles;//多条图文消息信息，默认第一个item为大图,注意，如果图文数超过8，则将会无响应

    private String msgId;
    @Override
    protected void readBody(Document document) {
        msgId = document.getElementsByTagName("MsgId").item(0).getTextContent();
    }

    @Override
    protected void writeBody(Element root, Document document) {
       /* Element contentElement = document.createElement("Content");
        CDATASection contentCData = document.createCDATASection(this.content);
        contentElement.appendChild(contentCData);
        root.appendChild(contentElement); */
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
