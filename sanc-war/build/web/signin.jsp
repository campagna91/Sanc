<%-- 
    Document   : signin
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
        <title>Signin - Sanc</title>
    </head>
    <body>
 
        <%-- Redirect to user home if sessionSID is setted --%>
        <c:if test="${sessionScope.sessionSID != null}">
            <%response.sendRedirect("home.jsp");%>
        </c:if>
        
        <div class="row">
            
            <%-- Signup link --%> 
            <a style="margin:20px" href="./signup.jsp" class="col s1 waves-effect waves-light btn-large red float right">SIGN UP</a>

            <%-- Retire secret key --%>
            <a style="margin:20px" href="./retire.jsp" class="col s3 waves-effect waves-light btn-large blue float right">LOST OR STOLEN DEVICE?</a>
        </div>
            
        <div style="" class="row valign-wrapper">
            <div style="padding:20px;" class="col s4 offset-s4 blue-grey darken-3 valign">
                
                <form id="loginForm" actions="Sanc" method="post">
                    <h3>Login</h3>
                    <%-- 
                        Step one
                    
                        User require login interface
                    --%>
                    <div class="row" id="stepOne">
                        <div class="row">

                            <input type="hidden" id="id"/>
                            <input type="hidden" id="ka"/>
                            <input type="hidden" id="iva"/>
                            <input type="hidden" id="kb"/>
                            <input type="hidden" id="ivb"/>

                            <%-- Username --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="username" name="username" type="email" id="username" />
                                <label for="username">Account email</label>
                            </div>

                            <%-- Password --%>
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="password" name="password" type="password" id="password" length="8"/>
                                <label for="email">Password</label>
                            </div>

                        </div>

                        <%-- 
                            Two factor 

                            It consist into a six digits pin or if user 
                            is in interim state, in temporary credential
                        --%>
                        <div id="twoFactorAuthentication" class="row">
                            <div class="input-field col s10 offset-s1">
                                <input placeholder="two factor athentication" name="twoFactor" type="password" id="twoFactor" length="6"/>
                                <label for="twoFactor">Two factor authentication</label>
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
                        Matrix

                        it consist into a 5 digits
                    --%>
                    <div id="stepTwoMatrix" class="hidden">
                        <div id="matrixAuthentication" class="row">
                            <h6>Enter corresponding matrix letter</h6>

                            <%-- First value --%>
                            <div class="col s1 offset-s1 input-field">
                                <input class="sequence" type="text" id="matrix1">
                                <label for="matrix1"></label>
                            </div>

                            <%-- Second value --%>
                            <div class="col s1 offset-s1 input-field">
                                <input class="sequence" type="text" id="matrix2">
                                <label for="matrix2"></label>
                            </div>

                            <%-- Third value --%>
                            <div class="col s1 offset-s1 input-field">
                                <input class="sequence" type="text" id="matrix3">
                                <label for="matrix3"></label>
                            </div>

                            <%-- Fourth value --%>
                            <div class="col s1 offset-s1 input-field">
                                <input class="sequence" type="text" id="matrix4">
                                <label for="matrix4"></label>
                            </div>

                            <%-- Value to change--%>
                            <div class="col s1 offset-s2 input-field">
                                <input class="sequence" type="text" id="matrix5">
                                <label for="matrix5"></label>
                            </div>
                        </div>
                    </div>
                        
                    <%-- 
                        Step two

                        User is in interim state therefore he must insert new 
                        temporary credential
                    --%>
                    <div id="stepTwoTotp" class="hidden">
                        <div class="row">
                            <h3>Update temporary credentials</h3>
                            <div class="row">

                                <%-- 
                                    New temporary credentials

                                    It must be different from previous temporary
                                    credentials or actual account password
                                --%>
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="temporary credentials" name="newTwoFactor" type="password" id="newTwoFactor" />
                                    <label for="username">Temporary credentials</label>
                                </div>

                                <%-- Confirm temporary credentials --%>
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="confirm temporary credentials" name="confirm_newTwoFactor" type="password" id="confirm_newTwoFactor" />
                                    <label for="email">Confirm temporary credentials</label>
                                </div>

                            </div>
                        </div>
                    
                    </div>
                     
                    <%-- Errors shown --%>
                    <div clas="row">
                        <span class="error"></span>
                    </div>

                    <button id="signinTotpButton" class="btn-large col s12"/>SEND</button>
                </form>
                
                <%-- Matrix successfully authenticated message --%>
                <span class="hidden success">Successfully authenticated</span>
                
            </div>
        </div>
                
    </body>
