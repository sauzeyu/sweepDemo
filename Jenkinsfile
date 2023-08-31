//def sendHttpRequest(String url, Map payload) {
//    sh "curl -s -X POST $url -H 'Content-Type: application/json' -d '${JsonOutput.toJson(payload)}'"
//}


pipeline {
    //声明在jenkins任何节点都可用
    agent any
    tools {
        maven 'maven3.6.1'
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

    environment {
        //企业微信webhook地址，用于企业微信的推送
        url = 'https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=b5e1d5f9-a58a-4db4-b42a-3f2f890f80a5'
        PROJECT_NAME = "jac"
        SERVICE_NAME = "back"
        BRANCH_NAME = "test"
    }


    stages {

        stage('Build') {
            steps {
                echo "env.allData: ${env.allData}"
                echo "env.allData_repository_slug: ${env.allData_repository_slug}"
                echo "env.allData_pullrequest_title: ${env.allData_pullrequest_title}"
                echo "env.allData_pullrequest_fromRef_branch_name: ${env.allData_pullrequest_fromRef_branch_name}"
                echo "env.allData_pullrequest_toRef_branch_name: ${env.allData_pullrequest_toRef_branch_name}"
                echo "env.allData_pullrequest_authorLogin: ${env.allData_pullrequest_authorLogin}"
                echo "env.allData_comment: ${env.allData_comment}"
                echo "env.allData_actor_displayName: ${env.allData_actor_displayName}"
                echo "env.allData_pullrequest_link: ${env.allData_pullrequest_link}"
                echo "env.allData_pullrequest_link: ${env.allData_pullrequest_link}"
                sh "java -version"
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
                                        execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}/${env.BRANCH_NAME}",
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
                                        remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}/${env.BRANCH_NAME}",
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
                                             sshLabel: [label: 'origin/liu'],
                                             transfers: [sshTransfer(
                                                     execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}/${env.BRANCH_NAME} && sh run.sh -n ${env.SERVICE_NAME} -b ${env.BRANCH_NAME} -t ${env.PROJECT_NAME}",
                                                     remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}/${env.BRANCH_NAME}",
                                                     removePrefix: '',
                                                     sourceFiles: 'Dockerfile',
                                             )],
                                             verbose: true
                                     )

                        ]
                )
            }
        }

        stage('send message') {
            steps {
                script {
                    def phoneNumbers = [
                            '田怡然': '18681358353',
                            '陈礼夫': '18381033475',
                            '李虎林': '13320925765',
                            '隆超': '17760482614',
                            '罗伟': '18728121329',
                            '蒲玉敏': '13980773579',
                            '田述杨': '18227646844',
                            '王雪涵': '17854298530',
                            '姚智强': '18111265258',
                            '朱霖黎': '13508357425',
                            '张力': '15927037860',


                            '唐超': '18202822982',
                            '欧阳成龙': '13880423129',
                            '刘竞择': '18108011546',
                            '余治强': '18715795031',

                            '陈胜哲': '18708128590',
                            '刘在希': '18280168756',
                            '吴溪': '17358989747',
                            '姚东': '13258265629',
                    ]


                    def  payload = [
                            msgtype : 'markdown',
                            markdown: [
                                    content: """**仓库名称**: <font color="info">『${env.allData_repository_slug}』</font>
                            >事件类型：<font color="info">评论</font>
                            >pr 标题：<font color="comment">${env.allData_pullrequest_title}</font>
                            >pr from 分支：<font color="comment">${env.allData_pullrequest_fromRef_branch_name}</font>
                            >pr to 分支：<font color="comment">${env.allData_pullrequest_toRef_branch_name}</font>
                            >pr 创建者：<font color="comment">${env.allData_pullrequest_authorLogin}</font>
                            >comment 内容：<font color="comment">${env.allData_comment}</font>
                            >comment 提交者：<font color="comment">${env.allData_actor_displayName}</font>
                            >pr 地址：[${env.allData_pullrequest_link}](${env.allData_pullrequest_link})"""
                            ]
                    ]

                    def mentionPayload = [
                            msgtype : 'text',
                            text: [
                                    mentioned_mobile_list: ["${phoneNumbers.get(env.allData_actor_displayName)}","${phoneNumbers.get(env.allData_pullrequest_authorLogin)}"].unique()
                            ]
                    ]
//                    sendHttpRequest(url, payload)
//                    sendHttpRequest(url, mentionPayload)
                }
            }
        }



    }


}