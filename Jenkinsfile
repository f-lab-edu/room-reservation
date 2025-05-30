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
                sh """
                    docker build -t $IMAGE_NAME:latest .
                    echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin
                    docker push $IMAGE_NAME:latest
                """
            }
        }

        stage('Deploy to VM') {
            steps {
                sshagent(['vm-ssh-key']) {
                    sh '''
                    ssh ubuntu@127.0.0.1 "
                      docker pull $IMAGE_NAME:latest &&
                      docker-compose -f /home/ubuntu/yeogi/docker-compose.yml up -d
                    "
                    '''
                }
            }
        }
    }
}
