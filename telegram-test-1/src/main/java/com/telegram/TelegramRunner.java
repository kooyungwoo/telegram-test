package com.telegram;

import java.util.HashSet;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import okhttp3.OkHttpClient;

//서비스 동작시 동작시작 update 내용 확인 후 메시지 저장소에 등록
@Component("telegramRunner")
public class TelegramRunner implements ApplicationRunner {
	
	@Value("${telegram.bot.token}")
	private String telegramBotToken; 
	
	@Resource(name = "messageInfoStorage")
	public MessageInfoStorage messageInfoStorage;
	
	private static TelegramBot bot;
	
	//메시지 데이터 생성시 update_id기준으로 중복 체크를 위해 추가
	private static HashSet<Integer> messUpdateSet;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		if(bot==null) {
			bot = new TelegramBot.Builder(telegramBotToken).okHttpClient(new OkHttpClient()).build();
		}
		if(messUpdateSet==null) {
			messUpdateSet = new HashSet<Integer>();
		}
		bot.setUpdatesListener(updates -> { 
			for(Update messUpdat:updates) {
				String messFirstNm 		= messUpdat.message().chat().firstName();
				String messLastNm 		= messUpdat.message().chat().lastName();
				Integer messDate 			= messUpdat.message().date();
				long chatId 					= messUpdat.message().chat().id();
				String messText 			= messUpdat.message().text();
				//boolean isBot 				= messUpdat.message().viaBot().isBot();
				boolean isBot				= false;
				//String messLan 				= messUpdat.message().viaBot().languageCode();
				String messLan 				= "kr";
				Integer messId 				= messUpdat.updateId();
				
				//신규 데이터가 들어온 경우 신규여부 체크 후 큐에 저장
				if(messUpdateSet.add(messId)) {
					TelMessVO telMessVo = TelMessVO.ByAllBuilder().messId(messId)
							.messDate(messDate).messText(messText).messLan(messLan)
							.messLastNm(messLastNm).messFirstNm(messFirstNm).chatId(chatId).isBot(isBot).build();
					
					messageInfoStorage.messSave(telMessVo);//메시지 정보 저장소에 저장
				}				
			}
			return UpdatesListener.CONFIRMED_UPDATES_ALL; 
		});
		
		//update목록 가지고 오는 리스너 멈추게 하려면 아래의 소스 실행
		//bot.removeGetUpdatesListener();
	}
	
	//메시지 발송 후 결과 전달
	public boolean sendMessage(TelMessVO telMessVo, String message) {
		SendResponse response = bot.execute(new SendMessage(telMessVo.getChatId(), message));
		return response.isOk();
	}
	
	public boolean messUpdateIdRemove(Integer messId) {
		return messUpdateSet.remove(messId);
	}
}
