
pipeline {
    //声明在jenkins任何节点都可用
    agent any
    environment {
        PROJECT_NAME = "jac"
        SERVICE_NAME = "back"
        BRANCH_NAME = "test"
        // 定义远程服务器的SSH配置名称
        remoteServer = 'your-ssh-server' // 替换为SSH配置名称
        remoteDirectory = '/path/to/remote/directory' // 替换为目标服务器上的目录路径
    }

    triggers {
        GenericTrigger(
                genericVariables: [
                        [key: 'allData', value: '$']
                ],
                causeString: 'Triggered on $ref',
                token: 'web-dkserver',
                printContributedVariables: true,
                printPostContent: true
        )
    }

    stages {
        stage('Checkout') {
            steps {
                // 使用 echo 函数打印输出
                echo 'Checkout'
                sh "echo $ref"
                sh "printenv"
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '217dfbc7-70b9-485b-8cfc-515b9ad785cc', url: 'http://172.16.70.112:7990/scm/back/dkserver-back-jac.git']]])
            }
        }
//        stage('Build') {
//            steps {
//                // 使用Maven构建Java项目，并生成JAR包
//                sh 'clean package -Dmaven.test.skip=true'
//            }
//        }
        stage('Run') {
            steps {
                // 使用 echo 函数打印输出
                echo 'Build'
                echo "location = /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}"
                echo "shell =  sh run.sh -n ${env.SERVICE_NAME}${env.BRANCH_NAME} -t ${env.PROJECT_NAME}"
                sshPublisher(publishers: [sshPublisherDesc(configName: '172.16.70.111', sshLabel: [label: 'origin/master'], transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}", remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'Dockerfile'), sshTransfer(cleanRemote: false, excludes: '', execCommand:
                        "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME} && sh run.sh -n ${env.SERVICE_NAME}-${env.BRANCH_NAME} -t ${env.PROJECT_NAME}", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}", remoteDirectorySDF: false, removePrefix: 'target/', sourceFiles: 'target/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])

            }
//            steps {
//                // 将生成的JAR包上传到远程服务器
//                sshPublisher(
//                        publishers: [sshPublisherDesc(
//                                configName: "${remoteServer}", // 使用定义的SSH配置名称
//                                transfers: [sshTransfer(
//                                        sourceFiles: 'target/*.jar', // 上传的JAR包路径
//                                        remoteDirectory: "${remoteDirectory}" // 目标服务器上的目录路径
//                                )]
//                        )]
//                )
//            }
        }


    }
}
