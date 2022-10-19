pipeline {
    agent any
    tools {
        maven 'M3_8_6'
        terraform 'Terraform'
    }
    parameters {
        booleanParam(name: 'destroy', defaultValue: false, description: 'Destroy Terraform build?')
        booleanParam(name: 'deploy', defaultValue: false, description: 'Deploy Terraform apply?')
        booleanParam(name: 'database', defaultValue: false, description: 'Create tables?')
        booleanParam(name: 'microservices', defaultValue: false, description: 'Create microservices?')
    }
    environment {
        AWS_ACCESS_KEY_ID       = credentials('AWS_ACCESS_KEY_ID')
        AWS_SECRET_ACCESS_KEY   = credentials('AWS_SECRET_ACCESS_KEY')
        AWS_ACCOUNT_ID          = "262583979852"
        AWS_DEFAULT_REGION      = "us-east-1" 
        TF_VAR_environment      = 'jimena'
        registry_payment        = '262583979852.dkr.ecr.us-east-1.amazonaws.com/payment-service:v5'
        registry_order          = '262583979852.dkr.ecr.us-east-1.amazonaws.com/order-service:v5'
        registry_kitchen        = '262583979852.dkr.ecr.us-east-1.amazonaws.com/kitchen-service:v5'
    }
    stages {
        stage('Create Infra') {
            when {
                equals expected: true, actual: params.deploy
            }
            steps {
                dir("infra/"){
                    sh "terraform init"
                    sh "terraform apply --auto-approve"
                }
            }
        }
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
        stage('Database') {
            when {
                equals expected: true, actual: params.database
            }
            steps {
                dir("liquibase/"){
                    sh '/opt/liquibase/liquibase --changeLogFile="changesets/db.changelog-master.xml" update'
                }
            }
        }
        stage("Docker Build") {
            when {
                equals expected: true, actual: params.microservices
            }
            steps {
                dir("payment-service/"){
                    sh "docker build --cache-from payment-service:latest -t payment-service:latest ."
                    //sh "docker pull payment-service:latest"
                    //docker hub publico
                }
                dir("order-service/"){
                    sh "docker build --cache-from order-service:latest -t order-service:latest ."
                }
                dir("kitchen-service/"){
                    sh "docker build --cache-from kitchen-service:latest -t kitchen-service:latest ."
                }
            }
        }
        stage('Logging into AWS ECR') {
            when {
                equals expected: true, actual: params.microservices
            }
            steps {
                withAWS(credentials: 'ecr-credentials', region: 'us-east-1') {
                        script {
                            def login = ecrLogin()
                            sh "${login}"
                            sh '''docker tag payment-service:latest ${registry_payment}'''
                            sh '''docker tag kitchen-service:latest ${registry_kitchen}"'''
                            sh '''docker tag order-service:latest 262583979852.dkr.ecr.us-east-1.amazonaws.com/order-service'''
                        }
                }
            }
        }
        stage("Docker Push") {
            when {
                equals expected: true, actual: params.microservices
            }
            steps {
                sh "docker push ${registry_payment}"
                sh "docker push ${registry_kitchen}"
                sh "docker push 262583979852.dkr.ecr.us-east-1.amazonaws.com/order-service"
            }
        }
        stage('Kubectl') {
            when {
                equals expected: true, actual: params.microservices
            }
            steps {
                withAWS(credentials: 'ecr-credentials', region: 'us-east-1') {
                    sh 'aws eks --region us-east-1 update-kubeconfig --name eks-cluster-jimena'
                    sh 'kubectl get pods'
                    dir("Deployment/"){
                        sh 'kubectl apply -f Payment-deployment.yaml'
                        sh 'kubectl apply -f Kitchen-deployment.yaml'
                        sh 'kubectl apply -f Order-deployment.yaml'
                        //sh 'kubectl apply -f secrets.yaml'
                        sh 'kubectl apply -f ingress-deploy.yaml'
                        sh 'kubectl apply -f nginx_ingress_services.yaml'
                    }
                }
            }
        }
    }
}
