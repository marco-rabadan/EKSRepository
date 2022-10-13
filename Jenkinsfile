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
        AWS_ACCESS_KEY_ID       = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY   = credentials('AWS_SECRET_ACCESS_KEY')
        TF_IN_AUTOMATION        = '1'
        AWS_ACCOUNT_ID          = "262583979852"
        AWS_DEFAULT_REGION      = "us-east-1" 
        IMAGE_REPO_NAME         = "ECR_REPO_NAME"
        IMAGE_TAG               = "IMAGE_TAG"
        REPOSITORY_URI          = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/${IMAGE_REPO_NAME}"
        registry = '262583979852.dkr.ecr.us-east-1.amazonaws.com/kitchen-service:v1'
    }
    stages {
        /*stage('Apply') {
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
        stage("Docker Build") {
            steps {
                dir("kitchen-service/"){
                    sh "docker build -t kitchen-service:latest ."
                }
            }
        }
        stage('Logging into AWS ECR') {
            steps {
                withAWS(credentials: 'ecr-credentials', region: 'us-east-1') {
                    //dir("kitchen-service/"){
                        script {
                            def login = ecrLogin()
                            sh "${login}"
                            sh '''docker tag kitchen-service:latest 262583979852.dkr.ecr.us-east-1.amazonaws.com/kitchen-service:v1'''
                        }
                    //}
                }
            }
        }
        stage("Docker Push") {
            steps {
                sh "docker push ${registry}"
            }
        }*/
        stage("Kubectl") {
            steps {
                withKubeConfig([credentialsId: 'aws-key']) {
                    //dir("kitchen-service/"){
                        script {
                            sh "kubectl --help"
                        }
                    //}
                }
            }
        }
        /*stage('Kitchen') {
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
        }*/
    }
}
