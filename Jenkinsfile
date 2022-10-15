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
        registry_payment = '262583979852.dkr.ecr.us-east-1.amazonaws.com/payment-service:v2'
        registry_order = '262583979852.dkr.ecr.us-east-1.amazonaws.com/order-service:v1'
    }
    stages {
        /*stage('Create Infra') {
            when {
                not {
                    equals expected: true, actual: params.destroy
                }
            }
            steps {
                sh "terraform apply --auto-approve"
            }
        }*/
        stage('Destroy Infra') {
            when {
                equals expected: true, actual: params.destroy
            }
            steps {
                dir("infra/"){
                    sh "terraform destroy --auto-approve"
                }
            }
        }
        stage("Docker Build") {
            steps {
                dir("payment-service/"){
                    sh "docker build --cache-from payment-service:latest -t payment-service:latest ."
                }
                dir("order-service/"){
                    sh "docker build --cache-from order-service:latest -t order-service:latest ."
                }
            }
        }
        stage('Logging into AWS ECR') {
            steps {
                withAWS(credentials: 'ecr-credentials', region: 'us-east-1') {
                        script {
                            def login = ecrLogin()
                            sh "${login}"
                            sh '''docker tag payment-service:latest 262583979852.dkr.ecr.us-east-1.amazonaws.com/payment-service:v1'''
                            sh '''docker tag order-service:latest 262583979852.dkr.ecr.us-east-1.amazonaws.com/order-service:v1'''
                        }
                }
            }
        }
        stage("Docker Push") {
            steps {
                sh "docker push ${registry_payment}"
                sh "docker push ${registry_order}"
            }
        }
        stage('Kubectl') {
            steps {
                withAWS(credentials: 'ecr-credentials', region: 'us-east-1') {
                    sh 'aws eks --region us-east-1 update-kubeconfig --name eks-cluster-test'
                    sh 'kubectl get pods'
                }
            }
        }
        /*stage("Kubectl") {
            steps {
                withKubeConfig([credentialsId: 'aws-key', clusterName: 'cluster-name']) {
                    //dir("kitchen-service/"){
                        script {
                            sh "kubectl get pods"
                        }
                    //}
                }
            }
        }*/
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
