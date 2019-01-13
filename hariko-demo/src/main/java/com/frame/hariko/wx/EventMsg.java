package com.frame.hariko.wx;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.frame.hariko.util.MessageUtil;
import com.thoughtworks.xstream.XStream;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 事件统一处理类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EventMsg extends AbstractMsg {
    // 事件列表：CLICK(点击自定义菜单)/subscribe(关注)/unsubscribe(取消关注)/SCAN(扫描二维码)/LOCATION(上报地理位置)
    private String event;

    private String eventKey;
    private String ticket;

    private String latitude;
    private String longitude;
    private String precision;

    /**
     * 从Document中读取各事件独有的属性数据
     */
    @Override
    protected void readBody(Document document) {
        event = getElementContent(document, "Event").toLowerCase();
        System.out.println("readBody:" + event);
        switch (event) {
            case "click": {
                eventKey = getElementContent(document, "EventKey");
                break;
            }
            case "subscribe":
            case "unsubscribe":
            case "scan": {
                eventKey = getElementContent(document, "EventKey");
                ticket = getElementContent(document, "Ticket");
                break;
            }
            case "location": {
                latitude = getElementContent(document, "Latitude");
                longitude = getElementContent(document, "Longitude");
                precision = getElementContent(document, "Precision");
                break;
            }
            default: {
                break;
            }
        }
    }

    /**
     * 回复时专用，由于事件无须回复，在此空实现
     */
    @Override
    protected void writeBody(Element root, Document document) {
    }

    public void onEventMsg(PrintWriter out) {
        System.out
                .println("此处添加处理事件的业务逻辑，对于关注事件回复：尊敬的 某某某 女士/先生，欢迎您关注我的个人公众号！");
        switch (event) {
            case "subscribe": {
                getTextMsg(out);
                //getNewsMsg(out);
                break;
            }
        }
    }

    private void getNewsMsg(PrintWriter out) {
        List<News> newsList = new ArrayList<News>();
        NewsMessage newsMessage = new NewsMessage();
        //组建一条图文↓ ↓ ↓
        News newsItem = new News();
        newsItem.setTitle("欢迎关注我的公众号");
        newsItem.setDescription("进行操作之前请先注册！");
        //newsItem.setPicUrl(WXConstants.BASE_SERVER + "/image/wx/login_article_cover.png");
        newsItem.setPicUrl("http://pic3.nipic.com/20090702/918855_174429094_2.jpg");
        newsItem.setUrl("http://www.baidu.com");
        newsList.add(newsItem);
        //组装图文消息相关信息
        newsMessage.setToUserName(this.fromUserName);
        newsMessage.setFromUserName(this.toUserName);
        newsMessage.setCreateTime(this.createTime);
        newsMessage.setMsgType("news");
        newsMessage.setArticles(newsList);
        newsMessage.setArticleCount(newsList.size());

       // String message = MessageUtil.initNewsMessage(this.toUserName, this.fromUserName);
        String message = newsMessage.write();
        System.out.println(message);
        out.print(message);
    }

    private void getTextMsg(PrintWriter out) {
        TextMsg msg = new TextMsg();
        msg.setFromUserName(this.toUserName);
        msg.setToUserName(this.fromUserName);
        msg.setCreateTime(this.createTime);
        msg.setMsgType("text");
        msg.setContent("尊敬的女士/先生，<a href='http://www.baidu.com'>请进行征信授权！</a>");
        out.print(msg.write());
    }
}