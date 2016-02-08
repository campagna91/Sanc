<%-- 
    Document   : home
    Created on : 15-dic-2015, 10.39.32
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
        <script type="text/javascript" src="lib/davidshimjs-qrcodejs-540308a/qrcode.min.js"></script>
        <title>Home - Sanc</title>
    </head>
    
    <body>
        
        <input type="hidden" id="id">
        <input type="hidden" id="ka">
        <input type="hidden" id="iva">
        <input type="hidden" id="kb">
        <input type="hidden" id="ivb">
        <input type="hidden" id="ks">
        
        <%-- Menu --%>
        <nav>
            <div class="nav-wrapper blue-grey darken-3">
              <a id="logo" href="#!" class="brand-logo">Benvenuto </a>
              <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
              <ul class="right hide-on-med-and-down">
                <li><a id="exit">Esci</a></li>
              </ul>
              <ul class="side-nav blue-grey darken-3" id="mobile-demo">
                <li><a id="exit">Esci</a></li>
              </ul>
            </div>
        </nav>
        
        <!-- Temporary credentials -->
        <div id="tempCredentialsAlert" class="modal">
          <div class="modal-content">
            <h4>Notice</h4>
            <p>Currently to access your account you are using temporary credentials.
                If you wanted to change them at any time press the "remove temporary" 
                link at the top right of the screen.</p>
          </div>
          <div class="modal-footer">
            <a href="#!" class=" modal-action modal-close waves-effect waves-green btn-flat">I understand</a>
          </div>
        </div>
        
        <!-- Remove interim state -->
        <div id="renewTemporaryCredentials" class="modal modal-fixed-footer blue-grey darken-3">
            <div class="modal-content">
                <h3>Renew your temporary credentials</h3>
                <div class="row">
                    <p>Select the device for which it intends to renew the credentials 
                        and start right now to use pin access rather than a new 
                        password on each login.</p>
                    
                    <!-- Device alias for which you want renew key -->
                    <div class="input-field">
                        <select id="deviceToRenew">
                            <option value="">Select a device</option>
                        </select>
                    </div>
                    
                    <!-- Qrcode -->
                    <div id="qrcodeContainer" class="white">
                        <div id="qrcodeRenewer"></div>
                    </div>
                </div>
                
                <!-- Authorization pin of new device -->
                <div class="row">    
                    <div class="input-field">
                        <input class="col s4 offset-s4" type="password" id="pinToRenew" name="pinToRenew" length="6"/>
                        <label for="pinToRenew">Pin</label>
                    </div>
                </div>
                
                <!-- Passphrase to authorize request -->
                <div class="row">    
                    <div class="input-field">
                        <input type="password" id="passphraseToRenew" name="passphraseToRenew" length="50"/>
                        <label for="passphraseToRenew">Passphrase</label>
                    </div>
                </div>
                
                <!-- Errors -->
                <span class="error"></span>
                
            </div>
            <div class="modal-footer">
              <a id="renewDeviceCredentials" class="btn red">Renew</a>
              <a class="left btn-flat" onclick="$('#renewTemporaryCredentials').closeModal()">Back</a> 
            </div>
        </div>
        
        <!-- Remove device -->
        <div id="blockCombinedDevice" class="modal modal-fixed-footer blue-grey darken-3">
            <div class="modal-content">
                <h3>Uncombine device</h3>
                
                <div class="row">
                    
                    <!-- Name of the device to be blocked -->
                    <h4 id="aliasToBlock"></h4>
                    
                    <!-- Passphrase to authorize request -->
                    <div class="input-field">
                        <input type="password" id="passphrase" name="passphrase"length="50"/>
                        <label for="passphrase">Passphrase</label>
                    </div>
                    
                </div>
                <span class="error"></span>
            </div>
            <div class="modal-footer">
              <a id="blockDeviceConfirm" class="btn red">Combined</a>
              <a class="left btn-flat" onclick="$('#blockCombinedDevice').closeModal()">Back</a> 
            </div>
        </div>
        
        <!-- Combine new device -->
        <div id="addDevice" class="modal modal-fixed-footer blue-grey darken-3">
            <div class="modal-content">
                <h3>Combine new device</h3>
                <div class="row">
                    
                    <!-- Alias of new device to be combine -->
                    <div class="input-field">
                        <input type="text" id="newDeviceAlias" name="newDeviceAlias"/>
                        <label for="newDeviceAlias">Alias device</label>
                    </div>
                    
                    <!-- Authorization pin of combined device -->
                    <div class="col s4 offset-s4 input-field">
                        <input type="password" id="twoFactor" name="twoFactor" length="6"/>
                        <label for="twoFactor">Two factor authorization</label>
                    </div>
                    
                </div>
                <div class="row">
                    
                    <!-- Qrcode -->
                    <div id="qrcodeContainer" class="white">
                        <div id="qrcode"></div>
                    </div>
                    
                    <!-- Pin of device to be combine -->
                    <div class="col s4 offset-s4 input-field">
                        <input type="password" id="newTwoFactor" name="newTwoFactor" length="6"/>
                        <label for="newTwoFactor">Alias</label>
                    </div>
                    
                </div>
                <span class="error"></span>
            </div>
            <div class="modal-footer">
              <a id="addDeviceConfirm" class="btn red">Combined</a>
              <a class="left btn-flat" onclick="$('#addDevice').closeModal()">Back</a> 
            </div>
        </div>
        
        <%-- Add device button --%>
        <div style="margin-top: 20px;" class="row">
            <a id="btnAddDevice" class="col s2 offset-s1 btn green">ADD DEVICE</a>
        </div>
        
        <%-- List of associated devices --%>
        <div class="row"> 
            <table id="deviceList" class="striped highlight col s10 offset-s1"> 
                <thead>
                    <tr style="color:white; text-align:center;" class="blue-grey darken-2">
                        <th>Device name</th>
                        <th>Added</th>
                        <th>Uncombine</th>
                    </tr>
                </thead>
                <tbody class="blue-grey darken-1"></tbody>
            </table>
        </div>
        
    </body>
