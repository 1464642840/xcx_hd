package com.hxh.basic.project.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hxh.basic.project.common.MyThread;
import com.hxh.basic.project.service.IDownLoadService;
import com.hxh.basic.project.service.IVedioService;
import com.hxh.basic.project.utils.http.FileUtils;
import com.hxh.basic.project.utils.http.HttpHelper;
import com.hxh.basic.project.utils.string.StrUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

@Service
public class VedioServiceImpl implements IVedioService {

    private final String DOUBAN_API = "https://movie.douban.com/j/search_subjects?";

    @Override
    public Object getList(String type, String tag, String sort, String page_start, Integer page_limit) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("sort", sort);
        map.put("tag", tag);
        map.put("page_start", page_start);
        map.put("page_limit", page_limit);
        String url = DOUBAN_API + StrUtils.jsonToParam(map);
        return HttpHelper.sendPOST(url, "", null, "utf-8");

    }


    @Override
    public Object getDouBanDeatil(String detailId) {
        String url = "https://movie.douban.com/subject/" + detailId;
        HashMap<String, String> header = new HashMap<>();
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");

        String s = HttpHelper.sendGET1(url, "", header, "utf-8");
        int i = s.indexOf("<script type=\"application/ld+json\">");
        String substring = s.substring(i);
        int i1 = substring.indexOf("</script>");
        String result = s.substring(i,i1+i);
        String substring1 = result.substring(result.indexOf("{")).replaceAll("@|\r|\n|\t","");
        JSONObject jsonObject = JSONObject.parseObject(substring1);

        return jsonObject;
    }
}
