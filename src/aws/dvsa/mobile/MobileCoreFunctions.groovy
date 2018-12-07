package aws.dvsa.mobile

import aws.dvsa.mobile.*

/**
 * The following method installs NPM packages it takes a variable installedPackage if this variable is not provided
 * a generic NPM install is run otherwise it will install a specific package.
 */
void install() {
    try {
        sh """
           npm install
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'UNABLE TO INSTALL NPM DEPENDENCIES: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method runs the npm build for the project
 */
void npmBuild() {
    try {
        sh """
           npm run build
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'UNABLE TO RUN NPM BUILD: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method runs the sonar scanner on the code
 */
void sonar() {
    try {
        sh """
           npm run sonar-scanner
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'RUNNING SONAR SCANNER: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method runs tests against the code. It accepts a String to be provided this variable is used to determine
 * which type of tests are run
 *
 * @param type - String
 * @param ui_automation_artifact - String
 * @param file - String
 * @param project_name - String
 */
void testing(String type, String ui_automation_artifact, String file, String project_name) {
    try {
        switch (type) {
            case 'unit':
                sh """
                   npm run test-ci-headless 
                   """
                break
            default:
                CommonFunctions.log('warm', 'INCORRECT PARAMETER PROVIDED THE TESTS WILL NOT RUN')
        }
    } catch (Exception exception) {
        CommonFunctions.log('error', 'RUNNING ' + type.toUpperCase() + 'TESTS: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method runs the security checks on the code
 */
void security() {
    try {
        sh """
           export PATH=/sbin:/usr/sbin:/bin:/usr/bin:/opt/scanrepo/
           git log -p | scanrepo
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'RUNNING SECURITY CHECKS: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * This method adds any extra cordova plugins to the project
 *
 * @param plugin_name - String
 */
void configurePlugin(String plugin_name) {
    try {
        sh '''
           export PATH="/usr/local/opt/node@8/bin:$PATH"
           export PATH="/usr/local/bin:/usr/local/sbin:~/bin:$PATH" 
           '''
        sh """
           cordova plugin add ${plugin_name}
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'CONFIGURING PLUGIN: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method configures all the dependencies for building the IPA file.
 */
void configure() {
    try {
        sh '''
            export PATH="/usr/local/opt/node@8/bin:$PATH"
            export PATH="/usr/local/bin:/usr/local/sbin:~/bin:$PATH"
            cordova platform add ios --save
           '''
    } catch (Exception exception) {
        CommonFunctions.log('error', 'CONFIGURING CORDOVA PLATFORM: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method uploads the IPA file to S3. It expects two Strings to be provided file and bucket
 *
 * @param bucket - String
 * @param file - String
 */
void upload(String bucket, String file) {
    try {
        sh """
           aws s3 cp ./build/${file}.ipa s3://${bucket}
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'UPLOADING FILE TO S3: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * The following method triggers fastlane to run
 */
void build() {
    try {
        sh '''
           sudo su admin -c "JENKINS_HOME=/Users/Shared/Jenkins/Home/Library/Application\\ Support/Jenkins/jenkins-runner.sh"
           security unlock-keychain /Users/admin/Library/Keychains/login.keychain
           
           export PATH="$HOME/.fastlane/bin:$PATH"
           fastlane build
           '''
    } catch (Exception exception) {
        CommonFunctions.log('error', 'BUILDING IPA FILE: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}

/**
 * This method creates a presign s3 url which can be sent to any party which needs to access the IPA file in this bucket
 *
 * @param bucket - String
 * @param file - String
 */
void publish(String bucket, String file) {
    try {
        sh """
           aws s3 presign s3://${bucket}/${file}.ipa --expires-in 3600
           """
    } catch (Exception exception) {
        CommonFunctions.log('error', 'CREATING S3 URL: ' + exception)
        currentBuild.result = 'FAILURE'
    }
}