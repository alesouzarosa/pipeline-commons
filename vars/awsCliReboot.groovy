!groovy


def awsCliReboot(instanceId) {
    sh 'aws ec2 reboot-instances --instance-ids ${instanceId}'
}
