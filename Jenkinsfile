
pipeline {
    //声明在jenkins任何节点都可用
    agent any

    tools {
        maven 'maven3.6.1'
    }

    environment {
      JAVA8="/var/jenkins_home/tools/hudson.model.JDK/jdk8u181/jdk1.8.0_181/bin/java"
      JAVA82="/var/jenkins_home/tools/hudson.model.JDK/jdk8u201/jdk1.8.0_201/bin/java"
        PROJECT_NAME = "jac"
        SERVICE_NAME = "back"
        BRANCH_NAME = "test"
    }

    triggers {
        GenericTrigger(
                genericVariables: [
                        [key: 'allData', value: '$']
                ],
                causeString: 'Triggered on $ref',
                token: 'web-dkserver',
//                printContributedVariables: true,
                printPostContent: true
        )
    }

    stages {

//        stage('Build with JDK 8') {
//            tools {
//                jdk 'jdk8u201'
//            }
//            steps {
//                sh "$JAVA8 -version"
//            }
//        }
//
//
//        stage('Checkout') {
//            steps {
//                // 使用 echo 函数打印输出
//                sh "echo $ref"
//                sh "printenv"
//                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '217dfbc7-70b9-485b-8cfc-515b9ad785cc', url: 'http://172.16.70.112:7990/scm/back/dkserver-back-jac.git']]])
//            }
//        }
        stage('Build') {
            steps {
                sh "mvn -v"
                // 使用Maven构建Java项目，并生成JAR包
                sh 'mvn clean package -Dmaven.test.skip=true'
            }
        }
        stage('run') {
            steps {
                // 将生成的JAR包上传到远程服务器
                sshPublisher(
                        publishers: [sshPublisherDesc(
                                configName: "172.16.70.111", // 使用定义的SSH配置名称
                                transfers: [sshTransfer(
                                        // 是否删除目标服务器上已存在的文件，默认为false
                                        cleanRemote: false,
                                        // 排除不需要上传的文件，使用Ant样式的通配符，可选
                                        excludes: '',
                                        // 在远程服务器上执行的命令，例如：进入目录、停止服务等
                                        execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}",
                                        // 执行命令的超时时间，单位为毫秒，默认为 120000 (2分钟)
                                        execTimeout: 120000,
                                        // 是否将源文件路径扁平化，默认为false
                                        flatten: false,
                                        // 是否在传输时创建空的目录，默认为false
                                        makeEmptyDirs: false,
                                        // 是否使用默认的排除规则，默认为false
                                        noDefaultExcludes: false,
                                        // 用于分隔文件路径的模式，默认为'[, ]+'
                                        patternSeparator: '[, ]+',
                                        // 目标服务器上的目录路径，上传的JAR包将放在此路径下
                                        remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}",
                                        // 是否使用SimpleDateFormat格式化目标路径，默认为false
                                        remoteDirectorySDF: false,
                                        // 上传文件时去掉的前缀，默认为空
                                        removePrefix: 'target/',
                                        // 需要上传的文件路径，使用Ant样式的通配符，例如：'target/*.jar'
                                        sourceFiles: 'target/*.jar', // 上传的JAR包路径
                                )],
                                // 是否使用构建的时间戳作为远程路径，默认为false
                                usePromotionTimestamp: false,
                                // 是否在传输期间使用工作区路径，默认为false
                                useWorkspaceInPromotion: false,
                                // 是否在控制台输出详细信息，默认为false
                                verbose: true
                        ),
                                     sshPublisherDesc(
                                             configName: '172.16.70.111', // 使用定义的SSH配置名称
                                             sshLabel: [label: 'origin/master'],
                                             transfers: [sshTransfer(
                                                     execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME} && sh run.sh -n ${env.SERVICE_NAME}-${env.BRANCH_NAME} -t ${env.PROJECT_NAME}",
                                                     remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}",
                                                     removePrefix: '',
                                                     sourceFiles: 'Dockerfile',
                                             )],
                                             verbose: true
                                     )

                        ]
                )
            }
        }



    }
}
