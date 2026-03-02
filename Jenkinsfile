pipeline {
  agent any

  environment {
    APP_NAME = "spring-ci-cd-demo"
    IMAGE    = "spring-ci-cd-demo:jenkins-${BUILD_NUMBER}"
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Unit Tests (CI)') {
      steps {
        sh '''
          chmod +x mvnw
          ./mvnw -q clean test
        '''
      }
    }

    stage('Package JAR') {
      steps {
        sh '''
          ./mvnw -q package -DskipTests
        '''
      }
    }

    stage('Build Docker Image') {
      steps {
        sh '''
          docker build -t "$IMAGE" .
        '''
      }
    }

    stage('Deploy (main only)') {
      when { branch 'main' }
      steps {
        sh '''
          docker rm -f "$APP_NAME" || true
          docker run -d -p 8086:8080 --name "$APP_NAME" "$IMAGE"
        '''
      }
    }

    stage('Smoke Test (main only)') {
      when { branch 'main' }
      steps {
        sh '''
          for i in $(seq 1 12); do
            echo "Smoke test attempt $i..."
            if docker exec "$APP_NAME" curl -fsS http://localhost:8080/health > /dev/null; then
              echo "Smoke test passed."
              exit 0
            fi
            sleep 5
          done
          echo "Smoke test failed. Logs:"
          docker logs "$APP_NAME" --tail 200
          exit 1
        '''
      }
    }
  }
}