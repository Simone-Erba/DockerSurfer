# DockerSurferWithREST

## Overview

Docker Surfer is a web service that you can find [here](http://ec2-18-221-47-183.us-east-2.compute.amazonaws.com). It offers a graphical interface to browse and analyze Docker dependencies between images. It also provides information about images popularity and stability. Dependencies are created by looking at the layers. 

## How to use

With DockerSurfer you can manage images' popularity and stability, so you can be aware of unexpected chenges in them. If for example an image lose popularity, this can mean that a new bug was discovered or that a better image was released. You can also see images stability, if the stability decrease (betweeness centrality value increase) the image has now more chances to change its layers unexpectly. If you don't know how to use an image, you can see how other users are using it. 

## Future Devolopments

The code lack in documentation.
The service doesn't show dependencies for very popular images, because the database take too long to answer.
The database schema is essential and it can be improved for better performance.
An ftp server to download the database will be avaiable soon. The database schema will be provided soon.

## Contribute

If you want to contibute, e-mail me at simone.erba.95@gmail.com to get more information about the project
