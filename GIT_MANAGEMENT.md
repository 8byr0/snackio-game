# snackio

Snackio is a multiplayer RPG platform game.

# Repository rules
## Data flow
All the features developed must be related to an opened issue on gitlab. Refer to https://gitlab.com/bYr0/snackio/boards to see existing issues. 
The tool used to organize data flow is git-flow. To install it and learn more about the way it works, refer to this document : https://danielkummer.github.io/git-flow-cheatsheet/

## First time init
On the first time you clone this repository, you need to run
```sh
git flow init
```
This will initialize git flow locally. This is mandatory.

## Commit
The way we commit to branch is inspired by Karma runner's rules.
A commit has to be written like this:
```sh
git commit -m "<type>: description of your commit"
```
Where `<type>` can be one of the following: 
```
feat (new feature)
fix (bug fix)
docs (changes to documentation)
style (formatting, missing semi colons, etc; no code change)
refactor (refactoring production code)
test (adding missing tests, refactoring tests; no production code change)
```
A few examples:
```sh
git commit -m "feat: Add a placeholder to user field"
git commit -m "fix: myFunction throws JavaNullPointerException"
git commit -m "docs: Add the documentation of MyClass method"
git commit -m "style: Add missing semicolon in myFunction"
```

## Creating a new feature
```sh
git checkout develop # Switch to develop branch
git pull # get last updates
git flow feature start 123 # Start a new feature development (123 is an exemple an would be the one for issue #123 in gitlab board)
git push --set-upstream origin feature/123 # Push your branch to remote repository
```
What these commands will basically do is create a new branch `feature/123` on which you'll be able to push all your code without disturbing other running developments.

## Releasing a feature
Once your dev is ready and you're sure it works properly, you must rebase your branch on develop (e.g. place your work on the top of develop). To do so:
```sh
git checkout develop # Switch to develop branch
git pull # Pull last updates that have been made since your dev started
git checkout feature/XXX # switch to your feature branch (replace XXX with your own issue number)
git rebase develop # Rebase your work on top of develop
```
The last step may show you conflicts, you'll have to resolve them before pushing.
Once done, just run
```sh
git push --force
```

Now that your feature is pushed and up to date, go to https://gitlab.com/bYr0/snackio/merge_requests/new and create a merge request. You'll have to specify a source branch (`feature/XXX`) and a destination branch (`develop`). If everything went well, notify `hugues.bureau@groupe-esigelec.org`, who is in charge of merging features to develop.

# Code rules
## Language
All the code must be written in english.

## Variables
Variables must be lowerCamelCase
```java
int myLongString = 0;
```
Do not use short variables like `a`, `varAbc`... All the content must be readable without comments !

## Functions / Methods
Functions must be lowerCamelCase
```java
public int multiplyTwoVariables(int firstVariable, int secondVariable) {
    return firstVariable + secondVariable;
}
``` 
Do not use short functions like `a`, `doSmth`... All the content must be readable without comments !

Don't forget to properly write javadoc
```java
/**
 * Returns the result of a multiplication.
 * @param firstVariable the first variable to multiply
 * @param secondVariable the second variable to multiply
 */
public int multiplyTwoVariables(int firstVariable, int secondVariable) {
    return firstVariable * secondVariable;
}
```
When you write a function, make it as simple as possible, avoid functions with more than 40 lines.