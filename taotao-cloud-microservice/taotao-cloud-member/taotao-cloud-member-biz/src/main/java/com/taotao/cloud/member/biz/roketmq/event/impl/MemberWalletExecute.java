package com.taotao.cloud.member.biz.roketmq.event.impl;


import com.taotao.cloud.member.api.feign.IFeignMemberWalletApi;
import com.taotao.cloud.member.biz.model.entity.Member;
import com.taotao.cloud.member.biz.roketmq.event.MemberRegisterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会员钱包创建
 */
@Service
public class MemberWalletExecute implements MemberRegisterEvent {

	@Autowired
	private IFeignMemberWalletApi memberWalletApi;

	@Override
	public void memberRegister(Member member) {
		// 有些情况下，会同时创建一个member_id的两条数据
		memberWalletApi.save(member.getId(), member.getUsername());
	}
}
