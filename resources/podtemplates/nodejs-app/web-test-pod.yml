kind: Pod
metadata:
  name: nodejs-app
spec:
  containers:
  - name: nodejs
    image: node:10.10.0-alpine
    command:
    - cat
    tty: true
  - name: testcafe
    image: gcr.io/technologists/testcafe:0.0.2
    command:
    - cat
    tty: true
  securityContext:
    runAsUser: 1000
