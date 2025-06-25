// pipeline 定义一个流水线脚本
// agent 指示 Jenkins 为整个流水线分配一个执行器在 Jenkins 环境中的任何可用代理/节点上和工作区。
// stages 全部的工作都在这里执行
// stage 每个工作开始
// steps jenkinsfile 声明式脚本往这里面写
// echo 写一个简单的字符串到控制台输出。



pipeline {
  agent {
    kubernetes {
      label 'maven'
    }

  }
  stages {
    stage('checkout scm') {
      agent none
      steps {
        git(url: 'https://git.miyutech.cn/CIMP/cimp-apps.git', credentialsId: 'gita', changelog: true, poll: false, branch: "$BRANCH_NAME")
      }
    }

    stage('unit test') {
      agent none
      steps {
        container('maven') {
          sh '''cd ${SERVICE}
mvn clean test'''
        }

      }
    }
    //sonarqube 源代码检测工具
    stage('sonarqube analysis') {
      agent none
      steps {
       //通过withCredentials调用Jenkins凭据中已保存的凭据，credentialsId需要填写，其他保持默认即可
        withCredentials([string(credentialsId : 'sonarqube' ,variable : 'SONAR_TOKEN' ,)]) {
          withSonarQubeEnv('sonar') {
            container('maven') {
              sh '''service_name=${SERVICE#*/}
service_name=${service_name#*/}

cd ${SERVICE}
mvn sonar:sonar -Dsonar.projectKey=${service_name}
echo "mvn sonar:sonar -Dsonar.projectKey=${service_name}"'''
            }

          }

        }

        timeout(time: 1, unit: 'HOURS') {
          waitForQualityGate true
        }

      }
    }

    stage('build & push') {
      agent none
      steps {
        withCredentials([usernamePassword(credentialsId : 'harbor-user-pass' ,passwordVariable : 'DOCKER_PASSWORD' ,usernameVariable : 'DOCKER_USERNAME' ,)]) {
          container('maven') {
            sh '''cd ${SERVICE}
mvn clean package -DskipTests
cd ${WORKSPACE}
ls
chmod -R 777 deploy/copy.sh && deploy/copy.sh'''
            sh '''echo "${DOCKER_PASSWORD}" | docker login ${REGISTRY} -u "${DOCKER_USERNAME}" --password-stdin

service_name=${SERVICE#*/}
service_name=${service_name#*/}
cd deploy/${service_name}/build
ls /target
docker build -f Dockerfile -t ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${service_name}:SNAPSHOT-$BUILD_NUMBER .
docker push ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${service_name}:SNAPSHOT-${BUILD_NUMBER}'''
          }

        }

      }
    }

    stage('push latest') {
      agent none
      steps {
        container('maven') {
          sh '''service_name=${SERVICE#*/}
service_name=${service_name#*/}
cd deploy/${service_name}/build

docker tag ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${service_name}:SNAPSHOT-${BUILD_NUMBER} ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${service_name}:latest
docker push ${REGISTRY}/${DOCKERHUB_NAMESPACE}/${service_name}:latest
'''
        }

      }
    }

    stage('deploy to dev') {
      agent none
      steps {
        input(id: 'deploy-to-dev', message: 'deploy to dev?')
        container('maven') {
          withCredentials([kubeconfigContent(credentialsId : 'kuberconfig-id' ,variable : 'ADMIN_KUBECONFIG' ,)]) {
            sh '''
service_name=${SERVICE#*/}
service_name=${service_name#*/}
cd deploy/${service_name}

sed -i\'\' "s#REGISTRY#${REGISTRY}#" deployment.yaml
sed -i\'\' "s#DOCKERHUB_NAMESPACE#${DOCKERHUB_NAMESPACE}#" deployment.yaml
sed -i\'\' "s#APP_NAME#${service_name}#" deployment.yaml
sed -i\'\' "s#BUILD_NUMBER#${BUILD_NUMBER}#" deployment.yaml
sed -i\'\' "s#REPLICAS#${REPLICAS}#" deployment.yaml

mkdir ~/.kube
echo "$ADMIN_KUBECONFIG" > ~/.kube/config


kubectl apply -f .'''
          }

        }

      }
    }

  }
  environment {
    APP_NAME = 'yudao'
    DOCKER_CREDENTIAL_ID = 'harbor-user-pass'
    REGISTRY = '192.168.2.221'
    GIT_REPO_URL = 'https://git.miyutech.cn/'
    GIT_CREDENTIAL_ID = 'gita'
    GIT_ACCOUNT = 'cimp-apps'
    SONAR_CREDENTIAL_ID = 'sonarqube-token'
    KUBECONFIG_CREDENTIAL_ID = 'kubeconfig-id'
  }
  parameters {
    choice(name: 'SERVICE', choices: ['nacos', 'yudao/yudao-gateway', 'yudao/yudao-module-system','yudao/yudao-module-infra'], description: '请选择要部署的服务')
    choice(name: 'DOCKERHUB_NAMESPACE', choices: ['snapshots', 'releases'], description: '请选择部署到哪个镜像仓库')
    choice(name: 'REPLICAS', choices: ['1', '3', '5', '7'], description: '请选择构建后的副本数')
    string(name: 'BRANCH_NAME', defaultValue: 'deviops', description: '请输入要构建的分支名称')
    string(name: 'TAG_NAME', defaultValue: 'snapshots', description: '部署版本：必须以 v 开头，例如：v1、v1.0.0')
  }
}
