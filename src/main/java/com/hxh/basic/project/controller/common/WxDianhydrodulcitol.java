package com.hxh.basic.project.controller.common;

import com.hxh.basic.project.enums.ResultEnum;
import com.hxh.basic.project.form.user.AddUserForm;
import com.hxh.basic.project.service.IDownLoadService;
import com.hxh.basic.project.service.IVedioService;
import com.hxh.basic.project.utils.ResultVoUtil;
import com.hxh.basic.project.utils.string.ParamUtils;
import com.hxh.basic.project.utils.string.StrUtils;
import com.hxh.basic.project.vo.ResultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@Api(tags = "去水印小程序")
@AllArgsConstructor
@RequestMapping("/xcx")
public class WxDianhydrodulcitol {


    private final String XCX_LOCATION = "/www/wwwroot/www.blxf.vip/tempFile/";


    private IDownLoadService downloadService;
    private IVedioService vedioService;

    @ApiOperation("下载文件")
    @PostMapping("/downFile")
    public ResultVo addUdownFileser(@RequestParam(value = "url", defaultValue = "") String url) {
        ParamUtils.isnotNull(url, "url");

        return ResultVoUtil.success(downloadService.dwonFileByUrl(url, XCX_LOCATION));
    }

    @ApiOperation("获取豆瓣视频列表")
    @PostMapping("/getVedioList")
    public ResultVo getVedio(
            @RequestParam(value = "type", defaultValue = "") String type,
            @RequestParam(value = "tag", defaultValue = "") String tag,
            @RequestParam(value = "sort", defaultValue = "") String sort,
            @RequestParam(value = "page_limit", defaultValue = "0") Integer page_limit,
            @RequestParam(value = "page_start", defaultValue = "") String page_start
    ) {
        ParamUtils.isnotNull(type, "type");
        ParamUtils.isnotNull(tag, "tag");
        ParamUtils.isnotNull(sort, "sort");
        ParamUtils.isNullOrDefault(page_limit, 15);
        ParamUtils.isNullOrDefault(page_start, 0);
        return ResultVoUtil.success(vedioService.getList(type, tag, sort, page_start, page_limit));
    }


    @ApiOperation("获取豆瓣视频详情")
    @PostMapping("/getVedioDetail")
    public ResultVo getVedioDetail(
            @RequestParam(value = "detailId",defaultValue = "") String detailId
    ) {
        ParamUtils.isnotNull(detailId, "detailId");
        return ResultVoUtil.success(vedioService.getDouBanDeatil(detailId));
    }


}
