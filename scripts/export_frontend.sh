#!/usr/bin/env bash
set -euo pipefail

if ! command -v git >/dev/null 2>&1; then
  echo "git command is required" >&2
  exit 1
fi

TARGET_REPO_URL="${1:-git@github.com:LuckyBurger7/LuckyBurger7-Front.git}"
TARGET_BRANCH="${2:-main}"
SUBTREE_PREFIX="frontend"
TEMP_BRANCH="frontend-export-$(date +%s)"

REPO_ROOT="$(git rev-parse --show-toplevel)"
cd "$REPO_ROOT"

echo "Splitting subtree '$SUBTREE_PREFIX' into temporary branch '$TEMP_BRANCH'..."
git subtree split --prefix="$SUBTREE_PREFIX" -b "$TEMP_BRANCH"

cleanup() {
  if git rev-parse --verify "$TEMP_BRANCH" >/dev/null 2>&1; then
    git branch -D "$TEMP_BRANCH" >/dev/null 2>&1 || true
  fi
}
trap cleanup EXIT

if ! git ls-remote "$TARGET_REPO_URL" &>/dev/null; then
  echo "Remote $TARGET_REPO_URL does not exist or is not accessible."
  echo "Attempting to create a bare repository at the target url is not supported automatically."
  echo "Please ensure the repository exists and that you have access."
  exit 1
fi

echo "Pushing subtree branch to $TARGET_REPO_URL ($TARGET_BRANCH)..."
git push "$TARGET_REPO_URL" "$TEMP_BRANCH:$TARGET_BRANCH"

echo "Frontend subtree successfully pushed to $TARGET_REPO_URL on branch $TARGET_BRANCH."
