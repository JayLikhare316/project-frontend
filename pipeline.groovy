pipeline {
    agent any 
    stages {
        stage('code-pull') {
            steps {
                git branch: 'main', url: 'https://github.com/JayLikhare316/project-frontend.git'
            }
        }
        stage('code-build') {
            steps {
                sh '''
                sudo chown -R jenkins:jenkins /var/lib/jenkins/workspace/frontend-deployment
                npm install
                ng build --configuration production
                '''
            }
}
        stage('code-deploy') {
            steps {
                withCredentials([aws(accessKeyVariable: 'AWS_ACCESS_KEY_ID', credentialsId: 'aws-creds', secretKeyVariable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh '''
                        aws s3 cp --recursive dist/angular-frontend s3://jaybhaukifrontends3bucket/
                    '''
                }
            }
        }
    }
}
