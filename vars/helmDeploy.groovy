// vars/imgBuildNexusGeneric.groovy
def call(Closure body) {
  def label = "helm-${UUID.randomUUID().toString()}"
  def podYaml = libraryResource 'podtemplates/helm.yml'
  podTemplate(name: 'helm', label: label, yaml: podYaml) {
    node(label) {
      checkout scm
      gitShortCommit()
      container('helm') {
        body()
      }
    }
  }
}
