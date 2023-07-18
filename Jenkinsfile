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
    environment {
        PROJECT_NAME = "jac"
        SERVICE_NAME = "back"
        BRANCH_NAME = "test"
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
                echo "location = /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}"
                echo "shell =  sh run.sh -n ${env.SERVICE_NAME}${env.BRANCH_NAME} -t ${env.PROJECT_NAME}"
                sshPublisher(publishers: [sshPublisherDesc(configName: '172.16.70.111', sshLabel: [label: 'origin/master'], transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}", remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'Dockerfile'), sshTransfer(cleanRemote: false, excludes: '', execCommand:
                        "cd /home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME} && sh run.sh -n ${env.SERVICE_NAME}-${env.BRANCH_NAME} -t ${env.PROJECT_NAME}", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: "/home/project/${env.PROJECT_NAME}/${env.SERVICE_NAME}", remoteDirectorySDF: false, removePrefix: 'target/', sourceFiles: 'target/*.jar')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: true)])

            }
        }


    }
}
