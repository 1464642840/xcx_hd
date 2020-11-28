package com.hxh.basic.project.controller.common;

import com.hxh.basic.project.enums.ResultEnum;
import com.hxh.basic.project.form.user.AddUserForm;
import com.hxh.basic.project.service.IDownLoadService;
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


@RestController
@Api(tags = "去水印小程序")
@AllArgsConstructor
@RequestMapping("/xcx")
public class WxDianhydrodulcitol {


    private final String XCX_LOCATION="/www/wwwroot/www.blxf.vip/tempFile/";


    private IDownLoadService downloadService;

    @ApiOperation("下载文件")
    @PostMapping("/downFile")
    public ResultVo addUdownFileser(@RequestParam(value = "url",defaultValue = "") String url) {
        ParamUtils.isnotNull(url, "url");

        return ResultVoUtil.success(downloadService.dwonFileByUrl(url,XCX_LOCATION));
    }

    @ApiOperation("下载文件")
    @PostMapping("/getVedio")
    public ResultVo getVedio(@RequestParam(value = "url",defaultValue = "") String url) {
        ParamUtils.isnotNull(url, "url");

        return ResultVoUtil.success(downloadService.dwonFileByUrl(url,XCX_LOCATION));
    }



}
