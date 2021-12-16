// vars/kanikoBuildPush.groovy
def call(String imageName, String imageTag = env.BUILD_NUMBER, String gcpProject = "core-flow-research", String dockerFile="Dockerfile", Closure body) {
  def dockerReg = "gcr.io/${gcpProject}"
  def label = "kaniko-${UUID.randomUUID().toString()}"
  def podYaml = libraryResource 'podtemplates/kaniko.yml'
  podTemplate(name: 'kaniko', label: label, yaml: podYaml) {
    node(label) {
      body()
      try {
        env.VERSION = readFile 'version.txt'
        env.VERSION = env.VERSION.trim()
        imageTag = env.VERSION
      } catch(e) {}
      imageName = imageName.toLowerCase()
      container(name: 'kaniko', shell: '/busybox/sh') {
        withEnv(['PATH+EXTRA=/busybox:/kaniko']) {
          sh """#!/busybox/sh
            /kaniko/executor -f ${pwd()}/${dockerFile} -c ${pwd()} --build-arg buildNumber=${BUILD_NUMBER} --build-arg shortCommit=${env.SHORT_COMMIT} --build-arg commitAuthor=${env.COMMIT_AUTHOR} -d ${dockerReg}/${imageName}:${imageTag}
          """
        }
      }
    }
  }
}