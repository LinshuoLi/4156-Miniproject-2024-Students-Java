# Welcome Students of 4156

Please follow the assignment specifications on Courseworks when completing this project.

In this project, I used the PMD plugin in Intellij initially. After reading questions posted
on edstem, I switched to the CLI version. I used two command lines. The first one is 
"pmd check -d src/main/java -R rulesets/java/quickstart.xml -f text -r bug.txt". It will generate 
bugs to a file called bug.txt. The second one is "mvn pmd:check", It will generate bugs in the 
terminal.
