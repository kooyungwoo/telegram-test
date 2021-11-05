package com.telegram;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TelMessVO {
	//메시지 아이디(update_id)
	private Integer messId;
	//메시지 수신시간(밀리세컨드)
	private Integer messDate;
	//메시지 내용
	private String messText;
	//채팅 언어
	private String messLan;
	//메시지 발송자 성
	private String messLastNm;
	//메시지 발송자 이름
	private String messFirstNm;
	//메시지 발송자 아이디
	private long chatId;
	//메시지 발송자 봇 여부
	private boolean isBot;
	
	@Builder(builderClassName = "ByTelMessVOBuilder", builderMethodName = "ByAllBuilder")
	public TelMessVO(Integer messId, Integer messDate, String messText, String messLan, String messLastNm, String messFirstNm, long chatId, boolean isBot) {
		this.messId = messId;
		this.messDate = messDate;
		this.messText = messText;
		this.messLan = messLan;
		this.messLastNm = messLastNm;
		this.messFirstNm = messFirstNm;
		this.chatId = chatId;
		this.isBot = isBot;
	}
}	
