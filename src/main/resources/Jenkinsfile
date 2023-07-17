pipeline {
    //声明在jenkins任何节点都可用
     agent any
     //声明一些环境变量
     environment {
             //企业微信webhook地址，用于企业微信的推送
             url = 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=b5e1d5f9-a58a-4db4-b42a-3f2f890f80a5'
             //获取环境变量，拿到项目名称和分支名称
            def projectName = "${env.JOB_NAME}"
            def branchName = "${env.BRANCH_NAME}"
     }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
         script {
                  // 使用 echo 函数打印输出
                  echo 'Checkout'
                }
      }
    }

    stage('Build') {
      steps {
        sh 'mvn clean package'
         script {
                  // 使用 echo 函数打印输出
                  echo 'mvn clean package'
                }
      }
    }

    stage('Test') {
      steps {
        sh 'mvn test'
         script {
                  // 使用 echo 函数打印输出
                  echo 'mvn test'
                }
      }
    }

    stage('Deploy') {
      steps {
        sh 'mvn deploy'
         script {
                  // 使用 echo 函数打印输出
                  echo 'mvn deploy'
                }
      }
    }
  }
}
