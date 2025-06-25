package com.miyu.cloud.macs.restServer.service;

import com.miyu.cloud.macs.dal.dataobject.device.DeviceDO;
import com.miyu.cloud.macs.dal.dataobject.door.DoorDO;
import com.miyu.cloud.macs.restServer.entity.MacsRestImageUploadVO;
import com.miyu.cloud.macs.restServer.entity.MacsRestPersonUpdateVO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public interface MacsRestService {

    void saveInformation(MacsRestImageUploadVO imageUpload, String remoteAddr) throws Exception;

    void checkUserAndVisitor() throws URISyntaxException;

    List<MacsRestPersonUpdateVO> addUserAndVisitor(URI uri, List<MacsRestPersonUpdateVO> userList);

    List<MacsRestPersonUpdateVO> updateUserAndVisitor(URI uri, List<MacsRestPersonUpdateVO> userList);

    List<MacsRestPersonUpdateVO> deleteUserAndVisitor(URI uri, List<MacsRestPersonUpdateVO> userList);

    void deleteAllUserAndVisitor(DeviceDO device) throws URISyntaxException;
}
