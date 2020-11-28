#!/usr/bin/env bash
set -eo pipefail

function main {

  # Set Git credentials
  if [ -z "$GIT_EMAIL" ] || [ -z "$GIT_USERNAME" ]; then
    echo "=> You must set the variables GIT_EMAIL and GIT_USERNAME to be able to commit"
    exit 1
  fi

  git config --global user.email "$GIT_EMAIL"
  git config --global user.name "$GIT_USERNAME"
  git config --global url."https://".insteadOf git://

  # Take https format and convert it to a SSH one so we can push from the CI
  local REPOSITORY_URL="git@github.com:4face-studi0/CineScout.git";

  # Gitlab default URL is https and the push doesn't work
  git remote set-url origin "$REPOSITORY_URL"

  echo "=> set new origin $REPOSITORY_URL";
  local version=$(cat ./client/android/releases.txt)

  ## COMMIT

  # Force releases.txt and build.gradle.kts
  git add -f ./client/android/releases.txt ./client/android/build.gradle.kts;

  git status;

  git commit -m "[release] $version"
  git push origin master;

  ## CREATE RELEASE
  git release create -a ./client/android/build/outputs/apk/debug/* -m "Android $version"
}

main
