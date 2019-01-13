package com.frame.hariko.sao.req;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetQrTicketReq implements Serializable {
    private long expire_seconds = 1800l;
    private String action_name = "QR_SCENE";
    private ActionInfo action_info;
}
