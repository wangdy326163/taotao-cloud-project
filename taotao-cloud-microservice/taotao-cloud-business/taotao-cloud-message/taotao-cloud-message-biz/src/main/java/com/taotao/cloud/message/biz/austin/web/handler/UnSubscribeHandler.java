package com.taotao.cloud.message.biz.austin.web.handler;

import com.taotao.cloud.message.biz.austin.common.constant.OfficialAccountParamConstant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;


/**
 * @author 3y
 */
@Component("unSubscribeHandler")
@Slf4j
public class UnSubscribeHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
			WxMpService wxMpService, WxSessionManager sessionManager) {
		return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser())
				.toUser(wxMessage.getFromUser())
				.content(OfficialAccountParamConstant.UNSUBSCRIBE_TIPS).build();
	}
}
