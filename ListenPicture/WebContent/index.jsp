<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Final Project</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">


<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link href="https://fonts.googleapis.com/css?family=Montserrat"
	rel="stylesheet">
<link href="css/main.css" rel="stylesheet" type="text/css" />


<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/shop-homepage.css" rel="stylesheet">
</head>

<body background="image/background.png">
	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
	<div class="container">
		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="index.jsp">ListenPhoto</a>

		</div>
		<!-- /.navbar-collapse -->
	</div>
	</nav>

	<div class="container">
	<!-- <hr>Upload your file:</hr> -->
	<font color="#C0C0C0">Upload your file:</font>
		<form action="song" method="post" enctype="multipart/form-data" style="float: right">

			<input type="file" name="file" style="float: left" /> <input type="submit"
				value="upload" />
		</form>
	</div>



	<!-- Page Content -->
	<c:if test="${requestScope.show=='yes'}"> 
	<div class="container-fluid bg-2 text-center">

		<div class="row">
			<div class="col-md-3">
				<p class="lead" style="color: #C0C0C0"></p>
				<p class="lead" style="color: #C0C0C0"></p>
				<p class="lead" style="color: #C0C0C0"></p>

				<p class="lead" style="color: #C0C0C0">${requestScope.songs0}</p>
				<p class="lead" style="color: #C0C0C0">${requestScope.songs1}</p>
				<p class="lead" style="color: #C0C0C0">${requestScope.songs2}</p>
				<p class="lead" style="color: #C0C0C0">${requestScope.songs3}</p>
				<p class="lead" style="color: #C0C0C0">${requestScope.songs4}</p>

			</div>

			<div class="col-md-9">

				<div class="row carousel-holder">

					<div class="col-md-12">
						<div id="carousel-example-generic" class="carousel slide"
							data-ride="carousel">
							<ol class="carousel-indicators">
								<li data-target="#carousel-example-generic" data-slide-to="0"
									class="active"></li>
								<li data-target="#carousel-example-generic" data-slide-to="1"></li>
								<li data-target="#carousel-example-generic" data-slide-to="2"></li>
							</ol>
							<div class="carousel-inner">
								<div class="item active">

									<iframe class="slide-image" width="400" height="315"
										src="${requestScope.links0}"
										frameborder="0" allowfullscreen></iframe>
								</div>
								<div class="item">
									<iframe class="slide-image" width="400" height="315"
										src="${requestScope.links1}"
										frameborder="0" allowfullscreen></iframe>
								</div>
								<div class="item">
									<iframe class="slide-image" width="400" height="315"
										src="${requestScope.links2}"
										frameborder="0" allowfullscreen></iframe>
								</div>
								
								<div class="item">
									<iframe class="slide-image" width="400" height="315"
										src="${requestScope.links3}"
										frameborder="0" allowfullscreen></iframe>
								</div>
								
								<div class="item">
									<iframe class="slide-image" width="400" height="315"
										src="${requestScope.links4}"
										frameborder="0" allowfullscreen></iframe>
								</div> 
							</div>
							<a class="left carousel-control" href="#carousel-example-generic"
								data-slide="prev"> <span
								class="glyphicon glyphicon-chevron-left"></span>
							</a> <a class="right carousel-control"
								href="#carousel-example-generic" data-slide="next"> <span
								class="glyphicon glyphicon-chevron-right"></span>
							</a>
						</div>
					</div>

				</div>
			</div>

		</div>

	</div>
	</c:if> 
	<!-- /.container -->


	<div class="container-fluid bg-3 text-center">
		<h3 class="margin copyright">Copyright, Web Design, Resource,
			Picture......Almost Everything @ 2017 EastearnBeauties</h3>
		<br>
	</div>


	<!-- jQuery -->
	<script src="js/jquery.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>
</body>

</html>