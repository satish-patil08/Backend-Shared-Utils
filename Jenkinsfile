pipeline {
  agent {
    node {
      label 'dev-server'
    }
  }
  stages {
    stage('Generate Jar Files') {
      steps {
        sh '''mvn clean install'''
      }
    }
  }
}
