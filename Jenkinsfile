pipeline {
    agent any
    tools {
        maven 'M3_8_6'
        terraform 'Terraform'
    }
    parameters {
        booleanParam(name: 'destroy', defaultValue: false, description: 'Destroy Terraform build?')
    }
    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        TF_IN_AUTOMATION      = '1'
    }
    stages {
        stage('Apply') {
            when {
                not {
                    equals expected: true, actual: params.destroy
                }
            }
            steps {
                sh "terraform apply --auto-approve"
            }
        }
        stage('Destroy') {
            when {
                equals expected: true, actual: params.destroy
            }
            steps {
                sh "terraform destroy --auto-approve"
            }
        }
        stage('Kitchen') {
            when {
                anyOf {
                    changeset "*kitchen-service/**"
                    expression { currentBuild.previousBuild.result != "SUCCESS"}
                }
            }
            steps {
                dir("kitchen-service/"){
                    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'docker_hub', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD']]) {
                        sh 'docker login -u $USERNAME -p $PASSWORD'
                        sh "docker build -t ingjavierr/kitchen-service:latest ."
                        sh 'docker push ingjavierr/kitchen-service:latest'
                    }
                }
            }
        }
    }
}
