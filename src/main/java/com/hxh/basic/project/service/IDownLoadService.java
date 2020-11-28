package com.hxh.basic.project.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface IDownLoadService {
    Object dwonFileByUrl(String url, String xcx_location);
}
