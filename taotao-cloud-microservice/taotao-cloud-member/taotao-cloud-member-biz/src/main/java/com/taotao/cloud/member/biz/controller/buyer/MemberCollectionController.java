package com.taotao.cloud.member.biz.controller.buyer;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.member.api.vo.GoodsCollectionVO;
import com.taotao.cloud.member.biz.service.GoodsCollectionService;
import com.taotao.cloud.member.biz.service.StoreCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,会员收藏接口
 */
@Validated
@RestController
@RequestMapping("/member/buyer/member-collection")
@Tag(name = "买家端-会员收藏API", description = "买家端-会员收藏API")
public class MemberCollectionController {

	/**
	 * 会员商品收藏
	 */
	@Autowired
	private GoodsCollectionService goodsCollectionService;
	/**
	 * 会员店铺
	 */
	@Autowired
	private StoreCollectionService storeCollectionService;
	/**
	 * 商品收藏关键字
	 */
	private static final String goods = "GOODS";

	@Operation(summary = "查询会员收藏列表", description = "查询会员收藏列表", method = CommonConstant.GET)
	@RequestLogger(description = "查询会员收藏列表")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping("/{type}")
	public Result<PageModel<GoodsCollectionVO>> goodsListPage(
		@Parameter(description = "类型", required = true) @PathVariable String type,
		@Validated PageParam page) {
		if (goods.equals(type)) {
			return Result.success(goodsCollectionService.goodsCollection(page));
		}
		return Result.success(storeCollectionService.storeCollection(page));
	}

	@Operation(summary = "添加会员收藏", description = "添加会员收藏", method = CommonConstant.POST)
	@RequestLogger(description = "添加会员收藏")
	@PreAuthorize("@el.check('admin','timing:list')")
	@PostMapping("/{type}/{id}")
	public Result<Boolean> addGoodsCollection(
		@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
		@Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable String id) {
		if (goods.equals(type)) {
			return Result.success(goodsCollectionService.addGoodsCollection(id));
		}
		return Result.success(storeCollectionService.addStoreCollection(id));

	}

	@Operation(summary = "删除会员收藏", description = "删除会员收藏", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除会员收藏")
	@PreAuthorize("@el.check('admin','timing:list')")
	@DeleteMapping(value = "/{type}/{id}")
	public Result<Object> deleteGoodsCollection(
		@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
		@Parameter(description = "id", required = true) @NotNull(message = "值不能为空") @PathVariable String id) {
		if (goods.equals(type)) {
			return Result.success(goodsCollectionService.deleteGoodsCollection(id));
		}
		return Result.success(storeCollectionService.deleteStoreCollection(id));
	}

	@Operation(summary = "查询会员是否收藏", description = "查询会员是否收藏", method = CommonConstant.GET)
	@RequestLogger(description = "查询会员是否收藏")
	@PreAuthorize("@el.check('admin','timing:list')")
	@GetMapping(value = "/{type}/{id}/collection")
	public Result<Boolean> isCollection(
		@Parameter(description = "类型", required = true, example = "GOODS:商品,STORE:店铺") @PathVariable String type,
		@Parameter(description = "id", required = true) @NotBlank(message = "值不能为空") @PathVariable String id) {
		if (goods.equals(type)) {
			return Result.data(this.goodsCollectionService.isCollection(id));
		}
		return Result.data(this.storeCollectionService.isCollection(id));
	}
}
