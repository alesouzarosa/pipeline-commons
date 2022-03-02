!groovy

def call(body) {

    def pipelineParams= [
            
            schedule: "0 12,20 */1 * *",
            credentialIdAws: "AWS_JENKINS_CREDENTIALS",
            instanceId: "",
            awsRegion: "us-east-1"
    ]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    

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
                steps{
                    awsCliConfig(credentialIdAws, awsRegion)
                }
            }

            stage('reboot') {
                steps{
                    awsCliReboot(pipelineParams.instanceId)
                }
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