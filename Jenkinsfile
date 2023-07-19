
pipeline {
    //声明在jenkins任何节点都可用
    agent any

    tools {
        maven 'maven3.6.1'
        // 指定JDK版本
//        jdk 'jdk1.8'
    }

    environment {
        PROJECT_NAME = "jac"
        SERVICE_NAME = "back"
        BRANCH_NAME = "test"
//        JAVA_HOME = "/var/jenkins_home/tools/hudson.model.JDK/jdk1.8"
        // 定义远程服务器的SSH配置名称
        remoteServer = '172.16.70.112' // 替换为SSH配置名称
        remoteDirectory = '/home/project/jac/back/' // 替换为目标服务器上的目录路径
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
        stage('Build') {
            steps {
                sh "mvn -v"
                echo 'Checkout'
                // 使用Maven构建Java项目，并生成JAR包
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }

        stage('Run') {


            steps {

                // 将生成的JAR包上传到远程服务器
                sshPublisher(
                        publishers: [sshPublisherDesc(
                                configName: "${remoteServer}", // 使用定义的SSH配置名称
                                transfers: [sshTransfer(
                                        sourceFiles: 'target/*.jar', // 上传的JAR包路径
                                        remoteDirectory: "${remoteDirectory}" // 目标服务器上的目录路径
                                )]
                        )]
                )
                // 使用 echo 函数打印输出
                echo 'Build'
                echo "location = /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}"
                echo "shell =  sh run.sh -n ${env.SERVICE_NAME}${env.BRANCH_NAME} -t ${env.PROJECT_NAME}"


                // 运行远程命令
                sshPublisher(publishers: [sshPublisherDesc(
                        configName: '172.16.70.111', // 使用定义的SSH配置名称
                        sshLabel: [label: 'origin/master'],
                        transfers: [sshTransfer(
                                cleanRemote: false,
                                excludes: '',
                                execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME} && sh run.sh -n ${env.SERVICE_NAME}-${env.BRANCH_NAME} -t ${env.PROJECT_NAME}",
                                execTimeout: 120000,
                                flatten: false,
                                makeEmptyDirs: false,
                                noDefaultExcludes: false,
                                patternSeparator: '[, ]+',
                                remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}",
                                remoteDirectorySDF: false,
                                removePrefix: 'target/',
                                sourceFiles: 'target/*.jar'
                        )],
                        usePromotionTimestamp: false,
                        useWorkspaceInPromotion: false,
                        verbose: true
                )])

            }

        }


    }
}
