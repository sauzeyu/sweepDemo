properties([
        pipelineTriggers([
                [
                        $class  : 'BitBucketPPRTrigger',
                        triggers: [
                                [
                                        $class      : 'BitBucketPPRPullRequestTriggerFilter',
                                        actionFilter: [
                                                $class: 'BitBucketPPRPullRequestCreatedActionFilter',
                                        ]
                                ],
                                [
                                        $class      : 'BitBucketPPRPullRequestTriggerFilter',
                                        actionFilter: [
                                                $class: 'BitBucketPPRPullRequestApprovedActionFilter',
                                        ]
                                ],
                                [
                                        $class      : 'BitBucketPPRPullRequestTriggerFilter',
                                        actionFilter: [
                                                $class: 'BitBucketPPRPullRequestUpdatedActionFilter',
                                        ]
                                ],
                                [
                                        $class      : 'BitBucketPPRPullRequestTriggerFilter',
                                        actionFilter: [
                                                $class: 'BitBucketPPRPullRequestMergedActionFilter',
                                        ]
                                ],
                                [
                                        $class      : 'BitBucketPPRRepositoryTriggerFilter',
                                        actionFilter: [
                                                $class                     : 'BitBucketPPRRepositoryPushActionFilter',
                                                triggerAlsoIfNothingChanged: true,
                                                triggerAlsoIfTagPush       : false,
                                                allowedBranches            : "",
                                                isToApprove                : true
                                        ]
                                ]
                        ]
                ]
        ])
])


pipeline {
    //声明在jenkins任何节点都可用
    agent any
    //声明一些环境变量
    environment {
        //获取环境变量，拿到项目名称和分支名称
        def projectName = "jac"
        def servicename = "back"
        def branchName = "test"
        // 将定义的变量保存到env中，使其可以在stages中使用
        env.PROJECT_NAME = projectName
        env.SERVICE_NAME = servicename
        env.BRANCH_NAME = branchName
    }

    stages {
        stage('Checkout') {
            steps {
                // 使用 echo 函数打印输出
                echo 'Checkout'
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: '217dfbc7-70b9-485b-8cfc-515b9ad785cc', url: 'http://172.16.70.112:7990/scm/back/dkserver-back-jac.git']]])
            }
        }

        stage('Build') {
            steps {
                // 使用 echo 函数打印输出
                echo 'Build'
                echo '/home/project/${PROJECT_NAME}/${SERVICE_NAME}'
                echo 'sh run.sh -n ${SERVICE_NAME}-dev  -t ${PROJECT_NAME}'
                echo '/docker/${SERVICE_NAME}-${PROJECT_NAME}-test'
                sshPublisher(publishers: [sshPublisherDesc(configName: '172.16.70.111', sshLabel: [label: 'origin/master'], transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: 'cd /home/project/${PROJECT_NAME}/${SERVICE_NAME}', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '/home/project/${PROJECT_NAME}/${SERVICE_NAME}', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'Dockerfile'), sshTransfer(cleanRemote: false, excludes: '', execCommand: '''source /etc/profile
cd /home/project/${PROJECT_NAME}/${SERVICE_NAME}
sh run.sh -n ${SERVICE_NAME}${BRANCH_NAME} -t ${PROJECT_NAME}''', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '/home/project/${PROJECT_NAME}/${SERVICE_NAME}', remoteDirectorySDF: false, removePrefix: 'target/', sourceFiles: 'target/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])

            }
        }


    }
}
