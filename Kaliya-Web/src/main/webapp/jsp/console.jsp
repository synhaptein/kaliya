<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/joblist.css">
</head>
<body>
<div id="header">
<h1>Kaliya Console</h1>
</div>
<div id="main">
<h2>Job list</h2>
<?php
	printJobList();
?>
<% //TODO job list %>    
<h2>System informations</h2>
<% //TODO print parameter list %>
<h2>Installed Demo</h2>
<div id="demos">MD5 Cracker</div>
</div>
<div id="right">
<h2>Client List</h2>
<% //TODO print client list %>
</div>
</body>
</html>