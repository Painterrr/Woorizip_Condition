#!/bin/bash

git pull
echo $RANDOM > test.md
git add .
git commit -m "ci-test"
git pull
git push
