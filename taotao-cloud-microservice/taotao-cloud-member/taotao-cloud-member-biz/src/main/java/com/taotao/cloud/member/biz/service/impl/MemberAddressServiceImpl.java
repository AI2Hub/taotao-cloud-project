package com.taotao.cloud.member.biz.service.impl;

import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taotao.cloud.member.biz.entity.MemberAddress;
import com.taotao.cloud.member.biz.mapper.MemberAddressMapper;
import com.taotao.cloud.member.biz.service.MemberAddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * 收货地址业务层实现
 *
 *
 * @since 2020/11/18 9:44 上午
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MemberAddressServiceImpl extends ServiceImpl<MemberAddressMapper, MemberAddress> implements
	MemberAddressService {

    @Override
    public IPage<MemberAddress> getAddressByMember(PageVO page, String memberId) {
        return this.page(PageUtil.initPage(page),
                new QueryWrapper<MemberAddress>()
                        .eq("member_id", memberId));

    }

    @Override
    public MemberAddress getMemberAddress(String id) {
        return this.getOne(
                new QueryWrapper<MemberAddress>()
                        .eq("member_id", Objects.requireNonNull(UserContext.getCurrentUser()).getId())
                        .eq("id", id));
    }

    /**
     * 根据地址ID获取当前会员地址信息
     *
     * @return 当前会员的地址信息
     */
    @Override
    public MemberAddress getDefaultMemberAddress() {
        return this.getOne(
                new QueryWrapper<MemberAddress>()
                        .eq("member_id", Objects.requireNonNull(UserContext.getCurrentUser()).getId())
                        .eq("is_default", true));
    }

    @Override
    public MemberAddress saveMemberAddress(MemberAddress memberAddress) {
        //判断当前地址是否为默认地址，如果为默认需要将其他的地址修改为非默认
        removeDefaultAddress(memberAddress);
        //添加会员地址
        this.save(memberAddress);

        return memberAddress;
    }

    @Override
    public MemberAddress updateMemberAddress(MemberAddress memberAddress) {
        MemberAddress originalMemberAddress = this.getMemberAddress(memberAddress.getId());
        if (originalMemberAddress != null &&
                originalMemberAddress.getMemberId().equals(Objects.requireNonNull(UserContext.getCurrentUser()).getId())) {

            if (memberAddress.getIsDefault() == null) {
                memberAddress.setIsDefault(false);
            }
            //判断当前地址是否为默认地址，如果为默认需要将其他的地址修改为非默认
            removeDefaultAddress(memberAddress);
            this.saveOrUpdate(memberAddress);
        }

        return memberAddress;
    }

    @Override
    public boolean removeMemberAddress(String id) {
        return this.remove(new QueryWrapper<MemberAddress>()
                .eq("id", id));
    }

    /**
     * 修改会员默认收件地址
     *
     * @param memberAddress 收件地址
     */
    private void removeDefaultAddress(MemberAddress memberAddress) {
        //如果不是默认地址不需要处理
        if (Boolean.TRUE.equals(memberAddress.getIsDefault())) {
            //将会员的地址修改为非默认地址
            LambdaUpdateWrapper<MemberAddress> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
            lambdaUpdateWrapper.set(MemberAddress::getIsDefault, false);
            lambdaUpdateWrapper.eq(MemberAddress::getMemberId, memberAddress.getMemberId());
            this.update(lambdaUpdateWrapper);
        }

    }
}
