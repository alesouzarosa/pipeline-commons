!groovy

def call(body) {

    def pipelineParams= [
            schedule: "0 0 0 0 0"   /*cron format */,
            credentialIdAws: "AWS_JENKINS_CREDENTIALS",
            instanceId: "",
            awsRegion: "us-east-1"
    ]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    def patternMatch = BRANCH_NAME ==~ pipelineParams.patternRule

    pipeline {
        agent any
        triggers {
            pollSCM('${pipelineParams.schedule}')
        }
        tools {
            jdk pipelineParams.jdkVersion
        }
        options {
            buildDiscarder(logRotator(numToKeepStr: '1'))
            disableConcurrentBuilds()
        }
        stages {
            stage('credentialIn') {
                awsCliConfig(credentialIdAws, awsRegion)
            }
            stage('reboot') {
                awsCliReboot(pipelineParams.instanceId)
            }
        }
        /*post {
            always {
                script {
                    slackNotifier(currentBuild.currentResult)
                }
            }
        }*/
    }
}