package com.hxh.basic.project.service;

import org.springframework.stereotype.Service;

@Service
public interface IVedioService {

    Object getList(String type, String tag, String sort, String page_start, Integer page_limit);

    Object getDouBanDeatil(String detailId);
}
