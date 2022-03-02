!groovy

def call() {
    sh 'aws --version'    
    //sh 'aws ec2 reboot-instances --instance-ids ${instanceId}'
}
