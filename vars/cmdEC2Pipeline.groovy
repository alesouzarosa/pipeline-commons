!groovy

def call(body) {

    def pipelineParams= [
            objparameterizedCron:
                """
                */2 * * * * %INSTANCE=instancia1;REGIAO=saopaulo
                */4 * * * * %INSTANCE=instancia2;REGIAO=saopaulo
                """,
            credentialIdAws: "Aasdadasd"
    ]

    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = pipelineParams
    body()    

    pipeline {
        agent any

        parameters{
            string(name: 'INSTANCE', defaultValue: 'defaut-instance', description: 'Which planet are we on?')
            string(name: 'REGIAO', defaultValue: 'defaut-regiao', description: 'How shall we greet?')
            
        }
        triggers {
            parameterizedCron(pipelineParams.objparameterizedCron)
        }
        options {
            buildDiscarder(logRotator(numToKeepStr: '1'))
            disableConcurrentBuilds()
        }
        stages {
            stage('reboot') {
                steps{
                    sh "echo ${pipelineParams.credentialIdAws}"                    
                    echo "${params.INSTANCE} ${params.REGIAO}"                    
                    script { currentBuild.description = "${params.INSTANCE} ${params.REGIAO}" }
 
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