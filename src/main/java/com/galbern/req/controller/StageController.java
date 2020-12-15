package com.galbern.req.controller;

import com.galbern.req.service.StageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stage")
public class StageController {
    @Autowired
    StageService stageService;
}
