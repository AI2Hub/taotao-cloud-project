package com.taotao.cloud.promotion.biz.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.promotion.api.vo.PointsGoodsSearchParams;
import com.taotao.cloud.promotion.api.vo.PointsGoodsVO;
import com.taotao.cloud.promotion.biz.entity.PointsGoods;
import com.taotao.cloud.promotion.biz.service.PointsGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端,积分商品接口
 *
 * 
 * @since 2021/1/14
 **/
@RestController
@Api(tags = "管理端,积分商品接口")
@RequestMapping("/manager/promotion/pointsGoods")
public class PointsGoodsManagerController {
    @Autowired
    private PointsGoodsService pointsGoodsService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "添加积分商品")
    public Result<Object> addPointsGoods(@RequestBody List<PointsGoods> pointsGoodsList) {
        if (pointsGoodsService.savePointsGoodsBatch(pointsGoodsList)) {
            return Result.success();
        }
        return Result.error(ResultEnum.POINT_GOODS_ERROR);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "修改积分商品")
    public Result<Object> updatePointsGoods(@RequestBody PointsGoodsVO pointsGoods) {
        Objects.requireNonNull(UserContext.getCurrentUser());
        pointsGoodsService.updatePromotions(pointsGoods);
        return Result.success();
    }

    @PutMapping("/status/{ids}")
    @ApiOperation(value = "修改积分商品状态")
    public Result<Object> updatePointsGoodsStatus(@PathVariable String ids, Long startTime, Long endTime) {
        if (pointsGoodsService.updateStatus(Arrays.asList(ids.split(",")), startTime, endTime)) {
            return Result.success();
        }
        return Result.error(ResultEnum.POINT_GOODS_ERROR);
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除积分商品")
    public Result<Object> delete(@PathVariable String ids) {
        if (pointsGoodsService.removePromotions(Arrays.asList(ids.split(",")))) {
            return Result.success();
        }
        throw new BusinessException(ResultEnum.POINT_GOODS_ERROR);
    }

    @GetMapping
    @ApiOperation(value = "分页获取积分商品")
    public Result<IPage<PointsGoods>> getPointsGoodsPage(PointsGoodsSearchParams searchParams, PageVO page) {
        IPage<PointsGoods> pointsGoodsByPage = pointsGoodsService.pageFindAll(searchParams, page);
        return Result.success(pointsGoodsByPage);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取积分商品详情")
    public Result<Object> getPointsGoodsDetail(@PathVariable String id) {
        PointsGoodsVO pointsGoodsDetail = pointsGoodsService.getPointsGoodsDetail(id);
        return Result.success(pointsGoodsDetail);
    }

}
