@Library('github.com/releaseworks/jenkinslib') _
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
        }*/
        stage('Logging into AWS ECR') {
            steps {
                withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'aws-key', usernameVariable: 'AWS_ACCESS_KEY_ID', passwordVariable: 'AWS_SECRET_ACCESS_KEY']]) {
                    AWS("--region=us-east-1 s3 ls")
                }

                /*script {
                    //sh "aws ecr get-login-password --region ${AWS_DEFAULT_REGION} | docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
                    sh "aws ecr get-login-password --region us-east-1"
                    sh "'docker login --username AWS -p password 262583979852.dkr.ecr.us-east-1.amazonaws.com'"
                }*/
                 
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
