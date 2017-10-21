# DockerSurfer

## Overview

Docker Surfer is a web service that you can find [here](http://dockersurfer.com). It offers a graphical interface to browse and analyze Docker dependencies between images. It also provides information about images popularity and stability. Dependencies are created by looking at the layers. For more details read my [thesis](https://github.com/Simone-Erba/DockerSurfer/blob/master/DockerSurferThesis.pdf).

## PageRank and Central betweeness

Page Rank index can be seen as the influence of the image. It is calculated by adding the Page Rank value of all the images that use that image (children images). Changes in the layers of an image with a high page Rank value can affect a lot of images.

Betweeness centrality index is the number of how many images can affect the image. A high value means that the image can have unexpected changes in its layers. 

## How to use

The user interface is structured as a REST Service, to guarantee an easy to browse and share interface. The REST interface is helpful
for sharing links or for performing searches directly in the URL address.
With DockerSurfer you can manage images popularity and stability, so you can be aware of unexpected changes in them. If for example an image loses popularity, this can mean that a new bug was discovered or that a better image was released. You can also see the images stability. If the stability decreases (betweeness centrality value increase) the image now has more chances to change its layers unexpectedly. If you don't know how to use an image or if the documentation is poor, you can see how other users are using it. If you are writing a new image, DockerSurfer can helps you with the choice of the base image.


## Interface


**/rest/res/\<user>** will show the user images. In the picture are showed some official images


![user page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/user.png)


**/rest/res/\<user>/\<repository>** will show the tags for the repository with their popularity (pageRank value)


![repository page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/repo.png)


**/rest/res/\<user>/\<repository>/\<tag>** show which image it's used by the image (if any) and which images use the image (if any). Also provide information such as indexes and the last update of the data. You can find links to the image's DockerHub and ImageLayers pages.


![tag page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/tag.png)


**/rest/popular** In this page you can see the top 100 most popular images ordered by Page Rank value. Using one of these images as base of your image can be a good idea, because they are trusted by other users. This page can also be accessed by clicking the "popular images" button
in the homepage.

A functionality that for now it's pretty useless is the graphical view for the dependencies, but I think that it can have some potential. The service has also another REST path that is **/rest/json/\<user>/\<repository>/\<tag>** that provides a json representation of the dependencies in a structure that it's already ready to be passed to the Cytoscape library to be drawn. 


![graph page](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/cyto.png)

## Use cases

### Understand how to use an image

There is an image that looks interesting but you don't really know what it does or how to use it. Search the image in the home page and then open some images that use the image. You can see their DockerFiles and descriptions by clicking on the links to DockerHub and ImageLayers.

### Choose a base image for your image

You want to create a new image but you are not sure about the image to use as base of your project. Should you use an official image? Or an image created by another user? Find some candidates on the DockerHub and check their DockerSurfer pages. Use the information that Docker Hub provide, but also consider stability and popularity not only for the candidate image, but also of the images that the candidate image it's using. You can find them just navigating the dependencies by using the "derived By" section. (see the tag page above).


## Data Analysis

![stability](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/stability.png)

Most of the images have betweeness centrality (stability) value between 1 and 3. Am image with stability 0 doesn't use other images, but it's probably a general-purpose image. Be careful when you choose an image with stability greater than 3. It can have an high probability to change its layers.

![page rank](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/pageRank.png)

90% of the images have a Page Rank value of 0. They are images not used by anyone. The below graph refers to the 10% of the images with at least one image that us them. We can see that the popular images are few. 

![top100](https://github.com/Simone-Erba/DockerSurfer/blob/master/images/top100.png)

In the below figure you can see that the top 100 most popular images can influence half of the images in the DockerHub.

## Future Developments

Show changes in images popularity and stability.
The code lack in documentation.
The service doesn't show dependencies for very popular images, because the database take too long to answer.
The database schema is essential and it can be improved for better performance.
An ftp server to download the database will be available soon. The database schema will be provided soon.

## Contribute

If you want to contribute, e-mail me at simone.erba.95@gmail.com to get more information about the project
