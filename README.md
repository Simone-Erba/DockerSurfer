# DockerSurfer

## Overview

Docker Surfer is a web service that you can find [here](http://dockersurfer.com). It offers a graphical interface to browse and analyze Docker dependencies between images. It also provides information about images popularity and stability. Dependencies are created by looking at the layers. For more details read my [thesis](https://github.com/Simone-Erba/DockerSurfer/blob/master/DockerSurferThesis.pdf).

## PageRank and Central betweeness

Page Rank index can be seen as the influence of the image. It is calculated by adding the Page Rank value of all the image that use that image (children images). Changes in the layers of an image with a high page Rank value can affect a lot of images.

Betweeness centrality index is the number of how many images can affect the image. An high value mean that the image can have unexpected changes in its layers. 

## How to use

The user interface is structured as a REST Service, to guarantee an easy to browse and share interface. The REST interface is helpful
for sharing links or for perform searches directly in the URL address.
With DockerSurfer you can manage images' popularity and stability, so you can be aware of unexpected chenges in them. If for example an image lose popularity, this can mean that a new bug was discovered or that a better image was released. You can also see images stability, if the stability decrease (betweeness centrality value increase) the image has now more chances to change its layers unexpectly. If you don't know how to use an image or if the documentation is poor, you can see how other users are using it. if you are writing a new image, Dockersurfer can helps you with the choice of the base image. 


## Interface


**/rest/res/\<user>** will show the user images. In the picture are showed some official images


![user page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/user.png)


**/rest/res/\<user>/\<repository>** will show the tags for the repository with their popularity (pageRank value)


![repository page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/repo.png)


**/rest/res/\<user>/\<repository>/\<tag>** show wich image it's used by the image (if any) and which images use the image (if any). 


![tag page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/tag.png)


the graphical view for the dependencies


![graph page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/cyto.png)


## Future Devolopments

The code lack in documentation.
The service doesn't show dependencies for very popular images, because the database take too long to answer.
The database schema is essential and it can be improved for better performance.
An ftp server to download the database will be avaiable soon. The database schema will be provided soon.

## Contribute

If you want to contibute, e-mail me at simone.erba.95@gmail.com to get more information about the project
