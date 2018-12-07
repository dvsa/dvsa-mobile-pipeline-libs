package aws.dvsa.mobile

/**
 * Checkout code from the git repository
 *
 * @param repo_url - String
 * @param repo_branch - String
 * @param target_dir - String
 * @param creds - String
 */
void git_check_out(String repo_url, String repo_branch, String target_dir, String creds = null) {
    sh "mkdir -p ${target_dir}"
    checkout poll: false, scm: [
            $class: 'GitSCM',
            branches: [[
                               name: repo_branch
                       ]],
            doGenerateSubmoduleConfigurations: false,
            extensions: [[
                                 $class: 'CloneOption',
                                 depth: 0,
                                 noTags: true,
                                 reference: '',
                                 shallow: true
                         ],[
                                 $class: 'RelativeTargetDirectory',
                                 relativeTargetDir: target_dir
                         ]],
            submoduleCfg: [],
            userRemoteConfigs: [[
                                        credentialsId: creds,
                                        url: repo_url
                                ]]
    ]
}

/**
 * Get git commit hash
 *
 * @param dir_name - String
 * @return commit_hash
 */
String git_get_commit_hash(String dir_name) {
    dir(dir_name) {
        commit_hash = sh(
                script: 'git rev-parse HEAD',
                returnStdout: true
        ).trim()
    }
    return commit_hash
}

/**
 * Get the name of the git repo
 *
 * @param url - String
 * @return name
 */
String git_get_repo_name(String url) {
    Integer repo_name_index

    if (url.contains('gitlab')) {
        repo_name_index = 1
    } else if (url.contains('github') && url.contains('https')) {
        repo_name_index = 3
    } else if (url.contains('github')) {
        repo_name_index = 1
    }

    name = ((url.tokenize('/'))[repo_name_index].tokenize('.'))[0]
    return name
}