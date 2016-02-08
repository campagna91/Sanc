<%-- 
    Document   : signup
    Created on : 13-dic-2015, 12.09.33
    Author     : champ
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>
<!DOCTYPE html>
<html>
    <head>
        <!-- Deny use without javascript enable -->
        <noscript>
            <meta http-equiv="refresh" content="0;url=noscript.html">
        </noscript>
        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link type="text/css" rel="stylesheet" href="lib/materialize/css/materialize.min.css"/>
        <link type="text/css" rel="stylesheet" href="css/system.css"/>
        <script type="text/javascript" src="lib/jQuery/jQuery1.11.1.js"></script>
        <script type="text/javascript" src="lib/materialize/js/materialize.js"></script>
        <script src="http://crypto-js.googlecode.com/svn/tags/3.1.2/build/rollups/aes.js"></script>
        
        <script type="text/javascript" src="lib/davidshimjs-qrcodejs-540308a/qrcode.min.js"></script>

        <title>Signup - Sanc</title>
    </head>
    <body>
        
        <%-- Redirect to user home if sessionSID is setted --%>
        <c:if test="${sessionScope.sessionSID != null}">
            <%response.sendRedirect("home.jsp");%>
        </c:if>
        
        <div class="row">
            
            <%-- Signup link --%> 
            <a style="margin:20px" href="./signin.jsp" class="col s1 waves-effect waves-light btn-large green float right">SIGN IN</a>

            <%-- Retire secret key --%>
            <a style="margin:20px" href="./retire.jsp" class="col s3 waves-effect waves-light btn-large blue float right">LOST OR STOLEN DEVICE?</a>
        </div>
            
        <div style="" class="row valign-wrapper">
            <div style="padding:20px;" class="col s4 offset-s4 blue-grey darken-3 valign">
                <h3>Create new account</h3>
                <form id="signupForm" action="Sanc" method="post">
                
                    <input type="hidden" value="" id="id">
                    <input type="hidden" value="" id="ka">
                    <input type="hidden" value="" id="kb">
                    <input type="hidden" value="" id="kc">
                    <input type="hidden" value="" id="iva">
                    <input type="hidden" value="" id="ivb">
                    <input type="hidden" value="" id="ivc">
                    <input type="hidden" value="" id="ks">
                    <input type="hidden" value="" id="sequence">

                    <%-- 
                        Step one
                    
                        User enters their username (his email)
                    --%>
                    <div id="stepOne">
                        <div class="row">
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="username" name="username" type="email" id="username" />
                                <label for="username">Account email</label>
                            </div>
                        </div>
                        
                        <!-- Switch authentication type -->
                        <div class="row">
                            <h6>Authentication mode</h6>
                            <div class="switch center">
                              <label>
                                Totp
                                <input id="authType" type="checkbox">
                                <span class="lever"></span>
                                Matrix
                              </label>
                            </div>
                        </div>
                    </div>

                    <%-- 
                        Step two
                    
                        User insert their credentials for totp authentiation method
                    --%>
                    <div id="stepTwoTotp" class="hidden">
                        <h6>Set up your credentials</h6>
                        <div class="row">

                            <%-- Desired password --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="password" name="password" type="password" id="password" length="8"/>
                                <label for="username">Password</label>
                            </div>

                            <%-- Confirm password --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="password" name="confirm_password" type="password" id="confirm_password" length="8"/>
                                <label for="username">Confirm password</label>
                            </div>

                            <%-- Passphrase request for some sensible operations --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="passphrase allow you to revoke device key" name="passphrase" type="password" id="passphrase" length="50"/>
                                <label for="username">Passphrase</label>
                            </div>
                            
                        </div>
                        
                        <%-- Qrcode --%>
                        <div id="qrcodeContainer" class="white">
                            <div id="qrcode"></div>
                        </div>
                        
                        <%-- Totp pin --%>
                        <div class="row">
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="totp pin" name="pin" type="password" id="pin" length="6"/>
                                <label for="email">Pin</label>
                            </div>
                        </div>
                    </div>
                        
                     <%-- 
                        Step two
                    
                        User insert their credentials for matrix authentiation method
                    --%>
                    <div id="stepTwoMatrix" class="hidden">
                        <h6>Set up your credentials</h6>
                        <div class="row">

                            <%-- Desired password --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="password" name="matrixPassword" type="password" id="matrixPassword" length="8"/>
                                <label for="username">Password</label>
                            </div>

                            <%-- Confirm password --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="password" name="matrixConfirm_password" type="password" id="matrixConfirm_password" length="8"/>
                                <label for="username">Confirm password</label>
                            </div>
                            
                        </div>
                    </div>

                    <%-- Errors shown --%>
                    <div clas="row">
                        <span class="error"></span>
                    </div>
                    
                    <button id="signupTotp" class="btn-large col s12"/>SEND</button>
                </form>
                    
                <%-- Totp success --%>
                <span class="hidden success" id="success">You are now successful registered!</span>
                
                <%-- Matrix success --%>
                <div id="successMatrix" class="hidden">
                    <p>Printed now the table and use it to access</p>
                    <div id="matrixCredential">
                        <table id="matrix" class="striped"></table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
