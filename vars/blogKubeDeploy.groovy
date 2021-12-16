// vars/kubeDeploy.groovy
def call(imageName, gcpProject, githubCredentialId, repoOwner) {
    def label = "kubectl"
    def podYaml = libraryResource 'podtemplates/kubeDeploy.yml'
    def deployYaml = libraryResource 'k8s/basicDeploy.yml'
    def envStagingRepo = "environment_staging"
    
    podTemplate(name: 'kubectl', label: label, yaml: podYaml) {
      node(label) {
        imageNameTag()
        def repoName = env.IMAGE_REPO.toLowerCase()
        //create environment repo for prod if it doesn't already exist
        echo githubCredentialId
        withCredentials([usernamePassword(credentialsId: githubCredentialId, usernameVariable: 'USERNAME', passwordVariable: 'ACCESS_TOKEN')]) {
          
          // try {
          //   sh 'git pull origin master'
          // } catch(e) {
          //   //nothing to do, just means remote hasn't been initialized yet
          // }
          checkout scm
          sh("sed -i.bak 's#REPLACE_IMAGE#gcr.io/${gcpProject}/blog-vuejs:${repoName}-${BUILD_NUMBER}#' .kubernetes/frontend.yaml")
          sh("sed -i.bak 's#REPLACE_HOSTNAME#preview.workshop.cb-sa.io#' .kubernetes/frontend.yaml")
          sh("sed -i.bak 's#REPLACE_PATH#/${repoOwner}#' .kubernetes/frontend.yaml")
        }
        container("kubectl") {
          sh "cat .kubernetes/frontend.yaml"
          sh "kubectl apply -f .kubernetes/frontend.yaml"
          sh "echo 'deployed to http://preview.workshop.cb-sa.io/${repoName}/'"
        }
      }
    }
}
