package com.galbern.req.service.Impl;

import com.galbern.req.dao.StageDao;
import com.galbern.req.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StageServiceImpl implements StageService {
    @Autowired
    private StageDao stageDao;

}
