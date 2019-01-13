package com.frame.hariko.wx;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;


import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 处理消息和事件的入口类
 */
public class WechatSession

{

    private InputStream in;

    private PrintWriter out;

    public static TransformerFactory tffactory = TransformerFactory.newInstance();

    private static DocumentBuilder documentBuilder = null;

    public static DocumentBuilder getDocumentBuilder()
    {
        // 先检查实例是否已创建，如果未创建才进入同步块
        if (null == documentBuilder)
        {
            synchronized (WechatSession.class)
            {
                // 再次检查实例是否已创建，如果真的未创建才创建实例
                if (null == documentBuilder)
                {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    try
                    {
                        documentBuilder = factory.newDocumentBuilder();
                    }
                    catch (ParserConfigurationException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        return documentBuilder;
    }

    /**
     * 构造方法
     *
     * @param in
     * @param out
     */
    public WechatSession(InputStream in, PrintWriter out)
    {
        this.in = in;
        this.out = out;
    }

    /**
     * 对接收到的消息和事件进行处理
     */
    public void process()

    {
        try
        {
            Document document = getDocumentBuilder().parse(in);
            String msgType = document.getElementsByTagName("MsgType").item(0).getTextContent();

            if ("text".equals(msgType))
            {
                System.out.println("text");
                TextMsg msg = new TextMsg();
                msg.read(document);
                msg.onTextMsg(out);
            }
            else if ("event".equals(msgType))
            {
                System.out.println("event");
                EventMsg msg = new EventMsg();
                msg.read(document);
                msg.onEventMsg(out);
            }
        }
        catch (SAXException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void close()

    {
        try
        {
            if (in != null)
            {
                in.close();
            }
            if (out != null)
            {
                out.flush();
                out.close();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}