# Review Workflow
This document describes the development process for this project.

## Involved People
Author: Dennis Wäckerle
Reviewer: Methusshan Elankumaran

## Issues
In order for changes to occur for the project an issue has to be created.

### Issue Types

### Issue Status
The following Issue Statuses exist

1. `To Do`
2. `In Progress`
3. `In Review`
4. `Ready To Merge`

#### To Do
Issues in the Status `To Do` have no work started on them yet. New Issues should always be tagged as `To Do`. Issues in
`To Do` status can only be moved to the `In Progress` status.

#### In Progress
Issues in the `In Progress` status are being worked on. Issues in the `In Progress` status must have an assignee and a
corresponding branch.

#### In Review
Issues that have finished being worked on and have a Pull Request (PR) are moved to the `In Review` status. A PR needs
an assigned Reviewer.

In the `In Review` status, the reviewer must review the code and either approve the PR or Request Changes. If the 
Reviewer approves the PR the corresponding Issue has to be set to `Ready To Merge`, otherwise it needs to be set to 
`In Progress`.

#### Ready To Merge
If the PR of an Issue can be Merged it will be labeled as `Ready To Merge`. Tickets with the `Ready To Merge` label, can
either be merged by the author of the PR or be further worked on. If the author decides to further work on the PR its
label needs to be set to `In Review` again, after changes were pushed.

## Git Flow

THe project uses the [GitHub Flow](https://docs.github.com/en/get-started/using-github/github-flow). The GitHub Flow 
consists out of 6 Steps.

![](https://cdn.hashnode.com/res/hashnode/image/upload/v1668070000889/rvf5Hx764.png)

### 1. Create a Branch

Create a branch from an Issue. Create only branches from existing Issues. If no Issue exists create a new one. 

__Never work directly on `main`.__

### 2. Implement the Issue
Implement the code necessary to complete the issue.

### 3. Create a Pull Request
Create a Pull Request. Select the feature branch as a Source and the `main` Branch as the destination.

### 4. Wait for Review and Address Review Comments

Wait until the reviewer is done reviewing the PR. Once a PR is reviewed address all comments. Comments should be argued
with. Comments must not be implemented, if the reviewer can be convinced that a better solutions exists or the proposed
solution does not offer any benefit.

### 5. Merge the Issue
Merge the issue into the `main` branch.

### 6. Delete the Feature Branch
Delete the olf feature branch. The only branches that should exist, are those currently in development and `main`.

## Commit Messages

Commit messages must follow [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/). The commit message
must follow the following format:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

The `optional scope` must be in brackets (`()`).

The following commit types are allowed:

- `fix`
- `feat`
- `build`
- `doc`
- `refactor`
- `test`
- `ci`
- `chore`

Breaking Changes are not used for this project. If the backend API undergoes Breaking Changes, the frontend must be
modified in the same issue.

## Review

The reviewer should account for the following factors, when reviewing a PR.

- Does the new code follow clean code rules?
- Are there alternative 
- Does the new code run?
- Does the new code resolve the issue?

### Local Testing

The application can be tested locally by executing the docker compose file in the `/docker` folder.

In order to run the current application run `docker compose up -d`. A new image will be build automatically.

The application will be available under `localhost:8080`.

### Comments

Comments should use a red, green logic in order to signal, if the comment is an optional nice to have, or has to be
implemented in order for the review to be approved.

- 🟢 - Nice to have
- 🔴 - Must be implemented