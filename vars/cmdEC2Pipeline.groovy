!groovy

def call(body) {

    def pipelineParams= [
            objeto: """
            */2 * * * * %instanceId=Hola;awsRegion=us-east-1;comando=reboot
            */2 * * * * %instanceId=Hola;awsRegion=us-east-1;comando=reboot
            """,         
            credentialIdAws: "AWS_JENKINS_CREDENTIALS"

    ]

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()    

    pipeline {
        agent any
        triggers {
            parameterizedCron("""
            */2 * * * * %instanceId=Hola;awsRegion=us-east-1;comando=reboot
            */2 * * * * %instanceId=Hola;awsRegion=us-east-1;comando=reboot
            """)
        }
        options {
            buildDiscarder(logRotator(numToKeepStr: '1'))
            disableConcurrentBuilds()
        }
        stages {

            stage('credentialIn') {
                steps{
                    awsCliConfig(pipelineParams.credentialIdAws, pipelineParams.awsRegion)
                }
            }

            stage('reboot') {
                steps{
                    awsCliReboot()
                }
            }
        }
        /*
        post {
            always {
                script {
                    slackNotifier(currentBuild.currentResult)
                }
            }
        }
        */
    }
}