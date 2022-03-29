package com.taotao.cloud.sys.api.vo.redis;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RedisVo
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 14:07:32
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RedisVo implements Serializable {

	private String key;

	private String value;
}
