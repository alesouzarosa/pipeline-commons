!groovy

def call(body) {

    def pipelineParams= [
            objparameterizedCron: """*/2 * * * * %GREETING=Hola;PLANET=plutaoooo
                */4 * * * * %PLANET=Marterrr
                """
    ]

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()    

    pipeline {
        agent any

        parameters{
            string(name: 'PLANET', defaultValue: 'Earth', description: 'Which planet are we on?')
            string(name: 'GREETING', defaultValue: 'Hello', description: 'How shall we greet?')
        }
        triggers {
            parameterizedCron(pipelineParams.objparameterizedCron)
        }
        options {
            buildDiscarder(logRotator(numToKeepStr: '1'))
            disableConcurrentBuilds()
        }
        stages {

            stage('credentialIn') {
                steps{
                    sh "echo ${pipelineParams.credentialIdAws}"
                    sh " echo ${pipelineParams.objparameterizedCron}"
                    echo "${params.GREETING} ${params.PLANET}"
                    script { currentBuild.description = "${params.GREETING} ${params.PLANET}" }
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