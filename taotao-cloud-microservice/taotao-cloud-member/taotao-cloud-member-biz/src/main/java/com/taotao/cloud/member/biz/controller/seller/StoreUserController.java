package com.taotao.cloud.member.biz.controller.seller;

import com.taotao.cloud.member.biz.entity.Member;
import com.taotao.cloud.member.biz.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 店铺端,管理员接口
 *
 * 
 * @since 2020/11/16 10:57
 */
@RestController
@Api(tags = "店铺端,管理员接口")
@RequestMapping("/store/user")
public class StoreUserController {
    @Autowired
    private MemberService memberService;


    @GetMapping(value = "/info")
    @ApiOperation(value = "获取当前登录用户接口")
    public ResultMessage<Member> getUserInfo() {
        AuthUser tokenUser = UserContext.getCurrentUser();
        if (tokenUser != null) {
            Member member = memberService.findByUsername(tokenUser.getUsername());
            member.setPassword(null);
            return ResultUtil.data(member);
        }
        throw new ServiceException(ResultCode.USER_NOT_LOGIN);
    }


}
