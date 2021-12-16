def call(Map config) {        
  withCredentials([usernamePassword(credentialsId: "${config.credId}", usernameVariable: 'USERNAME', passwordVariable: 'TOKEN')]) {
    sh """
      curl -s -H "Authorization: token ${TOKEN}" \
        -X POST -d '{"body": "${config.message}"}' \
        "https://api.github.com/repos/${config.repoOwner}/${config.repo}/issues/${config.issueId}/comments"
    """
  }
}        
