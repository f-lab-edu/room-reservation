pipeline {
    agent any

    environment {
        IMAGE_NAME = "dockerhub-ownert/yeogi-app"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds')
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/f-lab-edu/room-reservation', branch: 'feature-cicd'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew :yeogi-customer:clean :yeogi-customer:bootJar -x test'
            }
        }

        stage('Docker Build & Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker build -t $IMAGE_NAME:latest .
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push $IMAGE_NAME:latest
                    """
                }
            }
        }

        stage('Deploy to VM') {
            steps {
                sshagent(['vm-ssh-key']) {
                    sh '''
                    ssh ubuntu@<YOUR_VM_IP> "
                        cd /home/ubuntu/yeogi &&
                        docker compose pull &&
                        docker compose down &&
                        docker compose up -d
                    "
                    '''
                }
            }
        }
    }
}
