!groovy

def call(credentialIdAws, awsRegion){
    withCredentials([usernamePassword(
            credentialsId: credentialIdAws,
            usernameVariable:'accessKey',
            passwordVariable:'secretAccessKey' )]) {
        sh 'aws configure set aws_access_key_id ${accessKey}'
        sh 'aws configure set aws_secret_access_key ${secretAccessKey}'
    }
    sh 'aws configure set default.region ${awsRegion}'
}
