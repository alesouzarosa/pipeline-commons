!groovy

def call(body) {

    def pipelineParams= [


             //String """___"""  



            credentialIdAws: "AWS_JENKINS_CREDENTIALS"


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
            parameterizedCron(
                """
                */2 * * * * %GREETING=Hola;PLANET=Pluto
                */4 * * * * %PLANET=Mar
                """  )
        }


        // parameters{[
        //     string(name: 'instaceId', defaultValue: 'i-123456789', description: 'Which planet are we on?'),
        //     string(name: 'awsRegion', defaultValue: 'us-east-1', description: 'Which planet are we on?'),
        //     string(name: 'command', defaultValue: 'Hello', description: 'How shall we greet?')   
        // ]}
        // triggers {
        //     parameterizedCron("""
        //         */2 * * * * %GREETING=Hola;PLANET=Pluto
        //         */3 * * * * %PLANET=Mar
        //     """)
        // }


        options {
            buildDiscarder(logRotator(numToKeepStr: '1'))
            disableConcurrentBuilds()
        }
        stages {

            stage('credentialIn') {
                steps{
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