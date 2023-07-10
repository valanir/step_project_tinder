<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="https://cdn-icons-png.flaticon.com/512/4675/4675855.png">

    <title>Like page of ${username}</title>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css" integrity="sha384-DNOHZ68U8hZfKXOrtjWvjxusGo9WQnrNx2sqG0tfsghAvtVlRW3tvkXWZh58N9jp" crossorigin="anonymous">
    <!-- Bootstrap core CSS -->
    <link href="resources/html/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link rel="stylesheet" href="resources/html/css/style.css">
</head>
<body style="background-color: #f5f5f5;">

<div class="col-4 offset-4">
    <div class="card">
        <div class="card-body">
            <div class="row">
                <div class="col-12 col-lg-12 col-md-12 text-center">
                    <img src="${image}" alt="" class="mx-auto rounded-circle img-fluid" width="300" height="300">
                    <h3 class="mb-0 text-truncated">${username}</h3>
                    <br>
                </div>
                <form name="action" method="POST"style="width: 450px; height: 25px;">
                <div class="col-12 col-lg-18">
                    <button class="btn btn-outline-success btn-block" name= "result" type="submit" value="yes" ><span class="fa fa-heart"></span> Yes </button>
                    <button class="btn btn-outline-danger btn-block" name="result" type="submit" value="no" ><span class="fa fa-times"></span> No </button>
                </div>
                </form>
                <!--/col-->
            </div>
            <!--/row-->
        </div>
        <!--/card-block-->
    </div>
</div>

</body>
</html>