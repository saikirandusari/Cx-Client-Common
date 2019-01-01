pipeline {
  parameters {        
        booleanParam(name: 'IsReleaseBuild', description: 'Check the box if you want to create a release build') 
        string(name: 'BranchName', defaultValue: 'master', description: 'Branch used by the job')  
    }
  agent {
    node {
      label 'Plugins'
    }
    tools {
        jdk 'JDK_WINDOWS_1.8.0_92'
    }

  }
  stages {
    stage('Remove Snapshot') {
      steps {
        
        powershell '''#------------------------------------------------------------------------------------------------------------
# REMOVE THE WORD SNAPSHOT (ONLY FOR RELEASE BUILDS)
#------------------------------------------------------------------------------------------------------------

[string]$IsReleaseBuild = $ENV:IsReleaseBuild
[string]$RootPath = "C:\CI-Slave\workspace\$ENV:JOB_NAME"


If($IsReleaseBuild -eq "true")
{
    Write-Host " ----------------------------------------------------- "
    Write-Host "|  SNAPSHOT DISABLED: Removing Snapshot before build  |"
    Write-Host " ----------------------------------------------------- "

    $FilePath1 = $RootPath + "\gradle.properties"
    $FilePath2 = $RootPath + "\build.gradle"

    If(Test-Path "$FilePath1")
    {  
        $FileContent = Get-Content -Path $FilePath1
        Foreach($LineContent in $FileContent)
        {
            If($LineContent.Length -gt 9)
            {
                If($LineContent.Substring(0,9) -eq "version =")
                {
                    $NewLineContent = $LineContent.Replace("-SNAPSHOT", "")
                    (Get-Content $FilePath1) | ForEach-Object { $_ -replace "$LineContent", "$NewLineContent" } | Set-Content "$FilePath1"
                }
            }
        }
    }

    If(Test-Path "$FilePath2")
    {
        $FileContent = Get-Content -Path $FilePath2
        Foreach($LineContent in $FileContent)
        {
            If($LineContent.Length -gt 9)
            {
                If($LineContent.Substring(0,9) -eq "version =")
                {
                    $NewLineContent = $LineContent.Replace("-SNAPSHOT", "")
                    (Get-Content $FilePath2) | ForEach-Object { $_ -replace "$LineContent", "$NewLineContent" } | Set-Content "$FilePath2"
                }
            }
        }
    }
}
Else
{
    Write-Host " ----------------------------------------------------- "
    Write-Host "|    SNAPSHOT ENABLED: Run Build without modifying    |"
    Write-Host " ----------------------------------------------------- " 
}
'''
      }
    }
	
     stage('Build') {
            steps {
				//dir("cli") {
					bat "gradlew.bat -DIsReleaseBuild=${params.IsReleaseBuild} -DBranchName=master --stacktrace clean build && exit %%ERRORLEVEL%%"
				//}
            }
        }
		stage('Archive Artifacts') {
			steps {
				archiveArtifacts '/build/distributions/*.*'
			}
		}  
  }
}