</html>
<script>
    
    // Redirect to home page if session user contains an id 
    $(document).ready(function() {
       if(sessionStorage.getItem("sessionSID") !== null &&
               sessionStorage.getItem("sessionSID") !== "undefined") {
           location.href="home.jsp";
       } 
    });
    
    // Switch between totp and matrix authentication mode
    $(document).on("change", "#authType", function() {
       if($(this).prop("checked")) {
           $("#twoFactorAuthentication").addClass("hidden");
           $.ajax({
                url: "Sanc",
                method: "post",
                data: {
                    requestType: "signin", 
                    state: "one",
                    protoType: "matrix"
                },
                success: function(data) {
                    console.log(data);
                    
                    if($(data).find("e").length) {
                        $("span.error").text($(data).find("e").html());
                    } else {
                        $("#id").val($(data).find("id").html());
                        $("#ka").val($(data).find("ka").html());
                        $("#iva").val($(data).find("iva").html());
                    }
                }
           });
           $("#signinTotpButton").attr("id", "signinMatrixButton");
       } else {
           $("#twoFactorAuthentication").removeClass("hidden");
           $("#matrixAuthentication").addClass("hidden");
           $("#signinMatrixButton").attr("id", "signinTotpButton");
       }
    });
    
    // Validator matrix data form
    $(document).on("click", "#signinMatrixButton", function() {
       event.preventDefault();
       
       // Error reset
       $("span.error").text("");
       
       if($("#username").val() === "") {
           $("span.error").text("username can't be empty");
       } else {
           var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if(!re.test($("#username").val())) {
                $("span.error").text("Username must be a valid email");
            } else {
                if($("#password").val() === "") {
                    $("span.error").text("Password can't be empty");
                } else {
                    loginMatrix();
                }
            }
        }
    });
    
    // Validator for confirmation matrix login
    $(document).on("click", "#signinMatrixConfirm", function() {
        event.preventDefault();
        
        var complete = true;
        $("input.sequence").each(function() {
            if($(this).val() === "") {
                complete = false;
                return;
            }
        });
        console.log("Resulting " + complete + " test");
        if(!complete) {
            $("span.error").text("sequence must be complete");
        } else {
            confirmSigninMatrix();
        }
    });
    
    // Validator totp data format
    $(document).on("click", "#signinTotpButton", function() {
        
        event.preventDefault();
        
        // Error reset
        $("span.error").text("");
        
        if($("#username").val() === "") {
            $("span.error").text("Username can't be empty");
        } else {
            var re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            if(!re.test($("#username").val())) {
                $("span.error").text("Username must be a valid email");
            } else {
                if($("#password").val() === "") {
                    $("span.error").text("Password can't be empty");
                } else {
                    if($("#twoFactor").val() === "") {
                        $("span.error").text("2° authentication can't be empty");
                    } else {
                        if($("#twoFactor").val().length < 6) {
                            $("span.error").text("2° authentication can't have less than 6 digits");
                        } else {
                            loginTotp();
                        }
                    }
                }
            }
        }
    });

    // Validator for renew temporary credentials
    $(document).on("click", "#signinInterimButton", function() {

        event.preventDefault();
        
        if($("#newTwoFactor").length) {
            if($("#newTwoFactor").val().length < 8) {
                $("span.error").text("Credentials must have at least 8 digits");
            } else {
                if($("#confirm_newTwoFactor").val() !== $("#newTwoFactor").val()) {
                    $("span.error").text("Credential confirmation must match");
                } else {
                    confirmSigninTotp();
                }
            }
        }
    });
    
    // Visual validator for form input data length;
    $(document).on("keyup keydown", "#password, #pin", function(){
        if($(this).val().length < $(this).attr("length")) {
            $(this).css("border-bottom", "1px solid red");
            $(this).css("box-shadow", "0 1px 0 0 red");
        } else{
            $(this).css("border-bottom", "1px solid lightgreen");
            $(this).css("box-shadow", "0 1px 0 0 lightgreen");   
        }
    });
    
    // Convert character position into alphanumeric string
    function convertNumToAN(num) {
        var letter = ['A', 'B', 'C', 'D', 'E', 'F', 'G'];
        var aux = "";
        if(num > 6) {
            var c = letter[num % 7];
            aux += c;
            if(c <= 6) {
                aux += (Math.floor(num/7));
            } else {
                aux += (Math.floor(num/7) + 1);
            }
        } else {
            aux = letter[num] + 1;
        }
        return aux;
    }
    
    // Request login with totp authentication mode
    function loginTotp() {
        $.ajax({
            url: "Sanc",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            method: "post",
            data: {
                requestType: "signin",
                protoType: "totp",
                state: "one"
            },
            success: function(data) {
                $("ka").val($(data).find("ka").html());
                $("iva").val($(data).find("iva").html());
                $("kb").val($(data).find("kb").html());
                $("ivb").val($(data).find("ivb").html());
                $("#id").val($(data).find("id").html());
                
                $.ajax({
                   url: "Sanc",
                   contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                   method: "post",
                   data: {
                        requestType: "signin",
                        protoType: "totp",
                        state: "two",
                        id: $("#id").val(),
                        username: $("#username").val(),
                        password: $("#password").val() + "---" + $("#ka").val(),
                        pin: $("#twoFactor").val() + "---" + $("#kb").val()
                   },
                   success: function(data) {
                       console.log(data);
                       
                       // Empty sensible data
                       $("input[type=hidden]").empty();
                       
                       // If an error occured
                       if($(data).find("e").length) {
                            $("span.error").text($(data).find("e").html());
                            $("#ka").val($(data).find("ka").html());
                            $("#iva").val($(data).find("iva").html());
                            $("#kb").val($(data).find("kb").html());
                            $("#ivb").val($(data).find("ivb").html());
                            $("#id").val($(data).find("id").html());
                            
                        } else if($(data).find("sessionSID").length) {
                            // Set user session and redirect to home
                            sessionStorage.setItem(
                                "sessionSID", $(data).find("sessionSID").html());
                        
                            sessionStorage.setItem(
                                "devices", $(data).find("devices").html());
                        
                            sessionStorage.setItem(
                                "user", $(data).find("user").html());
                        
                            if($(data).find("message").length) {
                                sessionStorage.setItem(
                                    "message", $(data).find("message").html());
                            }
                            location.href="home.jsp";
                        
                        } else {
                            /**
                             * User is in interim state and must insert 
                             * new temporary credentials
                             */ 
                            $("#stepTwoTotp").removeClass("hidden");
                            $("#stepOne").addClass("hidden");
                            $("#id").val($(data).find("id").html());
                            $("#ka").val($(data).find("ka").html());
                            $("#iva").val($(data).find("iva").html());
                            $("#signinTotpButton").attr('id', 'signinInterimButton');
                            
                        }
                    }
                });
           }
        });
    }
    
    // Request login with matrix authentication mode
    function loginMatrix() {
        $.ajax({
           url: "Sanc", 
           method: "post",
           data: {
               requestType: "signin",
               state: "two",
               protoType: "matrix",
               username: $("#username").val(),
               id: $("#id").val(),
               password: $("#password").val() + "---" + $("#ka").val() + "---" + $("#iva").val()
           },
           success: function(data) {
               console.log(data);
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
               } else {
                    $("#stepTwoMatrix").removeClass("hidden");
                    $("#stepOne").addClass("hidden");
                    $("#ka").val($(data).find("ka").html());
                    $("#iva").val($(data).find("iva").html());
                    $("#id").val($(data).find("id").html());
                    var seq = $(data).find("ks").html().split("-");
                    
                    // Print requested values
                    for(i = 1; i <= seq.length; ++i)
                        $("label[for='matrix" + i + "']").text(convertNumToAN(seq[i-1]));
                    $("#signinMatrixButton").attr("id", "signinMatrixConfirm");
               }
           }
        });
    }
   
    // Confirm login with totp authentication mode sending new temporary credentials
    function confirmSigninTotp() {
        $.ajax({
           url: "Sanc",
           method: "post",
           contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
           data: {
               protoType: "totp",
               requestType: "signin",
               state: "three",
               id: $("#id").val(),
               username: $("#username").val(),
               temporaryCredentials: $("#newTwoFactor").val() + "---" + $("#ka").val() + "---" + $("#iva").val()
           },
           success: function(data) {
               console.log(data);
               
               // If an error occured
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
                   $("#id").val($(data).find("id").html());
                   $("#ka").val($(data).find("ka").html());
                   $("#iva").val($(data).find("iva").html());
                   
               } else {
                   /** 
                    * Temporary credentials are correctly updated
                    * and user is now logged into system
                    */
                    sessionStorage.setItem(
                        "sessionSID", $(data).find("sessionSID").html());

                    sessionStorage.setItem(
                        "devices", $(data).find("devices").html());

                    sessionStorage.setItem(
                        "user", $(data).find("user").html());

                    if($(data).find("message").length) {
                        sessionStorage.setItem(
                            "message", $(data).find("message").html());
                    }
                    location.href="home.jsp";
               }
           }
        });
    }
    
    // Confirm login with matrix authentication mode sending matrix sequence
    function confirmSigninMatrix() {
        $("span.error").empty();
        
        $.ajax({
            url: "Sanc",
            method: "post",
            data: {
                requestType: "signin",
                protoType: "matrix",
                state: "three",
                id: $("#id").val(),
                username: $("#username").val(),
                sequence:   $("#matrix1").val() + "-" +
                            $("#matrix2").val() + "-" +
                            $("#matrix3").val() + "-" +
                            $("#matrix4").val() + "-" +
                            $("#matrix5").val() + "---" +
                            $("#ka").val() + "---" +
                            $("#iva").val()
            },
            success: function(data) {
                console.log(data);
                
                if($(data).find("e").length) {
                    $("span.error").text($(data).find("e").html());
                    $("#ka").val($(data).find("ka").html());
                    $("#iva").val($(data).find("iva").html());
                    $("#id").val($(data).find("id").html());
                    var seq = $(data).find("ks").html().split("-");
                    
                    // Empty values and print new requested values
                    for(i = 1; i <= 5; ++i) {
                        $("label[for='matrix" + i + "']").text(convertNumToAN(seq[i-1]));
                    }
                    $("input.sequence").empty();
                    
                } else {
                    $("span.success").removeClass("hidden");
                    $("#loginForm").addClass("hidden");
                }
            }
        });
    }
</script>