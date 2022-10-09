pipeline {
    agent any
    tools {
        maven 'M3_8_6'
    }
    environment {
        AWS_ACCESS_KEY_ID     = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY = credentials('AWS_SECRET_ACCESS_KEY')
        TF_IN_AUTOMATION      = '1'
    }
    stages {
        stage('terraform'){
            steps{
                sh 'terraform init'
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
