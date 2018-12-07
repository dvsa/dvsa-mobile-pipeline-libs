package aws.dvsa.mobile

/**
 * Common logging function
 *
 * @param level - String
 * @param message - String
 */
void log(String level, String message) {
    String label
    String colour
    String reset = '\u001B[0m'

    switch (level) {
        case 'info':
            label  = 'INFO'
            colour = '\u001B[32m' // Green
            break
        case 'warn':
            label  = 'WARN'
            colour = '\u001B[33m' // Yellow
            break
        case 'error':
            label  = 'ERROR'
            colour = '\u001B[31m' // Red
            break
        default:
            label = 'DEBUG'
            break
    }
    println("${colour}[${label}] ${message}${reset}")
}

