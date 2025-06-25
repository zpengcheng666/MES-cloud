#!/bin/bash


# nacos
if [ -f "nacos/target/classes/application.yml" ]; then
  echo 'copying nacos to build path...'
  mkdir -p deploy/nacos/build/target/
  cp nacos/target/*.jar deploy/nacos/build/target/

fi


# 网关
if [ -f "yudao-gateway/target/classes/application.yaml" ]; then
  echo 'copying yudao-gateway to build path...'
  mkdir -p deploy/yudao-gateway/build/target/
  cp yudao-gateway/target/*.jar deploy/yudao-gateway/build/target/
fi

#System微服务启动
if [ -f "yudao-module-system/yudao-module-system-biz/target/classes/application.yaml" ]; then
  echo 'copying yudao-module-system to build path...'
  mkdir -p deploy/yudao-module-system/build/target/
  cp yudao-module-system/yudao-module-system-biz/target/*.jar deploy/yudao-module-system/build/target/
fi


#infra 代码生成
if [ -f "yudao-module-infra/yudao-module-infra-biz/target/classes/application.yaml" ]; then
  echo 'copying yudao-module-infra to build path...'
  mkdir -p deploy/yudao-module-infra/build/target/
  cp yudao-module-infra/yudao-module-infra-biz/target/*.jar deploy/yudao-module-infra/build/target/

fi

#macs 门禁
if [ -f "miyu-cloud-macs/miyu-cloud-macs-biz/target/classes/application.yml" ]; then
  echo 'copying miyu-cloud-macs to build path...'
  mkdir -p deploy/miyu-cloud-macs/build/target/
  cp miyu-cloud-macs/miyu-cloud-macs-biz/target/*.jar deploy/miyu-cloud-macs/build/target/

fi

