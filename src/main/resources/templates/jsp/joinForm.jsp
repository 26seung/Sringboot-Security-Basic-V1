<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>회원가입 페이지</h1>
<div class="container">
<form action="/join" method="post" onSubmit="return valid()">
 <div class="link">
    <input type="text" id="username" name="username" placeholder="아이디를 입력하세요"/>
    <span text="${valid_username}" />
    <button type="button" onClick="usernameCheck()">중복체크</button>
    </div>
     <div class="link">
    <input type="password" name="password" placeholder="비밀번호를 입력하세요"/>
       </div>
     <div class="link">
    <input type="text" name="email" placeholder="이메일을 입력하세요"/>
       </div>
    <button type="submit">회원가입</button>
</form>
</div>
<script>

var isChecking=false;

function valid(){
alert("확인");
return isChecking;
}

function usernameCheck(){

    var username = $("#username").val():
    console.log(username);

    $.ajax({
        type:"POST",
        url:"/user?cmd=usernameCheck",
        data:username,
        contentType:"text/plain;charset=utf-8",
        dataType:"text"
        });
}

</script>
</body>
</html>