package com.miyu.module.eg.controller.admin;

import com.miyu.module.eg.modbus.DeviceManager;
import com.miyu.module.eg.modbus.ModbusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Tag(name = "modbus连接 - 测试")
@RestController
@RequestMapping("/eg/modbus")
@Validated
public class ModbusController {

    @Resource
    private ModbusService modbusService;



}
