def IMAGENAMECMD = "hms-pmcmd"
def IMAGENAMEQRY = "hms-pmqry"
def HELM_FOLDER_NAME = "hms-pm"
def IMAGETAG = 'latest'
def gitCommit() {
    return sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
}
def getLatestTag(){
    return sh(returnStdout: true, script: 'git describe --tags --always').trim()
}
pipeline {
    agent any
    stages {
        stage ('Clone') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: 'main']], extensions: [[$class: 'WipeWorkspace']], userRemoteConfigs: [[credentialsId: 'bff11b86-4d33-4fb2-b02c-38e042b1b0b7', refspec: '+refs/tags/*:refs/remotes/origin/tag/*', url: 'https://github.com/leevydanomalik/hms-pm.git']]])
                script{
                    IMAGETAG = getLatestTag()
                }
            }
        }
        
        stage ('build maven') {
            steps {
                configFileProvider([configFile(fileId: '35487557-94df-4be8-b04e-feaac076ccfe', variable: 'MAVEN_SETTINGS')]){
                    sh "git checkout -b deploy/${IMAGETAG} ${IMAGETAG}"
                    sh "mvn -s $MAVEN_SETTINGS clean install -U -DskipTests"
                }
            }
        }
        
        stage('Docker Build - Command'){
            steps {
                echo 'Building Command..'
                sh "cd hms.pm.command.svc && mvn package docker:build -DskipTests"
                sh "docker tag leevy/${IMAGENAMECMD}:latest leevy/${IMAGENAMECMD}:dev-${IMAGETAG}"
            }
        }
        
        stage('Docker Build - Query'){
            steps {
                echo 'Building Query..'
                sh "cd hms.pm.query.svc && mvn package docker:build -DskipTests"
                sh "docker tag leevy/${IMAGENAMEQRY}:latest leevy/${IMAGENAMEQRY}:dev-${IMAGETAG}"
            }
        }
        stage('Push docker image-cmd'){
            steps {
                withDockerRegistry([ credentialsId: "ae1b0e71-0d8a-4bad-9093-7ffcb42595ab", url: "https://index.docker.io/v1/" ]){
                    echo 'Push Docker image-cmd..'
                    sh "docker push leevy/${IMAGENAMECMD}:dev-${IMAGETAG}"   
                }
            }
        }
        
                stage('Push docker image-qry'){
            steps {
                withDockerRegistry([ credentialsId: "ae1b0e71-0d8a-4bad-9093-7ffcb42595ab", url: "https://index.docker.io/v1/" ]){
                    echo 'Push Docker image-qry..'
                    sh "docker push leevy/${IMAGENAMEQRY}:dev-${IMAGETAG}"   
                }
            }
        }
    }
}
