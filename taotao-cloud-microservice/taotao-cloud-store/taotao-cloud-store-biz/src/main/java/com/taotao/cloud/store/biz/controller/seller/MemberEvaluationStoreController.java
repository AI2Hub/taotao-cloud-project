package com.taotao.cloud.store.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.member.api.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 店铺端,商品评价管理接口
 *
 * 
 * @since 2020-02-25 14:10:16
 */
@RestController
@Api(tags = "店铺端,商品评价管理接口")
@RequestMapping("/store/memberEvaluation")
public class MemberEvaluationStoreController {

    @Autowired
    private MemberEvaluationService memberEvaluationService;

    @ApiOperation(value = "分页获取会员评论列表")
    @GetMapping
    public Result<IPage<MemberEvaluationListVO>> getByPage(
	    EvaluationPageQuery evaluationPageQuery) {
        String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
        evaluationPageQuery.setStoreId(storeId);
        return Result.success(memberEvaluationService.queryPage(evaluationPageQuery));
    }

    @ApiOperation(value = "通过id获取")
    @ApiImplicitParam(name = "id", value = "评价ID", required = true, dataType = "String", paramType = "path")
    @GetMapping(value = "/get/{id}")
    public Result<MemberEvaluationVO> get(@PathVariable String id) {
        return Result.success(OperationalJudgment.judgment(memberEvaluationService.queryById(id)));
    }

    @ApiOperation(value = "回复评价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "评价ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "reply", value = "回复内容", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "replyImage", value = "回复图片", dataType = "String", paramType = "query")
    })
    @PutMapping(value = "/reply/{id}")
    public Result<MemberEvaluationVO> reply(@PathVariable String id, @RequestParam String reply, @RequestParam String replyImage) {
        OperationalJudgment.judgment(memberEvaluationService.queryById(id));
        memberEvaluationService.reply(id, reply, replyImage);
        return Result.success();
    }
}