<script>
    
    // Redirect to home page if session user contains an id 
    $(document).ready(function() {
       if(sessionStorage.getItem("sessionSID") !== null) {
           location.href="home.jsp";
       } 
    });
    
    // Validate sign up data form 
    $(document).on("click", "#signupTotp", function() {
        
        // Block form submit
        event.preventDefault();
        
        if($("#username").val() === "") {
            $("span.error").text("username can't be emtpy");
        } else {
            var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if(!re.test($("#username").val())) {
                $("span.error").text("Username must be a valid email");
            } else {
                requestSignupTotp();
            }
        }
    });
    
    $(document).on("click", "#signupMatrix", function() {
         // Block form submit
        event.preventDefault();
        
        if($("#username").val() === "") {
            $("span.error").text("username can't be emtpy");
        } else {
            var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if(!re.test($("#username").val())) {
                $("span.error").text("Username must be a valid email");
            } else {
                requestSignupMatrix();
            }
        }
    });
    
    // Validate signup data credentials form
    $(document).on("click", "#signupTotpConfirm", function() {
        event.preventDefault();
        if ($("#password").val().length < 8) {
           $("span.error").text("password must have at least 8 digits"); 
        } else {
            if($("#confirm_password").val() !== $("#password").val()) {
                $("span.error").text("confirm password must match password");
            } else {
                if ($("#passphrase").val().length < 50) {
                   $("span.error").text("passphrase must have at least 50 digits"); 
                } else {
                    if(!$.isNumeric($("#pin").val())) {
                        $("span.error").text("pin must be numeric");
                    } else {
                        if($("#pin").val().length < 6) {
                            $("span.error").text("pin must have exactly 6 digits");
                        } else {
                            confirmSignupTotp();
                        }
                    }
                }
            }
        }
    });
    
    // Validte signup data credentials form
    $(document).on("click", "#signupMatrixConfirm", function() {
        event.preventDefault();
        
        if ($("#matrixPassword").val().length < 8) {
           $("span.error").text("password must have at least 8 digits"); 
        } else {
            if($("#matrixConfirm_password").val() !== $("#matrixPassword").val()) {
                $("span.error").text("confirm password must match password");
            } else {
              confirmSignupMatrix();
            }
        }
    });
    
    // Visual validation for data form 
    $(document).on("keyup keydown", "#passphrase, #pin, #password, #confirm_password", function(){
        if($(this).val().length < $(this).attr("length")) {
            $(this).css("border-bottom", "1px solid red");
            $(this).css("box-shadow", "0 1px 0 0 red");
        } else{
            $(this).css("border-bottom", "1px solid lightgreen");
            $(this).css("box-shadow", "0 1px 0 0 lightgreen");   
        }
    });
    
    // Switch between totp and matrix authentication type
    $(document).on("change", "#authType", function() {
        
        // Matrix authentication type is require
        if($(this).prop("checked"))
            $("#signupTotp").attr("id", "signupMatrix");
        else
            $("#signupMatrix").attr("id", "signupTotp");
    });
    
    /**
     * Request a temporary credentials to do login with matrix authentication mode
     */
    function requestSignupMatrix() {
        $("span.error").empty();
        
        $.ajax({
           url: "Sanc",
           method: "post",
           contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
           data: {
               requestType: "signup",
               protoType: "matrix",
               state: "one",
               username: $("#username").val()
           },
           success: function(data) {
               console.log(data);
               
               if ($(data).find("e").html()) {
                    $("span.error").text($(data).find("e").html());
                } else {
                    $("#id").val($(data).find("id").html());
                    $("#ka").val($(data).find("ka").html());
                    $("#iva").val($(data).find("iva").html());
                    $("#stepOne").addClass("hidden");
                    $("#stepTwoMatrix").removeClass("hidden");
                    $("#signupMatrix").attr("id", "signupMatrixConfirm");
                }
           }
        });
    }
    
    /**
     * Request a temporary credentials to do login with totp authentication mode
     */
    function requestSignupTotp() {
        $("span.error").empty();
        
        $.ajax({
            url: "Sanc",
            method: "post",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: {
                protoType: "totp",
                requestType: "signup",
                state: "one",
                username: $("#username").val()
            },
            success: function(data) {
                console.log(data);
                if ($(data).find("e").html()) {
                    $("span.error").text($(data).find("e").html());
                } else {
                    $("#stepOne").addClass("hidden");
                    $("#stepTwoTotp").removeClass("hidden");
                    $("#username").val($(data).find("u").html());
                    $("#id").val($(data).find("id").html());
                    $("#ka").val($(data).find("ka").html());
                    $("#kb").val($(data).find("kb").html());
                    $("#kc").val($(data).find("kc").html());
                    $("#iva").val($(data).find("iva").html());
                    $("#ivb").val($(data).find("ivb").html());
                    $("#ivc").val($(data).find("ivc").html());
                    $("#ks").val($(data).find("ks").html());
                    $("#ks").val("otpauth://totp/" + $("#username").val() +
                            "?secret=" + $("#ks").val());
                    new QRCode(document.getElementById("qrcode"), $("#ks").val());
                    $("#signupTotp").attr('id', 'signupTotpConfirm');
                }
            }
        });
    }
    
    /**
     * Confirm login request with matrix authentication mode
     */
    function confirmSignupMatrix() {
        $("span.error").empty();
        
        $.ajax({
           url: "Sanc",
           method: "post",
           contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
           data: {
               protoType: "matrix",
               requestType: "signup",
               state: "two",
               id: $("#id").val(),
               username: $("#username").val(),
               password: $("#matrixPassword").val() + "---" + $("#ka").val() + "---" +$("#iva").val()
           }, 
           success: function(data) {
               console.log(data);
               
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
               } else {
                    $("#successMatrix").removeClass("hidden");
                    $("form#signupForm").addClass("hidden");
                    
                    // Print matrix
                    $("#matrix").append("<thead><tr><th>/</th><th>A</th><th>B</th><th>C</th><th>D</th><th>E</th><th>F</th><th>G</th>");
                    $("#matrix").append("<tbody></tbody>");
                    for(i = 0; i < 7; i++) {
                        var row = "";
                        for(j = 0; j < 7; j++) {
                                row += "<td>" + $(data).find("ks").html().substr((7*i)+j, 1) + "</td>";
                        }
                        row = "<tr><td class='first'>" + (i+1) + "</td>" + row + "</tr>";
                        $("#matrix tbody").append(row);
                    }
               }
           }
        });
    }
    
    /**
     * Confirm login request with totp authentication mode
     */
    function confirmSignupTotp() {
        $("span.error").empty(); 
        $("input[type=hidden]").empty();
        $.ajax({
            url: "Sanc",
            method: "post",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            data: {
                requestType: "signup",
                protoType: "totp",
                state: "two",
                id: $("#id").val(),
                username: $("#username").val(),
                password: $("#password").val() + "---" + $("#ka").val() + "---" + $("#iva").val(),
                passphrase: $("#passphrase").val() + "---" + $("#kb").val() + "---" + $("#ivb").val(),
                pin: $("#pin").val() + "---" + $("#kc").val() + "---" + $("#ivc").val()   
            },
            success: function(data) {
                console.log("confirmation sent");
                if($(data).find("e").length) {
                    $("span.error").text($(data).find("e").html());
                } else {
                    $("#signupForm").addClass("hidden");
                    $("#success").removeClass("hidden");
                }
            }
        });
    }
</script>