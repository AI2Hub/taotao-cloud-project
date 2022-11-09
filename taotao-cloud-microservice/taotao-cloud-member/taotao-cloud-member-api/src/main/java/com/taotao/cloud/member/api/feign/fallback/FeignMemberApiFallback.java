package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.member.api.model.vo.MemberVO;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class FeignMemberApiFallback implements FallbackFactory<IFeignMemberApi> {

	@Override
	public IFeignMemberApi create(Throwable throwable) {
		return new IFeignMemberApi() {

			@Override
			public SecurityUser getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail) {
				return null;
			}

			@Override
			public MemberVO findMemberById(Long id) {
				return null;
			}

			@Override
			public Boolean updateMemberPoint(Long payPoint, String name, Long memberId, String s) {
				return null;
			}

			@Override
			public MemberVO findByUsername(String username) {
				return null;
			}

			@Override
			public MemberVO getById(Long memberId) {
				return null;
			}

			@Override
			public Boolean update(Long memberId, Long sotreId) {
				return false;
			}

			@Override
			public Boolean updateById(MemberVO member) {
				return false;
			}

			@Override
			public List<Map<String, Object>> listFieldsByMemberIds(String s, List<String> ids) {
				return null;
			}
		};
	}
}
