<h1>
job4j_dreamjob
</h1>

[![Build Status](https://travis-ci.com/k-r-3/job4j_dreamjob.svg?branch=master)](https://travis-ci.com/k-r-3/job4j_dreamjob)
[![codecov](https://codecov.io/gh/k-r-3/job4j_dreamjob/branch/master/graph/badge.svg?token=KJ72A7JEFU)](https://codecov.io/gh/k-r-3/job4j_dreamjob)

<h3>Description :</h3>

<p>
This project represents labor exchange.
</p>
<p>
The app provide CRUD operations based on database query.
</p>
<h4>
Used technologies :
</h4>
<ul>
<li>Servlets</li>
<li>JDBC</li>
<li>PowerMock</li>
<li>JUnit</li>
<li>Slf4j</li>
<li>Checkstyle</li>
</ul>
<p>
Web-Service has a 5 screens
</p>
<ul>

<li>
Log-in or registration in the service
</li>
<p>

![ScreenShot](images/main.png)

</p>
<li>
List of candidates
</li>
<p>

![ScreenShot](images/candidates.png)

</p>
<li>
Candidate profile editing
</li>
<p>

![ScreenShot](images/edit.png)

</p>
<li>
List of vacancies
</li>
<p>

![ScreenShot](images/posts.png)

</p>
<li>
Vacancy editing
</li>
<p>

![ScreenShot](images/post_edit.png)

</p>
</ul>
<h3>
Configurations :
</h3>
<p>
To app boot is necessary use Apache Tomcat and edit run configuration. 
This configuration should have follow points : 
</p>
<ul>
<li>
'http://localhost:8080/dreamjob' as start page url;
</li>
<li>
'job4j_dreamjob:war exploded' as build artifact;
</li>
<li>
'/dreamjob' as application context
</li>
</ul>
<p>
NOTICE, before first run is necessary use maven compile command
('mvn compile' for console input, or phase 'compile' of Maven Lifecycle in case compile from IDE).
</p>
<p>
After success compile, Liquibase create schema in Database. 
For authorize in service You might input 'root@local' in email form 
and 'root' in password form of start page.
</p>
<h3>Contact</h3>
<p>If You have any question, please contact me:</p>
<p>https://t.me/roman_kozlov</p>




