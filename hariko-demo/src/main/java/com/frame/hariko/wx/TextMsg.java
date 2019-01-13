package com.frame.hariko.wx;

import java.io.PrintWriter;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 普通文本消息
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TextMsg extends AbstractMsg
{
    private String content;
    private String msgId;

    /**
     * 从Document中读取普通文本消息独有的属性数据
     */
    @Override
    protected void readBody(Document document)
    {
        content = document.getElementsByTagName("Content").item(0).getTextContent();
        msgId = document.getElementsByTagName("MsgId").item(0).getTextContent();
    }

    /**
     * 回复时专用，将普通文本消息独有的回复内容写入Document中
     */
    @Override
    protected void writeBody(Element root, Document document)
    {
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
