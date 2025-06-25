package com.miyu.cloud.macs.restServer.api;

import com.miyu.cloud.macs.restServer.entity.MacsRestDevice;
import com.miyu.cloud.macs.restServer.entity.MacsRestDoor;
import com.miyu.cloud.macs.restServer.entity.MacsRestPersonUpdateVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;
import java.util.List;

@Component
@FeignClient(contextId = "macs-cloud", url = "empty", name = "net-debug")
public interface MacsInteractiveApi {

    @PostMapping(value = "/api/Door/connectionInit")
    boolean connectionInit(URI uri, List<MacsRestDevice> devices);

    //执行指令
    @PostMapping(value = "/api/Door/executeInstruct")
    boolean executeInstruct(URI uri, MacsRestDevice device);

    @PostMapping(value = "/api/Door/addUserVisitor")
    String addUserVisitorToDevice(URI uri, MacsRestPersonUpdateVO person);

    @PostMapping(value = "/api/Door/updateUserVisitor")
    boolean updateUserVisitorToDevice(URI uri, MacsRestPersonUpdateVO person);

    @PostMapping(value = "/api/Door/deleteUserVisitor")
    boolean deleteUserVisitorFromDevice(URI uri, MacsRestPersonUpdateVO person);

    @PostMapping(value = "/api/Door/deleteAllUserVisitor")
    boolean deleteAllUserVisitorFromDevice(URI uri, MacsRestPersonUpdateVO person);
}
