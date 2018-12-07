# DVSA Mobile Pipeline Library

The following repo contains the shared libraries used for the mobile pipeline. The pipeline will expect this pipeline to
configured in Jenkins before it can run.

For the main pipeline please go [here](https://github.com/dvsa/dvsa-mobile-pipeline)

## Getting Started

To use this shared library you need to configure your Jenkins box:

1) Navigate to **Manage Jenkins**
2) Select **Configure**
3) Go down to the section **Global Pipeline Libraries**
4) Set the name to **MobilePipelineSharedLibrary**
5) Set the Default version to **master**
6) Under **Source Code Management** set the project repository to this one 

### Prerequisites

To use this shared library a Jenkins pipeline needs to be configured and this code needs to be referenced. For any extra
information about what environment variables are expected please visit the main repo

## Contributing

To contribute please create a pull request

## Authors

* **Alex Le Peltier** - *Initial work*

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details
