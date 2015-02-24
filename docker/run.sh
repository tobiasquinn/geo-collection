#!/bin/sh
mkdir -p ~/geo-collection/sqlite
docker run -d -v ~/geo-collection/sqlite:/home/app/geo-collection/db -p 3000:3000 geo-collection
