package com.frame.hariko.wx;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import lombok.Data;

/**
 * 消息与事件的抽象父类
 */
@Data
public abstract class AbstractMsg{
    protected String toUserName;
    protected String fromUserName;
    protected String createTime;
    protected String msgType;

    /**
     * 读取Document内容到Java对象
     *
     * @param document
     */
    public void read(Document document)
    {
        readHead(document);
        readBody(document);
    }

    /**
     * 获取Java对象的XML字符串
     */
    public String write()
    {
        Document document = WechatSession.getDocumentBuilder().newDocument();
        Element root = document.createElement("xml");
        writeHead(root, document);
        writeBody(root, document);
        document.appendChild(root);
        return readDocument(document);
    }

    /**
     * 读取消息和事件公有的一些基础属性
     *
     * @param document
     */
    private void readHead(Document document)
    {
        toUserName = document.getElementsByTagName("ToUserName").item(0).getTextContent();
        fromUserName = document.getElementsByTagName("FromUserName").item(0).getTextContent();
        createTime = document.getElementsByTagName("CreateTime").item(0).getTextContent();
        msgType = document.getElementsByTagName("MsgType").item(0).getTextContent();

    }

    /**
     * 消息和事件具体子类需实现该方法，用来读取一些自身独有的属性数据
     *
     * @param document
     */
    protected abstract void readBody(Document document);

    /**
     * 将Java对象中的基础属性写入Document
     *
     * @param root
     * @param document
     */
    private void writeHead(Element root, Document document)
    {
        Element toUserNameElement = document.createElement("ToUserName");
        CDATASection toUserNameCData = document.createCDATASection(this.toUserName);
        toUserNameElement.appendChild(toUserNameCData);
        Element fromUserNameElement = document.createElement("FromUserName");
        CDATASection fromUserNameCData = document.createCDATASection(this.fromUserName);
        fromUserNameElement.appendChild(fromUserNameCData);
        Element createTimeElement = document.createElement("CreateTime");
        createTimeElement.setTextContent(this.createTime);
        Element msgTypeElement = document.createElement("MsgType");
        CDATASection msgTypeCData = document.createCDATASection(this.msgType);
        msgTypeElement.appendChild(msgTypeCData);

        root.appendChild(toUserNameElement);
        root.appendChild(fromUserNameElement);
        root.appendChild(createTimeElement);
        root.appendChild(msgTypeElement);
    }

    /**
     * 消息和事件各子类需实现该方法，用来写入一些自身独有的属性数据
     *
     * @param root
     * @param document
     */
    protected abstract void writeBody(Element root, Document document);

    /**
     * 获取Document对象中指定元素的内容
     *
     * @param document
     * @param elementName
     * @return
     */
    protected String getElementContent(Document document, String elementName)
    {
        if (document.getElementsByTagName(elementName).getLength() > 0)
        {
            return document.getElementsByTagName(elementName).item(0).getTextContent();
        }
        return null;
    }

    /**
     * 读取Document对象为XML字符串
     *
     * @param document
     */
    private String readDocument(Document document)
    {
        String docXml = "";
        StringWriter writer = new StringWriter();
        try
        {
            Transformer transformer = WechatSession.tffactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");// 设置编码字符集
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");// 设置缩进

            transformer.transform(new DOMSource(document), new StreamResult(writer));
            docXml = writer.getBuffer().toString();
            System.out.println(docXml);// 将获取到的XML字符串打印至控制台
            writer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != writer)
            {
                try
                {
                    writer.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return docXml;

    }

}
