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
        <title>Retire - Sanc</title>
    </head>
    <body>
        
        <div class="row">
            
            <%-- Signup link --%> 
            <a style="margin:20px" href="./signup.jsp" class="col s1 waves-effect waves-light btn-large red float right">SIGN UP</a>

            <%-- Signin link --%>
            <a style="margin:20px" href="./signin.jsp" class="col s1 waves-effect waves-light btn-large green float right">SIGN IN</a>
        </div>
            
        <div class="row valign-wrapper">
            <div style="padding:20px;" class="col s4 offset-s4 blue-grey darken-3 valign">
                <form id="loginForm" actions="Sanc" method="post">
                    <input type="hidden" id="id">
                    <input type="hidden" id="ka">
                    <input type="hidden" id="iva">
                    <input type="hidden" id="kb">
                    <input type="hidden" id="ivb">
                    
                    <!-- 
                        Step one 
                            
                        User request to block all device's key
                    -->
                    <div id="stepOne">
                        <div class="row">
                            <h3>Block access</h3>
                            <div class="row">
                                
                                <!-- Username  -->
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="username" name="username" type="email" id="username" />
                                    <label for="username">Account email</label>
                                </div>

                                <!-- User password -->
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="password" name="password" type="password" id="password" length="8"/>
                                    <label for="email">Password</label>
                                </div>

                                <!-- Passphrase to authorize request -->
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="passphrase" name="passphrase" type="password" id="passphrase" length="50"/>
                                    <label for="passphrase">Passphrase</label>
                                </div>
                            </div>
                        </div>
                    </div>
                        
                    <!-- 
                        Step two
                        
                        User insert temporary credential to access into system
                    -->
                    <div id="stepTwo" class="hidden">
                        <div class="row">
                            <h3>Set temporary credentials</h3>
                            <div class="row">
                                
                                <!-- New temporary credential -->
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="temporary credentials" name="temporaryCredentials" type="password" id="temporaryCredentials" length="8"/>
                                    <label for="username">Temporary credentials</label>
                                </div>

                                <!-- Temporary credential confirm -->
                                <div class="input-field col s10 offset-s1">
                                    <input placeholder="confirm temporary credentials" name="confirm_temporaryCredentials" type="password" id="confirm_temporaryCredentials" length="8"/>
                                    <label for="email">Confirm temporary credentials</label>
                                </div>
                            </div>
                        </div>
                    </div> 
                     
                    <%-- Errors --%>
                    <div clas="row">
                        <span class="error"></span>
                    </div>

                    <button id="retireButton" class="btn-large col s12"/>SEND</button>
                </form>
                    
                <!-- Show request success -->
                <div id="success" class="hidden">
                    <span class="valid">I tuoi dati sono ora al sicuro!</span>
                </div>
                    
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
    
    // Validator data format 
    $(document).on("click", "#retireButton", function() {
        
        event.preventDefault();
        
        // Error reset
        $("span.error").text("");
        
        // Validate data to send
        if($("#username").length) {
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
                        if($("#passphrase").val() === "") {
                            $("span.error").text("Passphrase can't be empty or have less than 50 digits");
                        } else {
                            if($("#passphrase").val().length < 50) {
                                $("span.error").text("passphrase can't have less than 50 digits");
                            } else {
                                retire();
                            }
                        }
                    }
                }
            }
        }
    });
    
    // Validator data format for new temporary credential
    $(document).on("click", "#retireConfirmButton", function() {
        
        // Block form submiting 
        event.preventDefault();
        
        if($("#temporaryCredentials").length) {
            if($("#temporaryCredentials").val().length < 8) {
                $("span.error").text("Credentials must have at least 8 digits");
            } else {
                if($("#confirm_temporaryCredentials").val() !== $("#temporaryCredentials").val()) {
                    $("span.error").text("Credential confirmation must match");
                } else {
                    createTemporaryCredentials();
                }
            }
        }
    });
    
    // Validator visual length
    $(document).on("keyup keydown", "#passphrase, #password, #temporaryCredentials, #confirm_temporaryCredentials", function(){
        if($(this).val().length < $(this).attr("length")) {
            $(this).css("border-bottom", "1px solid red");
            $(this).css("box-shadow", "0 1px 0 0 red");
        } else{
            $(this).css("border-bottom", "1px solid lightgreen");
            $(this).css("box-shadow", "0 1px 0 0 lightgreen");   
        }
    });
    
    /**
     * Allow user to withdraw key of all his device
     * Function request a temporary key and then send encrypted data form
     */
    function retire() {
        $.ajax({
            url: "Sanc",
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            method: "post",
            data: {
                requestType: "retire",
                protoType: "totp",
                state: "one"
            },
            success: function(data) {
                console.log(data);
                $("#ka").val($(data).find("ka").html());
                $("#iva").val($(data).find("iva").html());
                $("#kb").val($(data).find("kb").html());
                $("#ivb").val($(data).find("ivb").html());
                $("#id").val($(data).find("id").html());
                
                $.ajax({
                   url: "Sanc",
                   contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
                   method: "post",
                   data: {
                        requestType: "retire",
                        protoType: "totp",
                        state: "two",
                        id: $("#id").val(),
                        username: $("#username").val(),
                        password: $("#password").val() + "---" + $("#ka").val() + "---" + $("#iva").val(),
                        passphrase: $("#passphrase").val() + "---" + $("#kb").val() + "---" + $("#ivb").val()
                   },
                   success: function(data) {
                       console.log(data);
                       
                       // Empty sensible data
                       $("input[type=hidden]").empty();
                       
                       if($(data).find("e").length) {
                            $("span.error").text($(data).find("e").html());
                            $("#ka").val($(data).find("ka").html());
                            $("#iva").val($(data).find("iva").html());
                            $("#kb").val($(data).find("kb").html());
                            $("#ivb").val($(data).find("ivb").html());
                            $("#id").val($(data).find("id").html());
                        } else {
                            $("#stepTwo").removeClass("hidden");
                            $("#stepOne").addClass("hidden");
                            $("#ka").val($(data).find("ka").html());
                            $("#iva").val($(data).find("iva").html());
                            $("#id").val($(data).find("id").html());
                            $("#retireButton").attr('id', "retireConfirmButton");
                        }
                   }
                });
           }
        });
    }
    
    /**
     * Allow uer to renvew his temporary credentials each acces in interim state
     */
    function createTemporaryCredentials() 
    {
        $("span.error").empty();   
        $.ajax({
           url: "Sanc",
           method: "post",
           contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
           data: {
               protoType: "totp",
               requestType: "retire",
               username: $("#username").val(),
               state: "three",
               id: $("#id").val(),
               temporaryCredentials: $("#temporaryCredentials").val() + "---" + $("#ka").val() + "---" + $("#iva").val()
           },
           success: function(data) {
               console.log(data);
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
                   $("#id").val($(data).find("id").html());
                   $("#ka").val($(data).find("ka").html());
                   $("#iva").val($(data).find("iva").html());
               } else {
                  $("form").addClass("hidden");
                  $("#success").removeClass("hidden");
               }
           }
        });
    }
</script>