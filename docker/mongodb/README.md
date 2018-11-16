
This repository contains Dockerfile for [MongoDB 4.0](https://www.mongodb.org)
container, based on the [Alpine edge](https://hub.docker.com/_/alpine/) image.

## Install

As a prerequisite, you need [Docker](https://docker.com) to be installed.


To re-build this image from the dockerfile:

	$ docker build -t todo-mongodb-img .

## Usage

To run `mongod`:

	$ docker run -d --name todo-mongodb -p 27017:27017 todo-mongodb-img

To run a shell session:

    $ docker exec -ti todo-mongodb sh

To use the mongo shell client:

	$ docker exec -ti todo-mongodb todo-mongodb

The mongo shell client can also be run its own container: 

	$ docker run -ti --rm --name mongoshell todo-mongodb host:port/db

## Limitations

- On MacOSX, volumes located in a virtualbox shared folder are not
  supported, due to a limitation of virtualbox (default docker-machine
  driver) not supporting fsync().