</html>
<script>
    
    // Check if user sessionSID is setted
    if(sessionStorage.getItem("sessionSID") === null ||
        sessionStorage.getItem("sessionSID") === 'undefined') {
            location.href="index.jsp";
    }
    
    // Logout request
    $(document).on("click", "#exit", function() {
        sessionStorage.removeItem("devices");            
        sessionStorage.removeItem("sessionSID");
        sessionStorage.removeItem("user");
        sessionStorage.removeItem("message");
        location.href="signin.jsp";
    });
    
    // Graphic library initialization
    $(document).ready(function() {
        checkTemporaryCredentials();
        loadUserDeviceList();
        $("#logo").text("Benvenuto " + sessionStorage.getItem("user").split("@")[0]);
        $('.modal-trigger').leanModal();
        $(".button-collapse").sideNav();
    });
    
    // Request for add a device
    $(document).on("click", "#btnAddDevice", function() {
        $("input[type=hidden]").empty();
        $("span.error").empty();
        $("#qrcode").empty();
        $("#newDeviceAlias").empty();
        combineRequest();
    });
    
    // Request for block a combined device
    $(document).on("click", ".blockDevice", function() {
        $("span.error").empty();
        $("#aliasToBlock").text($(this).parent().prev().prev().text());
        uncombineRequest();
    });
    
    // Confirm device block
    $(document).on("click", "#blockDeviceConfirm", function() {
        $("span.error").empty();
        uncombineConfirm(); 
    });
    
    // Request renew credentials for a combined device
    $(document).on("click", "#removeTemporaryCredentials", function() {
        $("span.error").empty();
        
        $("#deviceList tbody tr").each(function() {
            var alias = $(this).children("td").eq(0).text();
            $("#deviceToRenew").append("<option value='" + alias + "'>" + alias + "</option>");
        });
        $("#qrcodeRenewer").empty();
        $('select option:gt(1)').each(function() {
            $(this).remove();
        });
        $('select').material_select();
        removeTemporaryCredentialsRequest();
    });
    
    // Confirms the cancellation of the temporary credentials
    $(document).on("click", "#renewDeviceCredentials", function() {
        $("span.error").empty();
        
        if($("#deviceToRenew").val() === "") {
            $("span.error").text("a device must be selected");
        } else {
            if($("#pinToRenew").val().length < 6) {
                $("span.error").text("confirmation pin can't be empty or have less than 6 digits");
            } else {
                if($("#passphraseToRenew").val() < 50) {
                    $("span.error").text("passphrase can't be empty or have less than 50 digits");
                } else {
                    removeTemporaryCredentialsConfirm();
                }
            }
        }
    });
    
    // Validation data form for insertion of new device
    $(document).on("click", "#addDeviceConfirm", function() {
        $("span.error").empty();
        
        if($("#newDeviceAlias").val().lenght) {
            $("span.error").text("device alias can't be empty");
        } else {
            if($("#twoFactor").val().length === 0) {
                $("span.error").text("two factor authorization can't be empty or have less than 6 digits");
            } else {
                if($("#newTwoFactor").val().length === 0) {
                    $("span.error").text("new device factor can't be empty or have less than 6 digis");
                } else {
                    combineConfirm();
                }
            }
        }
    });
    
    /**
     * Dispaly user combined devices list
     */
    function loadUserDeviceList() {
        $("#deviceList tbody tr").each(function() {
            $(this).remove();
        });
        var devices = sessionStorage.getItem("devices").split("|");
        for(i = 0; i < devices.length - 1; i++) {
            $("#deviceList tbody").append(
                "<tr>" +
                "<td>" + devices[i].split(",")[0] + "</td>" +
                "<td>" + devices[i].split(",")[1] + "</td>" +
                "<td>" + 
                    "<a class='modal-trigger btn red blockDevice'>REMOVE DEVICE</a>" +
                "</td>" +
                "</tr>"
            );
        }
    }
    
    /**
     * Request an insertion for a new user device 
     */
    function combineRequest() {
        $.ajax({
            url: "Sanc",
            method: "post",
            data: {
                requestType: "combine",
                protoType: "totp",
                state: "one",
                username: sessionStorage.getItem("user")
            },
            success: function(data) {
                console.log(data);
                $("#id").val($(data).find("id").html());
                $("#ka").val($(data).find("ka").html());
                $("#iva").val($(data).find("iva").html());
                $("#kb").val($(data).find("kb").html());
                $("#ivb").val($(data).find("ivb").html());
                $("#ks").val($(data).find("ks").html());
                $("#ks").val("otpauth://totp/" + sessionStorage.getItem("user") +
                        "?secret=" + $("#ks").val());
                $("#addDevice").openModal({dismissible:false});
                new QRCode(document.getElementById("qrcode"), $("#ks").val());
            }
        });    
    }
    
    /**
     * Confirm user device
     */
    function combineConfirm() {
        $.ajax({
           url: "Sanc",
           method: "post",
           data: {
               requestType: "combine",
               protoType: "totp",
               state: "two",
               username: sessionStorage.getItem("user"),
               id: $("#id").val(),
               twoFactor: $("#twoFactor").val() + "---" + $("#ka").val() + "---" + $("#iva").val(),
               newTwoFactor: $("#newTwoFactor").val() + "---" + $("#kb").val() + "---" + $("#ivb").val(),
               alias: $("#newDeviceAlias").val()
           },
           success: function(data) {
               console.log(data);
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
               } else {
                   $("#addDevice").closeModal();
                   sessionStorage.setItem("devices", $(data).find("devices").html());
                   loadUserDeviceList();
               }
           }
        });
    }
    
    /**
     * Request combined user's device locking
     */
    function uncombineRequest() {
        $.ajax({
            url: "Sanc",
            method: "post",
            data: {
                requestType: "uncombine",
                protoType: "totp",
                state: "one"
            },
            success: function(data) {
            console.log(data);
                if($(data).find("e").length) {
                    $("span.error").text($(data).find("e").html());
                } else {
                    $("#id").val($(data).find("id").html());
                    $("#ka").val($(data).find("ka").html());
                    $("#iva").val($(data).find("iva").html());
                    $("#blockCombinedDevice").openModal({dismissible:false});
                }
            }
        });
    }
    
    /**
     * Confirm lock for combined user's device
     */
    function uncombineConfirm() {
        $.ajax({
           url: "Sanc",
           method: "post",
           data: {
               requestType: "uncombine",
               protoType: "totp",
               state: "two",
               username: sessionStorage.getItem("user"),
               alias: $("#aliasToBlock").text(),
               passphrase: $("#passphrase").val() + "---" + $("#ka").val() + "---" + $("#iva").val(),
               id: $("#id").val()
           },
           success: function(data) {
               console.log(data);
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
               } else {
                   $("#blockCombinedDevice").closeModal();
                   sessionStorage.setItem("devices", $(data).find("devices").html());
                   loadUserDeviceList();
               }
           }
        });
    }
    
    /**
     * Request temporary credentials for retire interim state
     */
    function removeTemporaryCredentialsRequest() {
        $.ajax({
           url: "Sanc",
           method: "post",
           data: {
               requestType: "renew",
               protoType:"totp",
               state: "one"
           },
           success: function(data) {
               console.log(data);
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
               } else {
                    $("#id").val($(data).find("id").html());
                    $("#ka").val($(data).find("ka").html());
                    $("#iva").val($(data).find("iva").html());
                    $("#kb").val($(data).find("kb").html());
                    $("#ivb").val($(data).find("ivb").html());
                    $("#ks").val($(data).find("ks").html());
                    $("#ks").val("otpauth://totp/" + sessionStorage.getItem("user") +
                            "?secret=" + $("#ks").val());
                    $("#renewTemporaryCredentials").openModal({dismissible:false});
                    new QRCode(document.getElementById("qrcodeRenewer"), $("#ks").val());
               }
           }
        });
    }
    
    /**
     * Confirm interim state cancellation
     */
    function removeTemporaryCredentialsConfirm() {
        $.ajax({
           url: "Sanc",
           method: "post",
           data: {
               requestType: "renew",
               protoType: "totp",
               state: "two",
               username: sessionStorage.getItem("user"),
               alias: $("#deviceToRenew").val(),
               id: $("#id").val(),
               twoFactor: $("#pinToRenew").val() + "---" + $("#ka").val() + "---" + $("#iva").val(),
               passphrase: $("#passphraseToRenew").val() + "---" + $("#kb").val() + "---" + $("#ivb").val()
           },
           success: function(data) {
               console.log(data);
               if($(data).find("e").length) {
                   $("span.error").text($(data).find("e").html());
                } else {
                    $("#renewTemporaryCredentials").closeModal();
                    sessionStorage.setItem("devices", $(data).find("devices").html());
                    loadUserDeviceList();
                    $("#removeTemporaryCredentials").remove();
                }
           }
        });
    }
    
    /**
     * Check if temporary credentials is used to access into accout.
     * If positive it is shown a button to permit to renew 
     */
    function checkTemporaryCredentials() {
        if(sessionStorage.getItem("message") !== null) {
            $("#tempCredentialsAlert").openModal({dismissible:false});
            $("nav").after("<a style='margin:20px;' id='removeTemporaryCredentials' class='col s2 offset-s1 btn right blue'>REMOVE TEMPORARY</a>");
        }
    }
    
    /**
     * Visual validator data form
     */
     $(document).on("keyup keydown", "#", function(){
        if($(this).val().length < $(this).attr("length")) {
            $(this).css("border-bottom", "1px solid red");
            $(this).css("box-shadow", "0 1px 0 0 red");
        } else{
            $(this).css("border-bottom", "1px solid lightgreen");
            $(this).css("box-shadow", "0 1px 0 0 lightgreen");   
        }
    });
</script>