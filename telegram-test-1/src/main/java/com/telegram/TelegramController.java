package com.telegram;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("telegramController")
@RequestMapping("**/telegram")
@CrossOrigin(origins = "*")
public class TelegramController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	@Qualifier("messageInfoStorage")
	private MessageInfoStorage messageInfoStorage;
	
	@Autowired
	@Qualifier("telegramRunner")
	private TelegramRunner telegramRunner;
	
	@RequestMapping(value="/msg/list", method = RequestMethod.GET)
	public LinkedList<TelMessVO> msgList(@RequestParam(value="searchWord", required=false) String searchWord) throws Exception{
		return messageInfoStorage.getQueue(searchWord);
	}
	
	@RequestMapping(value="/msg/send", method = RequestMethod.POST)
	public Boolean msgSend(
			@ModelAttribute() SendMsgVO sendMsgVO,
			@ModelAttribute() TelMessVO telMessVO
			) throws Exception{
		boolean isOk = telegramRunner.sendMessage(telMessVO, sendMsgVO.getSnedMsg());
		//메시지 발송 성공시 등록된 메시지 목록  key 삭제
		if(isOk) {
			messageInfoStorage.messRemove(telMessVO);
		}
		return isOk;
	}
	
	@RequestMapping(value="/msg/data/{msgId}", method = RequestMethod.DELETE)
	public Boolean msgDel(
			@PathVariable("msgId") Integer msgId
			) throws Exception{
		logger.info("msgId: "+msgId);
		
		boolean isOk = messageInfoStorage.messRemove(msgId);
		return isOk;
	}
}
