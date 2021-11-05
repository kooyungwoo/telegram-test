package com.telegram;

import java.util.LinkedList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component("messageInfoStorage")
public class MessageInfoStorage {
	private static LinkedList<TelMessVO> messQueue = new LinkedList<TelMessVO>();
	
	public void messSave(TelMessVO telMessVo) {
		messQueue.add(telMessVo);
	}
	
	public void messRemove(TelMessVO telMessVo) {
		messQueue.remove(telMessVo);
	}
	
	public void messFirstRemove() {
		messQueue.remove();
	}
	
	public void messRemove(int index) {
		messQueue.remove(index);
	}
	
	//데이터 확인 후 삭제가 필요한 경우 삭제 
	public boolean messRemove(Integer messId) {
		return messQueue.removeIf(telMessVO -> telMessVO.getMessId()==messId);
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<TelMessVO> getQueue(String searchWord){
		LinkedList<TelMessVO> clone = (LinkedList<TelMessVO>) messQueue.clone();
		
		//검색어가 있는경우 검색 조건에 맞는 메시지가 있는 경우에만 넘겨주게 작업
		if(searchWord!=null && !searchWord.equals("")) {
			clone = clone.stream().filter(telMessVO -> telMessVO.getMessText().indexOf(searchWord)!=-1).collect(Collectors.toCollection(LinkedList::new));
		}
		return clone;
	}
}
