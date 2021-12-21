package com.taotao.cloud.member.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员信息修改DTO
 *
 * 
 * @since 2020/12/11 14:39
 */
@Schema(description = "会员信息修改DTO")
public class MemberEditDTO {


	@Schema(description = "昵称", required = true)
	@Size(min = 2, max = 20, message = "会员昵称必须为2到20位之间")
	private String nickName;

	@Schema(description = "会员地址ID")
	private String regionId;

	@Schema(description = "会员地址")
	private String region;

	@Min(message = "必须为数字且1为男,0为女", value = 0)
	@Max(message = "必须为数字且1为男,0为女", value = 1)
	@NotNull(message = "会员性别不能为空")
	@Schema(description = "会员性别,1为男，0为女", required = true)
	private Integer sex;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "会员生日")
	private Date birthday;

	@Schema(description = "详细地址")
	private String address;

	@Schema(description = "会员头像")
	private String face;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getRegionId() {
		return regionId;
	}

	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}
}
