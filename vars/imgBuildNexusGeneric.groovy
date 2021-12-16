// vars/imgBuildNexusGeneric.groovy
def call(String imageName, String repoOwner, String registry, String dockerFile="Dockerfile", String imageTag = env.BUILD_NUMBER, Closure body) {
  def label = "img-${UUID.randomUUID().toString()}"
  def podYaml = libraryResource 'podtemplates/imageBuildPushNexus.yml'
  podTemplate(name: 'img', label: label, yaml: podYaml) {
    node(label) {
      body()
      gitShortCommit()
      container('img') {
        sh """
          img build --build-arg buildNumber=${BUILD_NUMBER} --build-arg shortCommit=${env.SHORT_COMMIT} --build-arg commitAuthor="${env.COMMIT_AUTHOR}" -t ${registry}/${repoOwner}/${imageName}:${BUILD_NUMBER} ${pwd()}/${dockerFile}
          img push ${registry}/${repoOwner}/${imageName}:${BUILD_NUMBER}
        """
      }
    }
  }
}
