!groovy

def call(body) {

    def pipelineParams= [

            objeto: 
            
            schedule: "0 12,20 */1 * *",
            credentialIdAws: "AWS_JENKINS_CREDENTIALS",
            instanceId: "instanceid",
            awsRegion: "us-east-1"
    ]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()

    

    pipeline {
        agent any
        triggers {
            parameterizedCron(pipelineParams.object}
 
        options {
            buildDiscarder(logRotator(numToKeepStr: '1'))
            disableConcurrentBuilds()
        }
        stages {

            stage("select a job"){
                //deve ler a tabela de jobs e retorna o trablaho que deve ser feito


            }

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