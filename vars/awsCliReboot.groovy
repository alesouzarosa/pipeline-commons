!groovy


def awsCliReboot() {
    sh 'aws --version'
    sh 'pwd'
    //sh 'aws ec2 reboot-instances --instance-ids ${instanceId}'
}
