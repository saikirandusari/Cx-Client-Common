Skip to content
 
Search or jump to…

Pull requests
Issues
Marketplace
Explore
 @cxadmin Sign out
We are having a problem billing your account. Please enter a new payment method or check with your payment provider for details on why the transaction failed. You can downgrade to the Free plan in your Billing settings.
You can always contact support with any questions.
5
1 4 CxRepositories/Cx-Client-Common
 Code  Issues 0  Pull requests 0  Projects 0  Wiki  Insights  Settings
Cx-Client-Common/Jenkinsfile
78f3672  13 days ago
@cxadmin cxadmin Update Jenkinsfile
We found potential security vulnerabilities in your dependencies.
You can see this message because you have been granted access to vulnerability alerts for this repository. 
Manage your notification settings or learn more about vulnerability alerts.

     
57 lines (53 sloc)  1.75 KB
pipeline {
  parameters {        
        booleanParam(name: 'IsReleaseBuild', description: 'Check the box if you want to create a release build') 
    }
  agent {
    node {
      label 'Plugins'
    }
  }
  tools {
        jdk 'JDK_WINDOWS_1.8.0_92'
  }
	
  stages {
    stage('Remove Snapshot') {
      steps {
        
        powershell '''#------------------------------------------------------------------------------------------------------------
# REMOVE THE WORD SNAPSHOT (ONLY FOR RELEASE BUILDS)
#------------------------------------------------------------------------------------------------------------
[string]$IsReleaseBuild = $ENV:IsReleaseBuild
[string]$RootPath = "C:\\CI-Slave\\workspace\\$ENV:JOB_NAME"
If($IsReleaseBuild -eq "true")
{
    Write-Host " ----------------------------------------------------- "
    Write-Host "|  SNAPSHOT DISABLED: Removing Snapshot before build  |"
    Write-Host " ----------------------------------------------------- "
    $XmlPath = $RootPath + "\\pom.xml"
    If(Test-Path "$XmlPath")
    {  
        [xml]$XmlDocument = Get-Content -Path $XmlPath
        $XmlDocument.project.version = $XmlDocument.project.version.Replace("-SNAPSHOT", "")
        $XmlDocument.Save($XmlPath)
    }
}
Else
{
    Write-Host " ----------------------------------------------------- "
    Write-Host "|    SNAPSHOT ENABLED: Run Build without modifying    |"
    Write-Host " ----------------------------------------------------- " 
}'''
      }
    }
	
     stage('Build') {
            steps {
		    bat """mvn clean install -Dorg.apache.maven.user-settings=C:\\Jenkins\\workspace\\settings.xml -Dbuild.number=${BUILD_NUMBER}"""
            }
        }
		stage('Archive Artifacts') {
			steps {
				archiveArtifacts 'target/*.jar'
			}
		}  
  }
}
© 2019 GitHub, Inc.
Terms
Privacy
Security
Status
Help
Contact GitHub
Pricing
API
Training
Blog
About
Press h to open a hovercard with more details.
