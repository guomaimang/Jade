const queryParams = new URLSearchParams(window.location.search);
const code = queryParams.get("code");
let data = {"code": code}

$.ajax({
    type: "POST",
    dataType: "json",
    url: "/userauth/oauth2/callback",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(data),

    success: function (result) {
        if (result.code === 0) {
            window.localStorage.setItem("jwt", result.data.jwt);
            window.localStorage.setItem("email", result.data.user.email);
            window.localStorage.setItem("nickname", result.data.user.nickname);

            window.location.href = `intent://open?text=${encodeURIComponent(JSON.stringify(result))}#Intent;scheme=myapp;package=com.iems5722.jade;end`;

        }else {
            alert("Login failed! The account is not authenticated!")
            window.location.href = "/login.html";
        }
    },
    error: function () {
        alert("Interface exception, please contact the administrator!")
    }
});